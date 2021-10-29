package com.example.agenda_4_menuinferiorcomfragmentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agenda_4_menuinferiorcomfragmentos.databinding.FragmentListasContatosBinding

class ListaContatosFragment: Fragment() {
    private var _binding: FragmentListasContatosBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: ContatosAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListasContatosBinding.inflate(inflater, container, false)

        adapter = ContatosAdapter(mutableListOf(), ::onBtEditarClick)

        binding.rvContatos.layoutManager = LinearLayoutManager(context)
        binding.rvContatos.adapter = adapter
        binding.rvContatos.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        inicializaLista()
        adapter.swapData(Agenda.listaContatos)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        adapter.swapData(Agenda.listaContatos)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun inicializaLista() {
        Agenda.listaContatos.addAll(
            listOf(
                Contato("1 Rodrigo", "1111", "rodrigo@uea.edu.br"),
                Contato("2 Willian", "2222", "wramos@uea.edu.br"),
                Contato("3 Thalia", "3333", "thalia@uea.edu.br"),

                )
        )
    }

    fun onBtEditarClick(indiceLista: Int) {
        val intent = Intent(context, EditarContatoActivity::class.java)
        intent.putExtra("indiceContato", indiceLista)
        startActivity(intent)
    }

}