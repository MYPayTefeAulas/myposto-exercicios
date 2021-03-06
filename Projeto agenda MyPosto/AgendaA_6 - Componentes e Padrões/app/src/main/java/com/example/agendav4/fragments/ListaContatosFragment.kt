package com.example.agendav4.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agendav4.*
import com.example.agendav4.databinding.FragmentListaContatosBinding
import com.example.agendav4.enums.TipoOrdenacao
import com.example.agendav4.utils.IntentsConstants
import com.example.agendav4.utils.PrefsConstants

class ListaContatosFragment: Fragment(), SearchView.OnQueryTextListener {
    private var _binding: FragmentListaContatosBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: ContatosAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaContatosBinding.inflate(inflater, container, false)

        adapter = ContatosAdapter(mutableListOf(), ::onBtEditarClick)

        binding.rvContatos.layoutManager = LinearLayoutManager(context)
        binding.rvContatos.adapter = adapter
        binding.rvContatos.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        carregaLista()

        initTopBar()

        return binding.root

    }

    private fun carregaLista() {
        val config = requireActivity().getSharedPreferences(PrefsConstants.FILE_CONFIGURACOES,0)

        val ordenacaoSelecionada_str = config.getString(
            PrefsConstants.KEY_TIPO_ORDENACAO_CONTATOS,
            TipoOrdenacao.ORDEM_INCERCAO.toString()
        )
        val ordenacaoSelecionada: TipoOrdenacao = TipoOrdenacao.valueOf(ordenacaoSelecionada_str!!)
        when (ordenacaoSelecionada) {
            TipoOrdenacao.ALFABEICA_AZ -> {
                val listaOrdenada = Agenda.listaContatos.sortedBy { it.nome }
                adapter.swapData(listaOrdenada)
            }
            TipoOrdenacao.ALFABETICA_ZA -> {
                val listaOrdenada = Agenda.listaContatos.sortedByDescending { it.nome }
                adapter.swapData(listaOrdenada)
            }
            TipoOrdenacao.ORDEM_INCERCAO -> {
                adapter.swapData(Agenda.listaContatos)
            }
        }
    }

    private fun initTopBar(){
        binding.toolbarContatos.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search_top_bar -> {
                    val searchView = menuItem?.actionView as SearchView
                    searchView.queryHint = "Digite para pesquisar"
                    searchView.setOnQueryTextListener(this)
                    true
                }
                else -> false
            }
        }

    }

    override fun onQueryTextChange(newText: String?): Boolean =
        onQueryTextSubmit(newText)

    override fun onQueryTextSubmit(query: String?): Boolean {
        val queryLowerCase = query.toString().lowercase()

        val listaFiltrada = Agenda.listaContatos.filter { contatoAtual ->
            contatoAtual.nome.lowercase().contains(queryLowerCase) ||
                    contatoAtual.telefone.lowercase().contains(queryLowerCase)
        }
        adapter.swapData(listaFiltrada)
        return true
    }

    override fun onResume() {
        super.onResume()
        adapter.swapData(Agenda.listaContatos)
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }



    fun onBtEditarClick(indiceLista: Int) {
        val intent = Intent(context, EditActivity::class.java)
        intent.putExtra(IntentsConstants.INT_INDICE_CONTATO, indiceLista)
        startActivity(intent)
    }

}