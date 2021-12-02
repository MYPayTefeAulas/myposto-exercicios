package br.uea.transirie.mypay.mypaytemplate.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.model.Servico
import br.uea.transirie.mypay.mypaytemplate.utils.toPrecoString
import kotlinx.android.synthetic.main.item_resumo_saida.view.*

class ResumoSaidaAdapter(
    private val context: Context,
    private val listaServicos: MutableList<Servico>,
): RecyclerView.Adapter<ResumoSaidaAdapter.ViewHolder>() {

    class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val tvNome: TextView = item.resumoSaida_tvNome
        val tvPreco: TextView = item.resumoSaida_tvPreco
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.item_resumo_saida, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val servico = listaServicos[position]

        val tvNome = servico.descricao
        val tvPreco = servico.preco.toPrecoString()

        holder.tvNome.text = tvNome
        holder.tvPreco.text = tvPreco
    }

    override fun getItemCount(): Int = listaServicos.size

    fun swapData(novaLista: List<Servico>) {
        listaServicos.clear()
        listaServicos.addAll(novaLista)
        notifyDataSetChanged()
    }
}