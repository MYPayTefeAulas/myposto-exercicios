package br.uea.transirie.mypay.mypaytemplate.utils.work_manager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.model.Estabelecimento
import br.uea.transirie.mypay.mypaytemplate.model.Usuario
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.*
import br.uea.transirie.mypay.mypaytemplate.utils.AppPreferences
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UpdateWorker(
    private val appContext: Context,
    workerParams: WorkerParameters,
) : Worker(appContext, workerParams) {
    private val logtag = "WORK_MANAGER"
    private lateinit var viewModel: WorkManagerViewModel
    private val db = Firebase.firestore
    private val userRef = db.collection(TABLE_USUARIO)
    private val estabelecimentoRef = db.collection(TABLE_ESTABELECIMENTO)
    private var cnpj = ""
    private var estab = Estabelecimento()
    private var listaUsu = listOf<Usuario>()

    /**
     * A função principal da classe. De início, recuperamos o CNPJ, os dados do estabelecimento e os
     * dados de usuário. Em seguida, realizamos o backup na nuvem e, no fim, gravamos a data e hora
     * do upload.
     */
    override fun doWork(): Result {
        Log.d(logtag, "BACKUP")

        cnpj = AppPreferences.getCNPJLogado(appContext)
        if (cnpj.isNotEmpty()) {
            doAsync {
                viewModel = WorkManagerViewModel(AppDatabase.getDatabase(appContext))
                val estabelecimento = viewModel.estabelecimentoByCNPJ(cnpj)
                val listaUsuarios = viewModel.getUsuarios()

                uiThread {
                    if (estabelecimento != null) {
                        estab = estabelecimento
                        listaUsu = listaUsuarios

                        atualizaNuvem { taskCompleted ->
                            if (taskCompleted)
                                atualizaUltimoUpload()
                        }
                    }
                    else Log.d(logtag, appContext.getString(R.string.erro_no_estabelecimento_cadastrado))
                }
            }
        }

        return Result.success()
    }

    /**
     * Aqui é onde chamamos as funções que atualizam o banco de dados da nuvem.
     */
    private fun atualizaNuvem(callback: (Boolean) -> Unit) {
        verificaUsuarios { usuariosAtualizados ->
            if (usuariosAtualizados)
                verificaEstabelecimento { estabAtualizados ->
                    if (estabAtualizados)
                        callback(true)
                }
        }
    }

    /**
     * Nesta função, procuramos pelos usuários do estabelecimento logado no banco de dados da nuvem.
     * todo descrever o resto
     */
    private fun verificaUsuarios(callback: (Boolean) -> Unit) {
        Log.d(logtag, "Iniciando localização dos usuários no firebase...")

        userRef.whereEqualTo(
            COLUMN_ESTABELECIMENTO_CNPJ, cnpj
        ).get().addOnSuccessListener { document ->
            Log.d(logtag, "Localização dos usuários no firebase completa.")

            if (document.metadata.isFromCache) {
                Log.w(logtag, appContext.getString(R.string.erro_dados_cache))
                callback(false)
            } else {
                val listaUsuariosExiste = !document.isEmpty
                val listaUsuariosFirebase = mutableListOf<Usuario>()

                val simOuNao = if (listaUsuariosExiste) "Sim" else "Não"
                Log.d(logtag, "Cadastro existe? $simOuNao")

                if (listaUsuariosExiste) {

                    /** transforma document do firebase em uma lista de usuários **/
                    document.forEach { usuario ->
                        val usu = Usuario(
                            id = usuario[COLUMN_ID] as Long,
                            estabelecimentoCNPJ = usuario[COLUMN_ESTABELECIMENTO_CNPJ] as String,
                            pin = (usuario[COLUMN_PIN] as Long).toInt(),
                            nome = usuario[COLUMN_NOME] as String,
                            email = usuario[COLUMN_EMAIL] as String,
                            telefone = usuario[COLUMN_TELEFONE] as String,
                            isGerente = usuario[COLUMN_IS_GERENTE] as Boolean
                        )
                        listaUsuariosFirebase.add(usu)
                    }

                    /** Deleta do firebase os usuários que não estão no local */
                    listaUsuariosFirebase.forEach {
                        if (!listaUsu.contains(it)) {
                            userRef.document(it.email).delete()
                        }
                    }
                }

                listaUsu.forEach {
                    if (!listaUsuariosFirebase.contains(it)) {
                        val data = hashMapOf(
                            COLUMN_ID to it.id,
                            COLUMN_ESTABELECIMENTO_CNPJ to it.estabelecimentoCNPJ,
                            COLUMN_NOME to it.nome,
                            COLUMN_EMAIL to it.email,
                            COLUMN_TELEFONE to it.telefone,
                            COLUMN_PIN to it.pin,
                            COLUMN_IS_GERENTE to it.isGerente
                        )
                        userRef.document(it.email).set(data)
                    }
                }

                callback(true)
            }
        }
    }

    /**
     * Nesta função, procuramos pelo estabelcimento logado no banco de dados da nuvem.
     * todo descrever o resto
     */
    private fun verificaEstabelecimento(callback: (Boolean) -> Unit) {
        Log.d(logtag, "Iniciando procura pelo estabelecimento no firebase...")

        estabelecimentoRef.whereEqualTo(
            COLUMN_CNPJ, cnpj
        ).get().addOnSuccessListener { document ->
            Log.d(logtag, "Procura pelo estabelecimento no firebase completa.")

            if (document.metadata.isFromCache) {
                Log.w(logtag, appContext.getString(R.string.erro_dados_cache))
                callback(false)
            } else {
                if (document.isEmpty) {
                    Log.d(logtag, "Na nuvem, não há um estabelecimento cadastrado com esse CNPJ.")

                    val dataEstabelecimento = hashMapOf(
                        COLUMN_ID to estab.id,
                        COLUMN_NOME_FANTASIA to estab.nomeFantasia,
                        COLUMN_CNPJ to estab.cnpj,
                    )

                    estabelecimentoRef.document(
                        estab.cnpj.filter { it.isDigit() }
                    ).set(dataEstabelecimento)
                } else {
                    Log.d(logtag, "Na nuvem, já há um estabelecimento cadastrado com esse CNPJ.")
                    var estabFirebase = Estabelecimento()

                    document.map { estab ->
                        estabFirebase = Estabelecimento(
                            id = estab[COLUMN_ID] as Long,
                            nomeFantasia = estab[COLUMN_NOME_FANTASIA] as String,
                            cnpj = estab[COLUMN_CNPJ] as String
                        )
                    }

                    if (estabFirebase != estab) {
                        estabelecimentoRef.document(
                            estabFirebase.cnpj.filter { it.isDigit() }
                        ).delete()

                        val dataEstabelecimento = hashMapOf(
                            COLUMN_ID to estab.id,
                            COLUMN_NOME_FANTASIA to estab.nomeFantasia,
                            COLUMN_CNPJ to estab.cnpj,
                        )

                        estabelecimentoRef.document(
                            estab.cnpj.filter { it.isDigit() }
                        ).set(dataEstabelecimento)
                    }
                }

                callback(true)
            }
        }
    }

    /**
     * Nesta função, gravamos, no registro local do estabelecimento, a data e hora do upload realizado.
     * todo
     */
    private fun atualizaUltimoUpload() {
        val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy',' HH:mm")
        val dataEHora = LocalDateTime.now().format(dtf)
        Log.d(logtag, "Data e hora: $dataEHora")

        AppPreferences.setUltimoUpload(dataEHora, appContext)
    }
}