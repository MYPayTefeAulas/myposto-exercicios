package br.uea.transirie.mypay.mypaytemplate.ui.pagamento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.model.TipoPagamento
import kotlinx.android.synthetic.main.activity_formas_pagamento.*

class FormasPagamentoActivity : AppCompatActivity() {
    private val context = this@FormasPagamentoActivity
    private val extraAtendimento: String = "EXTRA_ATENDIMENTO_ID"
    private val extraValor: String = "EXTRA_PRECO_TOTAL"
    private val extraPagamento: String = "EXTRA_FORMA_PAGAMENTO"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formas_pagamento)

        title = getString(R.string.formas_pagamento)

        val atendimentoId = intent.getLongExtra(extraAtendimento, 0)
        val valor = intent.getDoubleExtra(extraValor, 0.0)

        formasPagamento_btCredito.setOnClickListener {
            val intent = Intent(context, PagamentoCartaoActivity::class.java)

            intent.putExtra(extraAtendimento, atendimentoId)
            intent.putExtra(extraValor, valor)
            intent.putExtra(extraPagamento, TipoPagamento.CREDITO)

            startActivity(intent)
        }
        formasPagamento_btDebito.setOnClickListener {
            val intent = Intent(context, PagamentoCartaoActivity::class.java)

            intent.putExtra(extraAtendimento, atendimentoId)
            intent.putExtra(extraValor, valor)
            intent.putExtra(extraPagamento, TipoPagamento.DEBITO)

            startActivity(intent)
        }
        formasPagamento_btDinheiro.setOnClickListener {
            val intent = Intent(context, PagamentoDinheiroActivity::class.java)

            intent.putExtra(extraAtendimento, atendimentoId)
            intent.putExtra(extraValor, valor)

            startActivity(intent)
        }
    }

    //função do botão de voltar padrão de cada tela
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}