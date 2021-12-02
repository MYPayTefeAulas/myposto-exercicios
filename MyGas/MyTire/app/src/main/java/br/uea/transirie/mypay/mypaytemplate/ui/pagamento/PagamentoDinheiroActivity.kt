package br.uea.transirie.mypay.mypaytemplate.ui.pagamento

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.model.Pagamento
import br.uea.transirie.mypay.mypaytemplate.model.TipoPagamento
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.utils.*
import br.uea.transirie.mypay.mypaytemplate.viewmodel.PagamentoViewModel
import kotlinx.android.synthetic.main.activity_pagamento_dinheiro.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*

class PagamentoDinheiroActivity : AppCompatActivity() {
    private val classTag = "PAGAMENTO_DINHEIRO"
    private val myContext = this@PagamentoDinheiroActivity
    private val viewModel = PagamentoViewModel(AppDatabase.getDatabase(myContext))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagamento_dinheiro)

        title = getString(R.string.titulo_pagamento_dinheiro)

        val colorDrawable = ColorDrawable(getNewColor(myContext))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

        // Recupera dados sobre o pagamento
        val atendimentoId = intent.getLongExtra("EXTRA_ATENDIMENTO_ID", 0)
        val valor = intent.getDoubleExtra("EXTRA_PRECO_TOTAL", 0.0)

        // Exibe dados sobre o pagamento na tela
        doAsync {
            val placa = viewModel.placaByAtendimentoId(atendimentoId)
            val horaEntrada = viewModel.horaAtendimentoById(atendimentoId)
            uiThread {
                pagDinheiro_tvPlacaValue.text = placa
                pagDinheiro_tvEntradaValue.text = horaEntrada
            }
        }

        val dataFormatter = SimpleDateFormat(getString(R.string.formato_data), Locale.getDefault())
        val dataPagamento = dataFormatter.format(Date())
        val horaPagamento = dataPagamento.substringAfter(" ")
        pagDinheiro_tvSaidaValue.text = horaPagamento

        val valorString = valor.toPrecoString()
        pagDinheiro_tvTotalValue.text = valorString

        pagamentoDinheiro_etValorRecebido.let {
            it.addTextChangedListener(MyMask(MaskType.Price, it))
        }

        pagamentoDinheiro_etValorRecebido.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            /* Transforma o texto com o valor recebido em Float e calcula o troco */
            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    pagamentoDinheiro_tiValorRecebido.error = null

                    Log.i(classTag, "Valor a pagar: $valor")

                    val valorRecebido = it.toString().toPrecoDouble()
                    Log.i(classTag, "Valor recebido em double: $valorRecebido")

                    val troco = valorRecebido.minus(valor)
                    val trocoString = troco.toPrecoString()
                    Log.i(classTag, "$troco")

                    if (troco >= -0.00) {
                        pagamentoDinheiro_etTroco.setText(trocoString)
                    } else {
                        pagamentoDinheiro_tiValorRecebido.error =
                            getString(R.string.erro_valor_recebido_menor_que_valor_a_pagar)
                    }
                }
            }
        })

        pagamentoDinheiro_etValorRecebido.setText(valorString)

        /* Armazena o objeto Pagamento e passa para a próximo tela */
        pagamentoDinheiro_btConcluir.setOnClickListener {
            val userPrefs = getSharedPreferences(getString(R.string.PREF_USER_DATA), Context.MODE_PRIVATE)
            val usuario = userPrefs.getString(getString(R.string.PREF_USER_ESTABELECIMENTO_CNPJ),"").toString()
            val pagamento = Pagamento(
                    0,
                    atendimentoId,
                    usuario,
                    valor,
                    dataPagamento,
                    TipoPagamento.DINHEIRO
            )

            doAsync {
                viewModel.cadastraPagamento(pagamento)
                startActivity(Intent(myContext, PagamentoSucessoActivity::class.java))
            }
        }

        /* Volta a tela anterior */
        pagamentoDinheiro_btCancelar.setOnClickListener {
            this.onBackPressed()
        }
    }

    //função do botão de voltar padrão de cada tela
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}