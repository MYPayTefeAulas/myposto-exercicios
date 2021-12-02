package br.uea.transirie.mypay.mypaytemplate.ui.atendimento

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.adapters.ResumoSaidaAdapter
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.ui.pagamento.FormasPagamentoActivity
import br.uea.transirie.mypay.mypaytemplate.utils.getNewColor
import br.uea.transirie.mypay.mypaytemplate.utils.impressora.printer.ModulePrinter
import br.uea.transirie.mypay.mypaytemplate.utils.toPrecoString
import br.uea.transirie.mypay.mypaytemplate.viewmodel.ResumoSaidaViewModel
import kotlinx.android.synthetic.main.activity_resumo_saida.*
import kotlinx.android.synthetic.main.item_resumo_saida.*
import kotlinx.android.synthetic.main.remocao_negada.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class ResumoSaidaActivity : AppCompatActivity() {
    private lateinit var viewModel: ResumoSaidaViewModel
    private val context = this@ResumoSaidaActivity
    private val adapter =
            ResumoSaidaAdapter(context, mutableListOf())

    private var atendimentoId: Long = 0
    private var listaPrint = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupInicial()

        doAsync {
            viewModel = ResumoSaidaViewModel(
                AppDatabase.getDatabase(context),
                atendimentoId
            )

            uiThread {
                // Exibe dados do atendimento na tela
                resumoSaida_tvDataEntradaValue.text = viewModel.getDataRecebimento()
                resumoSaida_tvHoraEntradaValue.text = viewModel.getHoraRecebimento()

                resumoSaida_tvPlacaValue.text = viewModel.getPlaca()
                resumoSaida_tvCategoriaValue.text = viewModel.getCategoria()

                resumoSaida_tvTelefoneValue.text = viewModel.getTelefone()

                updateAdapter()
            }
        }

        setListeners()
    }

    private fun setupInicial() {
        setContentView(R.layout.activity_resumo_saida)

        title = getString(R.string.resumo_saida)

        val colorDrawable = ColorDrawable(getNewColor(context))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

        atendimentoId = intent.getLongExtra("EXTRA_ATENDIMENTO_ID", 0)

        resumoSaida_rv.adapter = adapter
        resumoSaida_rv.layoutManager = LinearLayoutManager(context)
    }

    private fun setListeners() {
        resumoSaida_btPagarAgora.setOnClickListener {
            val intent = Intent(this, FormasPagamentoActivity::class.java)
                .putExtra("EXTRA_ATENDIMENTO_ID", atendimentoId)
                .putExtra("EXTRA_PRECO_TOTAL", viewModel.getPrecoTotal())

            startActivity(intent)
        }

        resumoSaida_btImprimir.setOnClickListener {
            printReceipt()
        }
    }

    private fun updateAdapter() {
        listaPrint = ""
        resumoSaida_tvTotalValue.text = viewModel.getPrecoTotalString()
        viewModel.getListaServicos().let { lista ->

            adapter.swapData(lista)
            if (lista.isNotEmpty()) {
                lista.forEach { item ->
                    listaPrint += "\n  ${item.descricao}  ${item.preco.toPrecoString()}"
                }
            }
            else listaPrint += "\n  (sem serviços)"
        }

    }

    private fun printReceipt() {
        val printer = ModulePrinter(this)
        Log.d("RESUMO_SAIDA", " - - PRINT RECIBO - - ")

        val recibo = "\n" +
                "\n" +
                "\n  PLACA: ${resumoSaida_tvPlacaValue.text}" +
                "\n  CATEGORIA: ${resumoSaida_tvCategoriaValue.text}" +
                "\n  TELEFONE: ${resumoSaida_tvTelefoneValue.text}" +
                "\n  DATA: ${resumoSaida_tvDataEntradaValue.text}" +
                "\n  HORA DE ENTRADA: ${resumoSaida_tvHoraEntradaValue.text}" +
                "\n" +
                "\n  LISTA DE SERVIÇOS" +
                listaPrint +
                "\n" +
                "\n  TOTAL: ${viewModel.getPrecoTotalString()}" +
                "\n" +
                "\n" +
                "\n" +
                "\n"

        printer.printText(recibo)

        Toast.makeText(this,"Impressão iniciada", Toast.LENGTH_SHORT).show()
        Log.d("RESUMO_SAIDA", recibo)
    }
}