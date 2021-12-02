package br.uea.transirie.mypay.mypaytemplate.ui.ajustes

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityEditarColaboradorBinding
import br.uea.transirie.mypay.mypaytemplate.model.Usuario
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.ui.cadastro.GerarPinActivity
import br.uea.transirie.mypay.mypaytemplate.utils.MaskType
import br.uea.transirie.mypay.mypaytemplate.utils.MyMask
import br.uea.transirie.mypay.mypaytemplate.utils.MyValidations
import br.uea.transirie.mypay.mypaytemplate.utils.getNewColor
import br.uea.transirie.mypay.mypaytemplate.viewmodel.CadastroEstabelecimentoEUsuarioViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EditarColaboradorActivity : AppCompatActivity() {
    private lateinit var viewModel: CadastroEstabelecimentoEUsuarioViewModel
    private val tag = "EDITAR_COLABORADOR"
    private var usuario: Usuario? = null
    private val myContext = this
    private val binding by lazy { ActivityEditarColaboradorBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val colorDrawable = ColorDrawable(getNewColor(myContext))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Editar colaborador"

        viewModel = CadastroEstabelecimentoEUsuarioViewModel(AppDatabase.getDatabase(myContext))

        binding.editarColaboradorEtTelefone.let {
            it.addTextChangedListener(MyMask(MaskType.Telephone, it))
        }

        usuario = intent.getParcelableExtra(getString(R.string.EXTRA_USUARIO))
        preencherDadosColaborador()
        binding.editarColaboradorBtConcluir.setOnClickListener { checkAndAdvance() }
    }
    private fun preencherDadosColaborador(){
        usuario!!.let {
            with(it){
                val nomeCompleto = nome.split(" ", limit = 2)
                binding.editarColaboradorEtEmail.setText(email)
                binding.editarColaboradorEtNomeUsuario.setText(nomeCompleto[0])
                binding.editarColaboradorEtSobrenomeUsuario.setText(nomeCompleto[1])
                binding.editarColaboradorEtTelefone.setText(telefone)
            }
        }
    }
    private fun checkAndAdvance(){
        val myValidations = MyValidations(myContext)

        val isEmailOkay = !myValidations.emailHasErrors(binding.editarColaboradorTiEmail)
        val isNomeUsuarioOkay = !myValidations.inputHasEmptyError(binding.editarColaboradorTiNomeUsuario)
        val isSobrenomeUsuarioOkay = !myValidations.inputHasEmptyError(binding.editarColaboradorTiSobrenomeUsuario)
        val isTelefoneOkay = !myValidations.telefoneHasErrors(binding.editarColaboradorTiTelefone)

        if (isEmailOkay){
            Log.d(tag, "O email é válido.")

            verificaEmailLocalmente {
                if (isNomeUsuarioOkay && isSobrenomeUsuarioOkay && isTelefoneOkay){
                    Log.d(tag, "Todos os dados são válidos.")

                    val email = binding.editarColaboradorEtEmail.text.toString()
                    val espacoChar = ' '
                    val nome = binding.editarColaboradorEtNomeUsuario.text.toString()
                    val sobrenome = binding.editarColaboradorEtSobrenomeUsuario.text.toString()
                    val nomeUsuarioCompleto = nome + espacoChar + sobrenome
                    val telefone = binding.editarColaboradorEtTelefone.text.toString()

                    usuario!!.email = email
                    usuario!!.nome = nomeUsuarioCompleto
                    usuario!!.telefone = telefone

                    doAsync {
                        viewModel.updateUsuario(usuario!!)
                        uiThread {
                            val intent = Intent(myContext, SplashUpdateConcluidoActivity::class.java)

                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
    /**
     * Função que verifica se o email já está em uso localmente.
     *
     * @param callback flag ativada apenas se o email estiver disponível para uso.
     */
    private fun verificaEmailLocalmente(callback: () -> Unit) {
        val email = binding.editarColaboradorEtEmail.text.toString()

        doAsync {
            val emailJaEmUso = when (email == usuario!!.email){
                true -> false
                else -> {
                    viewModel.emailJaEmUsoLocalmente(email)
                }
            }

            uiThread {
                if (emailJaEmUso) {
                    val errorMsg =
                        getString(R.string.erro_email_ja_em_uso_tente_outro)
                    binding.editarColaboradorTiEmail.error = errorMsg

                    Log.d(tag, errorMsg)
                }
                else callback()
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}