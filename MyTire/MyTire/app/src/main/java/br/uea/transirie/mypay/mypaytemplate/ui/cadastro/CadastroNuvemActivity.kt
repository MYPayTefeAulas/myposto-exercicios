package br.uea.transirie.mypay.mypaytemplate.ui.cadastro

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityCadastroNuvemBinding
import br.uea.transirie.mypay.mypaytemplate.model.Estabelecimento
import br.uea.transirie.mypay.mypaytemplate.model.Usuario
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.*
import br.uea.transirie.mypay.mypaytemplate.viewmodel.CadastroEstabelecimentoEUsuarioViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class CadastroNuvemActivity : AppCompatActivity() {
    private lateinit var viewModel: CadastroEstabelecimentoEUsuarioViewModel
    private val binding by lazy { ActivityCadastroNuvemBinding.inflate(layoutInflater) }
    private val tag = "CADASTRO_NUVEM"
    private val myContext = this@CadastroNuvemActivity
    private var autenticacaoFeita = false
    private var estabelecimento: Estabelecimento? = null
    private var usuario: Usuario? = null

    /**
     *
     * ESTÁ FORA DO FLUXO NO MOMENTO - Para possibilitar um cadastro sem acesso à internet (07/11/2021)
     *
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        /** Recupera o Estabelecimento e Usuário recém-cadastrados nas telas anteriores **/
        estabelecimento = intent.getParcelableExtra(getString(R.string.EXTRA_ESTABELECIMENTO))
        usuario = intent.getParcelableExtra(getString(R.string.EXTRA_USUARIO))

        /** Inicializa o viewmodel para cadastrar localmente no fim */
        viewModel = CadastroEstabelecimentoEUsuarioViewModel(AppDatabase.getDatabase(myContext))

        /** Realiza a animação **/
        binding.cadastroNuvemIvRoda.animation =
            AnimationUtils.loadAnimation(myContext, R.anim.rotate_carregamento)

        estabelecimento?.let { estab ->
            /** Realiza o cadastro do novo estabelecimento no firebase. */
            createEstalecimentoFirebase(estab) {
                usuario?.let { usu ->
                    createUsuarioFirebase(usu) {
                        finalizaCadastro()
                    }
                }
            }

            /**  Tratamento para erro de conexão. */
            trataErroConexao()
        }
    }

    /**
     * Salva os dados de um novo estabelecimento na nuvem do firebase.
     */
    private fun createEstalecimentoFirebase(estabelecimento: Estabelecimento, callback: () -> Unit) {
        doAsync {
            val idEstabelecimento = viewModel.saveEstabelecimento(estabelecimento)

            uiThread {
                estabelecimento.id = idEstabelecimento
                val dataEstabelecimento = hashMapOf(
                    COLUMN_ID to estabelecimento.id,
                    COLUMN_NOME_FANTASIA to estabelecimento.nomeFantasia,
                    COLUMN_CNPJ to estabelecimento.cnpj,
                )

                Firebase.firestore.collection(TABLE_ESTABELECIMENTO).document(
                    estabelecimento.cnpj.filter { it.isDigit() }
                ).set(dataEstabelecimento).addOnCompleteListener { task ->
                    //usuario?.estabelecimentoId = estabelecimento.id

                    if (task.isSuccessful) callback()
                    else {
                        viewModel.deleteEstabelecimento(estabelecimento)
                        cancelActivity(task.exception?.message)
                    }
                }
            }
        }
    }

    private fun createUsuarioFirebase(usu: Usuario, callback: () -> Unit) {
        Firebase.auth.createUserWithEmailAndPassword(
            usu.email, usu.pin.toString()
        ).addOnCompleteListener { taskAuth ->
            if (taskAuth.isSuccessful) {
                Log.d(tag, "createUserWithEmail:success")
                autenticacaoFeita = true

                doAsync {
                    val idUsuario = viewModel.saveUsuario(usu)

                    uiThread {
                        val data = hashMapOf(
                            COLUMN_ID to idUsuario,
                            COLUMN_ESTABELECIMENTO_CNPJ to usu.estabelecimentoCNPJ,
                            COLUMN_NOME to usu.nome,
                            COLUMN_EMAIL to usu.email,
                            COLUMN_TELEFONE to usu.telefone,
                            COLUMN_PIN to usu.pin,
                            COLUMN_IS_GERENTE to usu.isGerente
                        )

                        Firebase.firestore.collection(TABLE_USUARIO).document(
                            usu.email
                        ).set(data).addOnCompleteListener { taskRegister ->
                            if (taskRegister.isSuccessful) callback()
                            else cancelActivity(taskRegister.exception?.message)
                        }
                    }
                }
            } else {
                Log.d(tag, "createUserWithEmail:failure", taskAuth.exception)
                Toast.makeText(baseContext, "Falha na autenticação.", Toast.LENGTH_SHORT).show()

                cancelActivity(taskAuth.exception?.message)
            }
        }
    }
    /**
     * Esta função fecha a tela e passa uma msg de erro à tela anterior via um ActivityResult que,
     * pos sua vez, foi invocado através de "startActivityForResult".
     */
    private fun cancelActivity(msg: String?) {
        val intent = Intent()
            .putExtra(getString(R.string.EXTRA_ERRO_CADASTRO), msg)

        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }

    /**
     * Esta função exibe a tela de erro por falta de conexão nos seguintes casos:
     * - se, após 5 segundos, não foi possível realizar a autenticação.
     * - se, após 10 segundos, a autenticação tenha sido finalizada, mas o cadastro não.
     */
    private fun trataErroConexao() {
        setATimeout {
            if (autenticacaoFeita) setATimeout {
                exibeErroTimeout()
            }
            else exibeErroTimeout()
        }
    }

    /**
     * Esta função cria um cronômetro de 5 segundos e, após esse período, se a tela não estiver
     * fechando, executa um callback.
     */
    private fun setATimeout(callback: (() -> Unit)) {
        val delayMillis = 5000L

        Handler(Looper.getMainLooper()).postDelayed({
            if (!this.isFinishing) callback()
        }, delayMillis)
    }

    /**
     * Esta função exibe informa nos logs o erro de falta de conexão e, em seguida, abre uma tela
     * para informar esse erro ao usuário.
     */
    private fun exibeErroTimeout() {
        val timeoutMessage = getString(R.string.ERRO_CADASTRO_TIMEOUT)
        Log.d(tag, timeoutMessage)

        val intent = Intent(myContext, CadastroErroConexaoActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Esta função realiza o cadastro local do estabelecimento.
     */
    private fun finalizaCadastro() {
        Log.d(tag, "Hora de cadastrar localmente!")

        val userPrefs = getSharedPreferences(getString(R.string.PREF_USER_DATA), Context.MODE_PRIVATE)
        val edit = userPrefs.edit()
        edit.putString(getString(R.string.PREF_USER_ESTABELECIMENTO_CNPJ), estabelecimento?.cnpj)
        edit.apply()

        startActivity(Intent(myContext, SplashCadastroConcluidoActivity::class.java))
    }
}