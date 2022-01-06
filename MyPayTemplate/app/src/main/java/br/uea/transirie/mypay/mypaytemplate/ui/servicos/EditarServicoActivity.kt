package br.uea.transirie.mypay.mypaytemplate.ui.servicos

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityEditarServicoBinding
import br.uea.transirie.mypay.mypaytemplate.model.Servico
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.utils.*
import br.uea.transirie.mypay.mypaytemplate.viewmodel.EditarServicoViewModel
import kotlinx.android.synthetic.main.activity_cadastrar_servico.*
import kotlinx.android.synthetic.main.activity_cadastrar_servico.view.*
import kotlinx.android.synthetic.main.activity_editar_servico.*
import kotlinx.android.synthetic.main.item_servico.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EditarServicoActivity : AppCompatActivity(){
    private val myContext = this@EditarServicoActivity
    private val viewModel = EditarServicoViewModel(AppDatabase.getDatabase(myContext))
    private val binding by lazy { ActivityEditarServicoBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(R.string.editar_precos)

        val servicoId = intent.getLongExtra(getString(R.string.extra_editar_servico_id), 0)

        editServico_etPreco.let { it.addTextChangedListener(MyMask(MaskType.Price, it)) }

        doAsync {
            viewModel.start(servicoId)

            uiThread {
                editServico_etNome.setText(viewModel.getServicoDescricao())
                editServico_etPreco.setText(viewModel.getServicoPreco())
            }
        }

        editServico_btnConcluir.setOnClickListener {
            updateDataServico(servicoId)
        }

        binding.btnDeletarItem.setOnClickListener {
            onDeleteClick()
        }
    }

    private fun onDeleteClick() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(myContext, R.style.AlertDialogTheme)
        builder.setMessage("Tem certeza que deseja excluir este item?")
        builder.setPositiveButton(getString(R.string.excluir)) { dialog, _ ->
            doAsync {
                viewModel.deleteServico()

                uiThread {
                    val intent = Intent()
                        .putExtra(
                            getString(R.string.EXTRA_SERVICO_OP),
                            getString(R.string.SERVICO_OP_DELETAR)
                        )

                    /** Informa o sucesso da operação para a activity anterior **/
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
            dialog.dismiss()
        }
        builder.setNegativeButton(getString(R.string.cancelar)) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun updateDataServico(servicoId: Long) {
        val isValid = !inputHasErrors()

        if (isValid) {
            val descricao = editServico_etNome.text.toString()
            val preco = editServico_etPreco.text.toString()
            val servico = Servico(servicoId, descricao, preco.toPrecoDouble())

            doAsync {
                viewModel.atualizarServico(servico)

                uiThread {
                    val intent = Intent()
                        .putExtra(
                            getString(R.string.EXTRA_SERVICO_OP),
                            getString(R.string.SERVICO_OP_EDITAR)
                        )

                    /** Informa o sucesso da operação para a activity anterior **/
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    private fun inputHasErrors(): Boolean {
        var hasErrors = false

        if (inputHasEmptyError(editServico_nomeServico, myContext)) {
            hasErrors = true
        }

        if (inputHasEmptyError(editServico_precoServico, myContext)){
            hasErrors = true
        } else if (precoGreaterThan10K(editServico_precoServico, myContext)) {
            hasErrors = true
        }

        return hasErrors
    }
}