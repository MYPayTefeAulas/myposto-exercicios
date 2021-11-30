package br.uea.transirie.mypay.mypaytemplate.ui.historico

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.adapters.DetalhesAdapter
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.utils.impressora.printer.ModulePrinter
import br.uea.transirie.mypay.mypaytemplate.utils.toPrecoString
import br.uea.transirie.mypay.mypaytemplate.viewmodel.DetalhesAtendimentoViewModel
import kotlinx.android.synthetic.main.activity_detalhes_atendimento.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class DetalhesAtendimentoActivity : AppCompatActivity() {
    private val myContext = this@DetalhesAtendimentoActivity
    private val viewModel = DetalhesAtendimentoViewModel(AppDatabase.getDatabase(myContext))
    private val adapterDetalhes = DetalhesAdapter(myContext, mutableListOf())
    private var listaPrint = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_atendimento)

        val idVeiculo = intent.getLongExtra(getString(R.string.EXTRA_VEICULO_ID), 0)

        val mlayoutManager = LinearLayoutManager(myContext)
        mlayoutManager.orientation = LinearLayoutManager.VERTICAL

        detalhes_rvServicos.layoutManager = mlayoutManager
        detalhes_rvServicos.adapter = adapterDetalhes

        doAsync {
            val veiculo = viewModel.veiculoId(idVeiculo)
            val pagamento = viewModel.pagamentoByAtendimentoId(idVeiculo)
            val atendimento = viewModel.atendimentoId(idVeiculo)

            val txtData = pagamento.dataPagamento.substringBefore(" ")
            val txtHoraInicio = atendimento.dataRecebimento.substringAfter(" ")
            val txtHoraConclusao = pagamento.dataPagamento.substringAfter(" ")
            val precoTotal = atendimento.valorTotal

            updateListaServico()

            uiThread {
                detalhes_txtSubTotal.text = precoTotal.toPrecoString()
                detalhes_txtPlaca.text = veiculo.placa
                detalhes_txtData.text = txtData
                detalhes_txtHoraInicio.text = txtHoraInicio
                detalhes_txtHoraTermino.text = txtHoraConclusao
            }
        }

        detalhes_btnReimprimir.setOnClickListener {
            printReceipt()
        }

        detalhes_btnCancelar.setOnClickListener {
            finish()
        }

        detalhes_toolBar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun printReceipt() {
        val printer = ModulePrinter(this)
        Log.d("DETALHES_ATENDIMENTO", " - - PRINT RECIBO - - ")

        val recibo = "\n" +
                "\n" +
                "\n  PLACA: ${detalhes_txtPlaca.text}" +
                "\n" +
                "\n  LISTA SERVICOS" +
                listaPrint +
                "\n" +
                "\n  DATA: ${detalhes_txtData.text}" +
                "\n  HORÁRIO DE ENTRADA: ${detalhes_txtHoraInicio.text}" +
                "\n  HORÁRIO DE SAÍDA: ${detalhes_txtHoraTermino.text}" +
                "\n  VALOR: ${detalhes_txtSubTotal.text}" +
                "\n" +
                "\n"

        printer.printText(recibo)

        Toast.makeText(this,"Impressão iniciada", Toast.LENGTH_SHORT).show()
        Log.d("DETALHES_ATENDIMENTO", recibo)

    }

    private fun updateListaServico(){
        listaPrint = ""
        doAsync {
            val listaServicos = viewModel.listaServicosVeiculo()

            uiThread {
                adapterDetalhes.swapData(listaServicos)
                if (listaServicos.isNotEmpty()) {
                    listaServicos.forEach {
                        listaPrint += "\n  ${it.descricao}  ${it.preco.toPrecoString()}"
                    }
                }
                else listaPrint += "\n  (sem serviços)"
            }
        }
    }
}