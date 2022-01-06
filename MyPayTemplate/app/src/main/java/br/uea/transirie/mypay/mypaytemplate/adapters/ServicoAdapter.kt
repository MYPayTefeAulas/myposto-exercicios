package br.uea.transirie.mypay.mypaytemplate.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.model.Servico
import br.uea.transirie.mypay.mypaytemplate.utils.toPrecoString
import kotlinx.android.synthetic.main.item_servico.view.*
import kotlin.reflect.KFunction1

class ServicoAdapter(
    private val context: Context,
    private val listaServicos: MutableList<Servico>,
    private val itemClickCallback: KFunction1<Servico, Unit>
) : RecyclerView.Adapter<ServicoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
            val txtItemNome: TextView = view.tv_nome_servico
            val txtPreco: TextView = view.tv_preco_servico
            val btnEditar: ImageView = view.btn_edit
    }

    fun swapData(novaListaServicos: List<Servico>) {
        listaServicos.clear()
        listaServicos.addAll(novaListaServicos)
        notifyDataSetChanged()
    }
  
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_servico, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val servico = listaServicos[position]

        val tvNome = servico.descricao
        holder.txtItemNome.text = tvNome

        val tvPreco = servico.preco.toPrecoString()
        holder.txtPreco.text = tvPreco

        holder.btnEditar.setOnClickListener {
            itemClickCallback(servico)
        }
    }

    override fun getItemCount(): Int = listaServicos.size

}