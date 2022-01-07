package br.uea.transirie.mypay.mypaytemplate.ui.servicos

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.model.Servico
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.utils.*
import br.uea.transirie.mypay.mypaytemplate.viewmodel.CadastrarServicoViewModel
import br.uea.transirie.mypay.mypaytemplate.viewmodel.TabelaServicosViewModel
import kotlinx.android.synthetic.main.activity_cadastrar_servico.*
import kotlinx.android.synthetic.main.activity_editar_servico.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class CadastrarServicoActivity : AppCompatActivity() {
    private val context = this@CadastrarServicoActivity
    private val viewModel = CadastrarServicoViewModel(AppDatabase.getDatabase(context))
    private lateinit var viewModelCadastros: TabelaServicosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_servico)

        title = getString(R.string.cadastrar_preco)

        cadastrarServico_etPreco.let { it.addTextChangedListener(MyMask(MaskType.Price, it)) }

        cadServico_btnConcluir.setOnClickListener {
            saveServiceData()
        }
    }

    /**
     * Função que verifica se os dados são válidos e então realiza o cadastro do serviço.
     */

    private fun saveServiceData() {
        hasErrors { callback ->
            if (!callback){
                val servico = Servico(
                        descricao = dado_nomeServico.editText?.text.toString(),
                        preco = dado_precoServico.editText?.text.toString().toPrecoDouble(),
                        desativado = false
                )

                doAsync {
                    viewModel.saveServico(servico)

                    uiThread {
                        val intent = Intent()
                            .putExtra(
                                getString(R.string.EXTRA_SERVICO_OP),
                                getString(R.string.SERVICO_OP_CADASTRO)
                            )

                        /** Informa o sucesso no cadastro para a activity anterior **/
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                        Log.i("CadServico", "Servico Cadastrado")
                    }
                }
            }
        }
    }


    private fun hasErrors(callback: (Boolean) -> Unit){
        doAsync {
            viewModelCadastros = TabelaServicosViewModel(AppDatabase.getDatabase(context))
            val listaServicos = viewModelCadastros.getAllServicos().filter { it.descricao ==  dado_nomeServico.editText?.text.toString()}
            uiThread {
                var hasErrors = false
                if (inputHasEmptyError(dado_nomeServico, context)) {
                    hasErrors = true
                }else if (listaServicos.isNotEmpty()){
                    dado_nomeServico.error = "Serviço já cadastrado"
                    hasErrors = true
                }

                if (inputHasEmptyError(dado_precoServico, context)) {
                    hasErrors = true
                } else if (precoGreaterThan10K(dado_precoServico, context)) {
                    hasErrors = true
                }
                callback(hasErrors)
            }
        }
    }
}