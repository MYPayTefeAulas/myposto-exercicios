package br.uea.transirie.mypay.mypaytemplate.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.model.PagamentoClienteEVeiculo
import br.uea.transirie.mypay.mypaytemplate.utils.toPrecoString
import kotlinx.android.synthetic.main.item_historico_atendimento.view.*

class ItemHistoricoAdapter(
    private val context: Context?,
    private val adapterList: MutableList<PagamentoClienteEVeiculo>,
    private val itemClickCallBack: ((Long) -> Unit)
) : RecyclerView.Adapter<ItemHistoricoAdapter.ViewHolder>(){

    //Items de visualização de dados para a lista do historico
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtPlaca: TextView = view.tv_Placa
        val txtSubTotal: TextView = view.tv_SubTotal
        val txtData: TextView = view.tv_Data
        val itemAtendimento: ConstraintLayout = view.itemHist_clVeiculo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.item_historico_atendimento, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pcv = adapterList[position] // objeto conjunto de pagamento, cliente e veiculo

        val identificador = if (pcv.cev.veiculo.categoria == "Bicicleta") {
            val separator = " / "
            pcv.cev.veiculo.placa + separator + pcv.cev.cliente.telefone
        } else {
            pcv.cev.veiculo.placa
        }
        // preenche o campo com placa (e telefone também, no caso da bicicleta)
        holder.txtPlaca.text = identificador

        // transforma o preco total de float para string
        val total = pcv.pagamento.valor.toPrecoString()
        // preenche o campo com o preço total
        holder.txtSubTotal.text = total

        // cria uma string com a data do pagamento
        val data = "Data: " + pcv.pagamento.dataPagamento
        // preenche o campo com a data
        holder.txtData.text = data

        // define o evento de click para esse item no recyclerview
        holder.itemAtendimento.setOnClickListener {
            itemClickCallBack(pcv.cev.veiculo.id)
        }
    }

    override fun getItemCount(): Int  = adapterList.size

    //Atualização da lista conjunta de pagamentos, clientes e veiculos
    fun swapData(newList: List<PagamentoClienteEVeiculo>){
        adapterList.clear()
        adapterList.addAll(newList)
        notifyDataSetChanged()
    }
}