package br.uea.transirie.mypay.mypaytemplate.ui.ajustes

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityEditarColaboradorBinding
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityEditarEstabelecimentoBinding
import br.uea.transirie.mypay.mypaytemplate.model.Estabelecimento
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.model.Usuario
import br.uea.transirie.mypay.mypaytemplate.utils.*
import br.uea.transirie.mypay.mypaytemplate.viewmodel.CadastroEstabelecimentoEUsuarioViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_estabelecimento.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EditarEstabelecimentoActivity : AppCompatActivity() {
    private lateinit var viewModel: CadastroEstabelecimentoEUsuarioViewModel
    private val tag = "EDITAR_ESTAB_E_USU"
    private var usuario: Usuario? = null
    private var estabelecimento: Estabelecimento? = null
    private val myContext = this
    private val alterandoDadosGerente = true
    private val binding by lazy { ActivityEditarEstabelecimentoBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val colorDrawable = ColorDrawable(getNewColor(myContext))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Editar dados"

        viewModel = CadastroEstabelecimentoEUsuarioViewModel(AppDatabase.getDatabase(myContext))

        binding.cadastroEstabelecimentoEtTelefone.let {
            it.addTextChangedListener(MyMask(MaskType.Telephone, it))
        }

        usuario = intent.getParcelableExtra(getString(R.string.EXTRA_USUARIO))
        estabelecimento = intent.getParcelableExtra(getString(R.string.EXTRA_ESTABELECIMENTO))

        preencherDadosColaborador()
        binding.cadastroEstabelecimentoBtAvancar.setOnClickListener { checkAndAdvance() }
    }
    private fun preencherDadosColaborador(){
        usuario!!.let {
            with(it){
                val nomeCompleto = nome.split(" ", limit = 2)
                binding.cadastroEstabelecimentoEtEmail.setText(email)
                binding.cadastroEstabelecimentoEtNomeUsuario.setText(nomeCompleto[0])
                binding.cadastroEstabelecimentoEtSobrenomeUsuario.setText(nomeCompleto[1])
                binding.cadastroEstabelecimentoEtTelefone.setText(telefone)
            }
        }
        estabelecimento!!.let {
            with(it){
                binding.cadastroEstabelecimentoEtNome.setText(nomeFantasia)
                binding.cadastroEstabelecimentoEtCNPJ.setText(cnpj)
            }
        }
    }
    private fun checkAndAdvance(){
        val myValidations = MyValidations(myContext)

        val isEmailOkay = !myValidations.emailHasErrors(binding.cadastroEstabelecimentoTiEmail)
        val isNomeUsuarioOkay = !myValidations.inputHasEmptyError(binding.cadastroEstabelecimentoTiNomeUsuario)
        val isSobrenomeUsuarioOkay = !myValidations.inputHasEmptyError(binding.cadastroEstabelecimentoTiSobrenomeUsuario)
        val isTelefoneOkay = !myValidations.telefoneHasErrors(binding.cadastroEstabelecimentoTiTelefone)
        val isNomeEstabOkay = !myValidations.inputHasEmptyError(binding.cadastroEstabelecimentoTiNome)

        if (isEmailOkay){
            Log.d(tag, "O email ?? v??lido.")

            verificaEmailLocalmente {
                if (isNomeUsuarioOkay && isSobrenomeUsuarioOkay && isTelefoneOkay && isNomeEstabOkay){
                    Log.d(tag, "Todos os dados s??o v??lidos.")

                    val email = binding.cadastroEstabelecimentoEtEmail.text.toString()
                    val espacoChar = ' '
                    val nome = binding.cadastroEstabelecimentoEtNomeUsuario.text.toString()
                    val sobrenome = binding.cadastroEstabelecimentoEtSobrenomeUsuario.text.toString()
                    val nomeUsuarioCompleto = nome + espacoChar + sobrenome
                    val telefone = binding.cadastroEstabelecimentoEtTelefone.text.toString()
                    val nomeEstab = binding.cadastroEstabelecimentoEtNome.text.toString()

                    estabelecimento!!.nomeFantasia = nomeEstab
                    usuario!!.email = email
                    usuario!!.nome = nomeUsuarioCompleto
                    usuario!!.telefone = telefone

                    doAsync {
                        viewModel.updateUsuario(usuario!!)
                        viewModel.updateEstab(estabelecimento!!)
                        uiThread {
                            val intent = Intent(myContext, SplashUpdateConcluidoActivity::class.java)
                                .putExtra(getString(R.string.ALTERANDO_DADOS_GERENTE), alterandoDadosGerente)

                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }
    /**
     * Fun????o que verifica se o email j?? est?? em uso localmente.
     *
     * @param callback flag ativada apenas se o email estiver dispon??vel para uso.
     */
    private fun verificaEmailLocalmente(callback: () -> Unit) {
        val email = binding.cadastroEstabelecimentoEtEmail.text.toString()

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
                    binding.cadastroEstabelecimentoTiEmail.error = errorMsg

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