package br.uea.transirie.mypay.mypaytemplate.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.model.AtendimentoClienteEVeiculo
import kotlinx.android.synthetic.main.item_saida_lista_atendimentos.view.*

class ListaAtendimentosAdapter(
    private val context: Context,
    private val adapterList: MutableList<AtendimentoClienteEVeiculo>,
    private val itemClickCallback: ((AtendimentoClienteEVeiculo) -> Unit)
) : RecyclerView.Adapter<ListaAtendimentosAdapter.ViewHolder>() {

    class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val clItem: ConstraintLayout = item.saidaAtend_clItem
        val tvPlaca: TextView = item.saidaAtend_tvPlaca
        //val tvTelefone: TextView = item.saidaAtend_tvTelefone
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.item_saida_lista_atendimentos, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val atend = adapterList[position]

        holder.clItem.setOnClickListener {
            itemClickCallback(atend)
        }
        holder.tvPlaca.text = atend.cev.veiculo.placa
        //holder.tvTelefone.text = atend.cev.cliente.telefone
    }

    override fun getItemCount(): Int = adapterList.size

    fun swapData(novaLista: List<AtendimentoClienteEVeiculo>) {
        adapterList.clear()
        adapterList.addAll(novaLista)
        notifyDataSetChanged()
    }

}