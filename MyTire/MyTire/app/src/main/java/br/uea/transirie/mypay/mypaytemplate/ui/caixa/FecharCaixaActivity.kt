package br.uea.transirie.mypay.mypaytemplate.ui.caixa

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityFecharCaixaBinding
import br.uea.transirie.mypay.mypaytemplate.model.Pagamento
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.PREF_VERIF_ABRIR_CAIXA
import br.uea.transirie.mypay.mypaytemplate.ui.home.EntradaActivity
import br.uea.transirie.mypay.mypaytemplate.utils.impressora.printer.ModulePrinter
import com.google.gson.Gson
import java.text.DecimalFormat
import java.time.LocalDate

class FecharCaixaActivity : AppCompatActivity() {
    private var df = DecimalFormat("0.00")
    private lateinit var binding: ActivityFecharCaixaBinding
    private lateinit var usuario:String
    private lateinit var userPrefs: SharedPreferences
    private lateinit var user:String
    private lateinit var pref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFecharCaixaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()
        // resgata usuário que está logado
        userPrefs = getSharedPreferences(getString(R.string.PREF_USER_DATA), Context.MODE_PRIVATE)
        usuario = userPrefs.getString(getString(R.string.PREF_USER_ESTABELECIMENTO_CNPJ),"").toString()

        preencherTela()
        listeners()
    }
    private fun listeners(){
        binding.toolbar11.setNavigationOnClickListener { finish() }
        binding.button.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage("Ao fechar o caixa você encerra as atividades do dia, e irá sair do aplicativo. Tem certeza que quer continuar?")
            builder.setPositiveButton("FECHAR"){dialog,_->
                finishAffinity()
                startActivity(Intent(this,EntradaActivity::class.java))
                val sharedEditor = userPrefs.edit()
                sharedEditor.putString(usuario, Gson().toJson(CaixaStatus(status = true, data = LocalDate.now())))
                sharedEditor.putBoolean(getString(R.string.PREF_USER_LOGADO),false)
                sharedEditor.apply()
                dialog.dismiss()
            }
            builder.setNegativeButton("CANCELAR"){dialog,_-> dialog.cancel()}
            builder.show()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun preencherTela(){
        val data = Pagamento.dataHoje.dataHoje
        var mes = data.monthValue.toString()
        if (mes.length < 2){
            mes = "0$mes"
        }
        binding.dataAtual.text = "${data.dayOfMonth}/$mes/${data.year}"
        binding.totalClientes.text = "Total de clientes: ${Pagamento.totalClientes.totalCliente}"

        val faturamento = Pagamento.dinheiroRecebimento.valorDinheiro +
                Pagamento.debitoRecebimento.valorDebito +
                Pagamento.creditoRecebimento.valorCredito
        binding.faturamentoDiario.text = "Total: R$ ${df.format(faturamento).replace(".",",")}"

        pref = getSharedPreferences(PREF_VERIF_ABRIR_CAIXA, MODE_PRIVATE)
        user = pref.getString("email", "").toString()
        val statusAbrirCaixa= Gson().fromJson(pref.getString(user, Gson().toJson(AbrirCaixaStatus(status = false, data = LocalDate.now(), valor =0f))),
            AbrirCaixaStatus::class.java)
        val valFormat= String.format("%.2f", statusAbrirCaixa.valor)
        binding.caixaInicial.text="R$ $valFormat"

        binding.cartaoRecebimento.text = "Total: R$ ${df.format(Pagamento.creditoRecebimento.valorCredito + Pagamento.debitoRecebimento.valorDebito).replace(".",",")}"
        binding.dinheiroRecebimento.text = "Total: R$ ${df.format(Pagamento.dinheiroRecebimento.valorDinheiro).replace(".",",")}"

        binding.dinheiroDespesa.text = "Total: R$ ${df.format(Pagamento.despesasDinheiro.valorDespesasDinheiroDia).replace(".",",")}"
        binding.cartaoDespesa.text = "Total: R$ ${df.format(Pagamento.despesasCartao.valorDespesasCartaoCredito + Pagamento.despesasCartao.valorDespesasCartaoDebito).replace(".",",")}"

        val total = statusAbrirCaixa.valor +
                Pagamento.dinheiroRecebimento.valorDinheiro +
                Pagamento.debitoRecebimento.valorDebito +
                Pagamento.creditoRecebimento.valorCredito -
                Pagamento.despesasCartao.valorDespesasCartaoDebito -
                Pagamento.despesasCartao.valorDespesasCartaoCredito -
                Pagamento.despesasDinheiro.valorDespesasDinheiroDia
        binding.caixaFinal.text = "R$ ${df.format(total).replace(".",",")}"
        binding.btImprimir.setOnClickListener {
            val str = "     ${data.dayOfMonth}/$mes/${data.year}\n" +
                    "     Total de clientes: ${Pagamento.totalClientes.totalCliente}\n" +
                    "     Faturamento do dia: R$ ${df.format(faturamento).replace(".", ",")}\n\n" +
                    "     Recebimentos em crédito: R$ ${df.format(Pagamento.creditoRecebimento.valorCredito).replace(".", ",")}\n" +
                    "     Recebimentos em débito: R$ ${df.format(Pagamento.debitoRecebimento.valorDebito).replace(".", ",")}\n" +
                    "     Recebimento em dinheiro: R$ ${df.format(Pagamento.dinheiroRecebimento.valorDinheiro).replace(".", ",")}\n" +
                    "     Despesas no dinheiro: R$ ${df.format(Pagamento.despesasDinheiro.valorDespesasDinheiroDia).replace(".", ",")}\n" +
                    "     Despesas no cartão de crédito: R$ ${df.format(Pagamento.despesasCartao.valorDespesasCartaoCredito).replace(".", ",")}\n" +
                    "     Despesas no cartão de débito: R$ ${df.format(Pagamento.despesasCartao.valorDespesasCartaoDebito).replace(".", ",")}\n\n" +
                    "     Caixa Final: R$ ${df.format(total).replace(".", ",")}\n\n\n\n"
            ModulePrinter(this).printText(str)
        }
    }
}