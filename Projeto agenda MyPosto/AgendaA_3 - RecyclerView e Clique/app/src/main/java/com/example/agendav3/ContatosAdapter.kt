package com.example.agendav3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.agendav3.databinding.ItemContatoBinding

class ContatosAdapter (
    val listaContatos: MutableList<Contato>,
    val onBtEditarClick: (Int) -> Unit
    ) : RecyclerView.Adapter<ContatosAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemContatoBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemContatoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
    }

    override fun getItemCount(): Int {
        return listaContatos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.txtNome.text = listaContatos[position].nome
        holder.binding.txtTelefone.text = listaContatos[position].telefone

        holder.binding.btEditContato.setOnClickListener {
            onBtEditarClick(position)
        }
    }

    fun swapData(novaLisaContatos: List<Contato>){
        listaContatos.clear()
        listaContatos.addAll(novaLisaContatos)
        notifyDataSetChanged()
    }

}