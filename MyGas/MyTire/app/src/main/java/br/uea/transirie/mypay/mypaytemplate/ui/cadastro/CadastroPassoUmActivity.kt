package br.uea.transirie.mypay.mypaytemplate.ui.cadastro

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityCadastroPassoUmBinding
import br.uea.transirie.mypay.mypaytemplate.model.Estabelecimento
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.ui.home.LoginActivity
import br.uea.transirie.mypay.mypaytemplate.utils.*
import br.uea.transirie.mypay.mypaytemplate.viewmodel.CadastroEstabelecimentoEUsuarioViewModel

class CadastroPassoUmActivity : AppCompatActivity() {
    private lateinit var viewModel: CadastroEstabelecimentoEUsuarioViewModel
    private val binding by lazy { ActivityCadastroPassoUmBinding.inflate(layoutInflater) }
    private val tag = "CADASTRO_PASSO_1"
    private val myContext = this@CadastroPassoUmActivity
    private var estabelecimento:Estabelecimento? = null
    private var timedout = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(R.string.title_cadastro)

        val colorDrawable = ColorDrawable(getNewColor(myContext))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

        maskEditText(getString(R.string.MASK_CNPJ), binding.cadastroEstabelecimentoEtCNPJ)

        viewModel = CadastroEstabelecimentoEUsuarioViewModel(AppDatabase.getDatabase(myContext))

        //Botão de ação para salvamento de informações
        binding.cadastroEstabelecimentoBtAvancar.setOnClickListener{
            checkAndAdvance()
        }
    }

    /**
     * Nessa função, verificamos os dados de cadastro fornecidos pelo usuário no passo 1 e então
     * seguimos ao passo 2.
     *
     * A validação dos dados é feita por funções do arquivo de validações (pasta utils) que
     * verifica erros comuns no campos de entrada. Caso haja algum erro comum, será exibida
     * uma mensagem abaixo do campo que contém o problema.
     *
     * Caso o cnpj seja válido, é realizada uma busca no banco para verificar se o cnpj informado
     * já está em uso. Caso esteja indisponível, exibimos o erro "CNPJ já cadastrado." Caso esteja
     * disponível, verificamos se o nome do estabelecimento e o telefone também são válidas e, se
     * sim, seguimos ao passo 2 do cadastro.
     */
    private fun checkAndAdvance() {
        val myValidations = MyValidations(myContext)
        val isNomeEstabelecimentoOkay =
            !myValidations.inputHasEmptyError(binding.cadastroEstabelecimentoTiNome)
        val isCnpjOkay =
            !myValidations.cnpjHasErrors(binding.cadastroEstabelecimentoTiCNPJ)

        if (isCnpjOkay) {
            Log.d(tag, "O cnpj é válido.")

            verificaCNPJ {
                Log.d(tag, "O cnpj está disponível.")

                if (isNomeEstabelecimentoOkay) {
                    Log.d(tag, "O nome do estabelecimento e o telefone são válidos.")

                    val nomeEstabelecimento = binding.cadastroEstabelecimentoEtNome.text.toString()
                    val cnpj = binding.cadastroEstabelecimentoEtCNPJ.text.toString()

                    estabelecimento = Estabelecimento(nomeFantasia = nomeEstabelecimento,
                    cnpj = cnpj)

                    val intent = Intent(myContext, CadastroPassoDoisActivity::class.java)
                        .putExtra(
                            getString(R.string.EXTRA_ESTABELECIMENTO),
                            estabelecimento
                        )

                    startActivity(intent)
                }
                else Log.d(tag, "O nome de estabelecimento ou o telefone não são válidos.")
            }
        }
        else Log.d(tag, "O cnpj não é válido.")
    }

    /**
     * Função que verifica se o cnpj já está cadastrado.
     *
     * @param callback flag ativada apenas se o cnpj estiver disponível para uso.
     */
    private fun verificaCNPJ(callback: () -> Unit) {
        val cnpj = binding.cadastroEstabelecimentoEtCNPJ.text.toString()
        var cnpjNotVerificado = true

        viewModel.cnpjJaEmUsoFirebase(cnpj) { cnpjJaEmUso ->
            cnpjNotVerificado = false

            if (!timedout) {
                if (cnpjJaEmUso) {
                    val builder = AlertDialog.Builder(myContext)
                    builder.setMessage(getString(R.string.alerta_msg_cnpj_ja_em_uso))
                    builder.setPositiveButton(getString(R.string.sim)) { dialog, _ ->
                        val intent = Intent(myContext, LoginActivity::class.java)
                            .putExtra(getString(R.string.EXTRA_CNPJ), false)
                        startActivity(intent)

                        dialog.dismiss()
                        finish()
                    }
                    builder.setNegativeButton(getString(R.string.cancelar)) { dialog, _ ->
                        val errorMsg = getString(R.string.erro_cnpj_ja_cadastrado)
                        binding.cadastroEstabelecimentoTiCNPJ.error = errorMsg
                        dialog.dismiss()
                    }
                    builder.show()
                }
                else callback()
            }
        }

        timeout { if (cnpjNotVerificado) exibeErroTimeout() }
    }

    /**
     * Esta função cria um cronômetro de 2 segundos e, após esse período, se a tela não estiver
     * fechando, executa um callback e ativa a flag de timeout que impede a leitura de dados
     * recuperadas da nuvem após esse tempo.
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
     * Esta função informa nos logs o erro de falta de conexão e, em seguida, abre exibe uma mensagem
     * de erro ao usuário.
     */
    private fun exibeErroTimeout() {
        val toastMsg = getString(R.string.erro_conexao_cnpj)
        Toast.makeText(myContext, toastMsg, Toast.LENGTH_LONG).show()
    }
}