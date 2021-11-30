package br.uea.transirie.mypay.mypaytemplate.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.model.Usuario

class ColaboradorAdapter(
    private val colaboradores: List<Usuario>,
    private val listenerItem: OnItemClickListener
): RecyclerView.Adapter<ColaboradorAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.usuario_colaborador, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nomeColaborador.text = colaboradores[position].nome
        holder.cargoColaborador.text = when(colaboradores[position].isGerente){
            true -> "Gerente " + "${position + 1}"
            else -> "Funcionário " + "${position + 1}"

        }
        holder.setaVisualizarColab.visibility = when(colaboradores[position].isGerente){
            true -> View.INVISIBLE
            else -> View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return  colaboradores.size
    }

    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView),
            View.OnClickListener{
        val nomeColaborador: TextView = itemView.findViewById(R.id.usuarioNomeTxt)
        val cargoColaborador: TextView = itemView.findViewById(R.id.usuarioCargoTxt)
        val setaVisualizarColab: ImageView = itemView.findViewById(R.id.setaVisualizarColab)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                val item = colaboradores [position]
                listenerItem.itemClick(item)
            }
        }
    }
    interface OnItemClickListener{
        fun itemClick(item: Usuario)
    }
}