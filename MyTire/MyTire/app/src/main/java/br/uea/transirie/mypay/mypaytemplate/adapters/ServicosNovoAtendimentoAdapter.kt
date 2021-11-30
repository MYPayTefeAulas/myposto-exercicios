package br.uea.transirie.mypay.mypaytemplate.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.model.Servico
import br.uea.transirie.mypay.mypaytemplate.utils.toPrecoString
import kotlinx.android.synthetic.main.item_novo_atendimento_servicos.view.*

class ServicosNovoAtendimentoAdapter(
    private val context: Context,
    private val listaAtivos: MutableList<Servico>,
    private val listaEscolhidos: MutableList<Servico>,
    private val checkboxCallback: ((Servico, Boolean) ->  Unit)
) : RecyclerView.Adapter<ServicosNovoAtendimentoAdapter.ViewHolder>() {

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val checkbox: CheckBox = view.checkBox
        val tvNome: TextView = view.tvNome
        val tvPreco: TextView = view.tvPreco
    }

    fun swapData(novaListaAtivos: List<Servico>, novaListaEscolhidos: List<Servico>) {
        listaAtivos.clear()
        listaAtivos.addAll(novaListaAtivos)

        listaEscolhidos.clear()
        listaEscolhidos.addAll(novaListaEscolhidos)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_novo_atendimento_servicos, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val servico = listaAtivos[position]

        val nome = servico.descricao
        holder.tvNome.text = nome

        val preco = servico.preco.toPrecoString()
        holder.tvPreco.text = preco

        holder.checkbox.isChecked = listaEscolhidos.contains(servico)

        holder.checkbox.setOnClickListener {
            checkboxCallback(servico, holder.checkbox.isChecked)
        }
    }

    override fun getItemCount(): Int = listaAtivos.size
}