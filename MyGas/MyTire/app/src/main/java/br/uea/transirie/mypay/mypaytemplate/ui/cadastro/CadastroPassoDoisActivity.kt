package br.uea.transirie.mypay.mypaytemplate.ui.cadastro

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityCadastroPassoDoisBinding
import br.uea.transirie.mypay.mypaytemplate.model.Estabelecimento
import br.uea.transirie.mypay.mypaytemplate.model.Usuario
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.utils.*
import br.uea.transirie.mypay.mypaytemplate.viewmodel.CadastroEstabelecimentoEUsuarioViewModel
import kotlinx.android.synthetic.main.activity_movimentar_caixa.view.*

class CadastroPassoDoisActivity : AppCompatActivity() {
    private lateinit var viewModel: CadastroEstabelecimentoEUsuarioViewModel
    private val binding by lazy { ActivityCadastroPassoDoisBinding.inflate(layoutInflater) }
    private val tag = "CADASTRO_PASSO_2"
    private val myContext = this@CadastroPassoDoisActivity
    private var usuario: Usuario? = null
    private var estabelecimento: Estabelecimento? = null
    private var timedout = false
    private var emailNotVerificado = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(R.string.title_cadastro)

        val colorDrawable = ColorDrawable(getNewColor(myContext))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

        binding.cadastroEstabelecimentoEtTelefone.let {
            it.addTextChangedListener(MyMask(MaskType.Telephone, it))
        }

        viewModel = CadastroEstabelecimentoEUsuarioViewModel(AppDatabase.getDatabase(myContext))

        binding.cadastroEstabelecimentoBtConcluir.setOnClickListener{
            checkAndSaveData()
        }
    }

    /**
     * Esta var ?? utilizada para tratar erros ocorridos durante o cadastro na nuvens.
     *
     * Atualmente, os seguintes erros s??o tratados:
     * - "The email is badly formatted" est?? sendo tratado."
     * - "The email address is already in use by another account."
     */
    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        when (result.resultCode) {
            Activity.RESULT_CANCELED -> {
                Log.d(tag, "Vish, deu algum problema no cadastro na nuvem...")

                val intent = result.data
                val erroCadastro = intent?.getStringExtra(getString(R.string.EXTRA_ERRO_CADASTRO))
                Log.d(tag, "O problema: $erroCadastro")

                when(erroCadastro) {
                    getString(R.string.erro_firebase_insira_email_valido) -> {
                        binding.cadastroEstabelecimentoTiEmail.error =
                            getString(R.string.erro_insira_email_valido)
                    }
                    getString(R.string.erro_firebase_email_ja_em_uso_tente_outro) -> {
                        binding.cadastroEstabelecimentoTiEmail.error =
                            getString(R.string.erro_email_ja_em_uso_tente_outro)
                    }
                    else -> {
                        if(erroCadastro != null)
                            Toast.makeText(myContext, erroCadastro, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    /**
     * Nessa fun????o, verificamos os dados de cadastro fornecidos pelo usu??rio e ent??o realizamos o
     * cadastro.
     *
     * A valida????o dos dados ?? feita por fun????es do arquivo de valida????es (pasta utils) que
     * verifica erros comuns no campos de entrada. Caso haja algum erro comum, ser?? exibida
     * uma mensagem abaixo do campo que cont??m o problema.
     *
     * Caso o email seja v??lido, ?? realizada uma busca no banco para verificar se o email informado
     * j?? est?? em uso. Caso esteja, exibimos o erro "Este email j?? est?? em uso. Tente outro." Caso
     * contr??rio, verificamos se a senha e confirma????o tamb??m s??o v??lidas e, se sim, realizamos o
     * cadastro.
     */
    private fun checkAndSaveData() {
        val myValidations = MyValidations(myContext)

        val isEmailOkay = !myValidations.emailHasErrors(
            binding.cadastroEstabelecimentoTiEmail
        )

        val isNomeUsuarioOkay = !myValidations.inputHasEmptyError(
            binding.cadastroEstabelecimentoTiNomeUsuario
        )

        val isSobrenomeUsuarioOkay = !myValidations.inputHasEmptyError(
            binding.cadastroEstabelecimentoTiSobrenomeUsuario
        )

        val isTelefoneOkay = !myValidations.telefoneHasErrors(
            binding.cadastroEstabelecimentoTiTelefone
        )

        if (isEmailOkay) {
            Log.d(tag, "O email ?? v??lido.")

            verificaEmail {
                Log.d(tag, "O email est?? dispon??vel.")

                if (isNomeUsuarioOkay && isTelefoneOkay && isSobrenomeUsuarioOkay) {
                    Log.i(tag, "O nome e o telefone s??o v??lidos")

                    val email = binding.cadastroEstabelecimentoEtEmail.text.toString().trim()

                    val espacoChar = ' '
                    val nome = binding.cadastroEstabelecimentoEtNomeUsuario.text.toString()
                    val sobrenome = binding.cadastroEstabelecimentoEtSobrenomeUsuario.text.toString()
                    val nomeUsuarioCompleto = nome + espacoChar + sobrenome

                    val telefone = binding.cadastroEstabelecimentoEtTelefone.text.toString()

                    usuario = Usuario(
                        email = email,
                        nome = nomeUsuarioCompleto,
                        telefone = telefone,
                        isGerente = true
                    )
                    estabelecimento = intent.getParcelableExtra(getString(R.string.EXTRA_ESTABELECIMENTO))

                    val intent = Intent(myContext, GerarPinActivity::class.java)
                        .putExtra(getString(R.string.EXTRA_USUARIO), usuario)
                        .putExtra(getString(R.string.EXTRA_ESTABELECIMENTO), estabelecimento)

                    startForResult.launch(intent)
                }
            }
        }
    }

    /**
     * Fun????o que verifica se o email j?? est?? em uso.
     *
     * @param callback flag ativada apenas se o email estiver dispon??vel para uso.
     */
    private fun verificaEmail(callback: () -> Unit) {
        val email = binding.cadastroEstabelecimentoEtEmail.text.toString()

        viewModel.emailJaEmUsoFirebase(email) { emailJaEmUso ->
            emailNotVerificado = false

            if (!timedout) {
                if (emailJaEmUso) {
                    val errorMsg =
                        getString(R.string.erro_email_ja_em_uso_tente_outro)
                    binding.cadastroEstabelecimentoTiEmail.error = errorMsg

                    Log.d(tag, errorMsg)
                }
                else callback()
            }
        }

        timeout { if (emailNotVerificado) exibeErroTimeout() }
    }

    /**
     * Esta fun????o cria um cron??metro de 2 segundos e, ap??s esse per??odo, se a tela n??o estiver
     * fechando, executa um callback e ativa a flag de timeout que impede a leitura de dados
     * recuperadas da nuvem ap??s esse tempo.
     */
    private fun timeout(callback: () -> Unit) {
        val delayMillis = 2000L
        timedout = false

        Handler(Looper.getMainLooper()).postDelayed({
            if (!this.isFinishing) {
                timedout = true
                callback()
            }
        }, delayMillis)
    }

    /**
     * Esta fun????o informa nos logs o erro de falta de conex??o e, em seguida, abre exibe uma mensagem
     * de erro ao usu??rio.
     */
    private fun exibeErroTimeout() {
        val toastMsg = getString(R.string.erro_conexao_email)
        Toast.makeText(myContext, toastMsg, Toast.LENGTH_LONG).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}