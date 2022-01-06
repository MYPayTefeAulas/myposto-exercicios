package br.uea.transirie.mypay.mypaytemplate.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.FragmentCaixaBinding
import br.uea.transirie.mypay.mypaytemplate.model.Pagamento
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.PREF_VERIF_ABRIR_CAIXA
import br.uea.transirie.mypay.mypaytemplate.ui.caixa.AbrirCaixaStatus
import br.uea.transirie.mypay.mypaytemplate.ui.caixa.FecharCaixaActivity
import br.uea.transirie.mypay.mypaytemplate.ui.caixa.HistoricoVendasActivity
import br.uea.transirie.mypay.mypaytemplate.ui.caixa.MovimentarCaixaActivity
import br.uea.transirie.mypay.mypaytemplate.utils.AppPreferences
import br.uea.transirie.mypay.mypaytemplate.viewmodel.CaixaViewModel
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.DecimalFormat
import java.time.LocalDate

class CaixaFragment : Fragment() {
    private lateinit var binding: FragmentCaixaBinding
    private lateinit var caixaViewModel: CaixaViewModel
    private lateinit var dataHoje: LocalDate
    private var df = DecimalFormat("00.00")
    private lateinit var estabelecimentoCNPJ:String
    private var preference: SharedPreferences? = null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCaixaBinding.inflate(inflater, container, false)
        dataHoje = LocalDate.now()
        estabelecimentoCNPJ = AppPreferences.getCNPJLogado(requireActivity())
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        listeners()
        preencherTela()
        calcularGanhosDespesas()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    private fun preencherTela(){
        var mes = dataHoje.monthValue.toString()
        if (mes.length < 2){
            mes = "0$mes"
        }
        binding.data.text = "${dataHoje.dayOfMonth}/$mes/${dataHoje.year}"
    }
    private fun listeners(){
        binding.btHistoricoVendas.setOnClickListener {
            startActivity(Intent(context,HistoricoVendasActivity::class.java))
        }
        binding.btMovimentarCaixa.setOnClickListener {
            startActivity(Intent(context,MovimentarCaixaActivity::class.java))
        }
        binding.btFecharCaixa.setOnClickListener {
            startActivity(Intent(context,FecharCaixaActivity::class.java))
        }
    }
    @SuppressLint("SetTextI18n")
    private fun calcularGanhosDespesas(){
        doAsync {
            caixaViewModel = context?.let { AppDatabase.getDatabase(it) }?.let { CaixaViewModel(it) }!!
            caixaViewModel.getRecebidosDia(LocalDate.now(),estabelecimentoCNPJ)
            caixaViewModel.getDespesasDia(LocalDate.now(),estabelecimentoCNPJ)

            uiThread {
                binding.recebimentosDinheiro.text = "R$ ${df.format(caixaViewModel.getPagamentoDinheiro()).replace(".",",")}"
                binding.recebimentosCartao.text = "R$ ${df.format(caixaViewModel.getPagamentoCartao()).replace(".",",")}"
                binding.despesasDinheiro.text = "R$ ${df.format(caixaViewModel.getDespesaDinheiro()).replace(".",",")}"
                binding.despesasCartao.text = "R$ ${df.format(caixaViewModel.getDespesaCartao()).replace(".",",")}"
                Pagamento.dataHoje.dataHoje = LocalDate.now()

                preference = context?.getSharedPreferences(PREF_VERIF_ABRIR_CAIXA, MODE_PRIVATE)
                preference?.let {
                    val statusAbrirCaixa= Gson().fromJson(it.getString(estabelecimentoCNPJ, Gson().toJson(
                        AbrirCaixaStatus(status = false, data = LocalDate.now(), valor =0f)
                    )),
                        AbrirCaixaStatus::class.java)
                    val valFormat=String.format("%.2f", statusAbrirCaixa.valor)
                    binding.txtCaixaInicial.text="R$ $valFormat"
                    binding.totalParcial.text = "R$ ${df.format(caixaViewModel.getTotal() + statusAbrirCaixa.valor).replace(".",",")}"
                    Pagamento.totalClientes.totalCliente = caixaViewModel.getClientes()
                    Pagamento.dinheiroRecebimento.valorDinheiro = caixaViewModel.getPagamentoDinheiro().toFloat()
                    Pagamento.creditoRecebimento.valorCredito = caixaViewModel.getPagamentoCredito().toFloat()
                    Pagamento.debitoRecebimento.valorDebito = caixaViewModel.getPagamentoDebito().toFloat()
                    Pagamento.despesasCartao.valorDespesasCartaoDebito = caixaViewModel.getDespesaDebito().toFloat()
                    Pagamento.despesasCartao.valorDespesasCartaoCredito = caixaViewModel.getDespesaCredito().toFloat()
                    Pagamento.despesasDinheiro.valorDespesasDinheiroDia = caixaViewModel.getDespesaDinheiro().toFloat()
                }
            }
        }
    }
}
