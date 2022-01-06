package br.uea.transirie.mypay.mypaytemplate.ui.pagamento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.model.Pagamento
import br.uea.transirie.mypay.mypaytemplate.model.TipoPagamento
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.utils.AppPreferences
import br.uea.transirie.mypay.mypaytemplate.utils.toPrecoString
import br.uea.transirie.mypay.mypaytemplate.viewmodel.PagamentoViewModel
import kotlinx.android.synthetic.main.activity_pagamento_cartao.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*

class PagamentoCartaoActivity : AppCompatActivity() {
    private val myContext = this@PagamentoCartaoActivity
    private val viewModel = PagamentoViewModel(AppDatabase.getDatabase(myContext))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagamento_cartao)

        val tipoPagamento =
                intent.getSerializableExtra("EXTRA_FORMA_PAGAMENTO") as TipoPagamento

        title = tipoPagamento.descricao

        //Recupera dados sobre o pagamento
        val atendimentoId = intent.getLongExtra("EXTRA_ATENDIMENTO_ID", 0)
        val valor = intent.getDoubleExtra("EXTRA_PRECO_TOTAL", 0.0)

        doAsync {
            val placa = viewModel.placaByAtendimentoId(atendimentoId)
            val horaEntrada = viewModel.horaAtendimentoById(atendimentoId)

            uiThread {
                pagCartao_tvPlacaValue.text = placa
                pagCartao_tvEntradaValue.text = horaEntrada
            }
        }

        val dataformatter = SimpleDateFormat(getString(R.string.formato_data), Locale.getDefault())
        val dataPagamento = dataformatter.format(Date())
        val horaPagamento = dataPagamento.substringAfter(" ")
        pagCartao_tvSaidaValue.text = horaPagamento

        pagCartao_tvTotalValue.text = valor.toPrecoString()

        pagamentoCartao_bt.setOnClickListener {
            val estabelecimentoCNPJ = AppPreferences.getCNPJLogado(myContext)
            //Cria o obj Pagamento
            val pagamento = Pagamento(0, atendimentoId, estabelecimentoCNPJ, valor, dataPagamento, tipoPagamento)

            doAsync {
                //Cadastra o objeto pagamento
                viewModel.cadastraPagamento(pagamento)

                //Navega para a activity que informa o sucesso da transação
                startActivity(Intent(myContext, PagamentoSucessoActivity::class.java))
            }
        }

        pagamentoCartao_btCancelar.setOnClickListener {
            this.onBackPressed()
        }
    }

    //função do botão de voltar padrão de cada tela
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}