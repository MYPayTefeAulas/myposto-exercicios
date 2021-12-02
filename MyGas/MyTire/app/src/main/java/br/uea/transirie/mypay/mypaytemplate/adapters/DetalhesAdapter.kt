package br.uea.transirie.mypay.mypaytemplate.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.model.Servico
import kotlinx.android.synthetic.main.item_detalhes_servico.view.*

class DetalhesAdapter(
        private val context: Context,
        private val servicosAtendimento: MutableList<Servico>
): RecyclerView.Adapter<DetalhesAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val txtServico: TextView = view.detalhes_itemServico
    }

    fun swapData(listaServicosAtendimentos: List<Servico>) {
        servicosAtendimento.clear()
        servicosAtendimento.addAll(listaServicosAtendimentos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
                R.layout.item_detalhes_servico, parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val servicos = servicosAtendimento[position]

        holder.txtServico.text = servicos.descricao
    }

    override fun getItemCount(): Int = servicosAtendimento.size

}