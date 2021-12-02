package br.uea.transirie.mypay.mypaytemplate.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.adapters.ItemHistoricoAdapter
import br.uea.transirie.mypay.mypaytemplate.model.PagamentoClienteEVeiculo
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.ui.historico.DetalhesAtendimentoActivity
import br.uea.transirie.mypay.mypaytemplate.viewmodel.HistoricoAtendimentosViewModel
import kotlinx.android.synthetic.main.fragment_historico_atendimentos.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class HistoricoAtendimentosFragment : Fragment() {
    private val classTag: String = "HISTORICO_ATENDIMENTO"
    private lateinit var viewModelHistorico: HistoricoAtendimentosViewModel
    private lateinit var listaPCVs: List<PagamentoClienteEVeiculo>
    private lateinit var historicoAdapter: ItemHistoricoAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_historico_atendimentos,
            container,
            false)

        val listaHistoricoAtendimentos = view!!.findViewById<RecyclerView>(
            R.id.histAtendimentos_rvListaAtendimentos)

        historicoAdapter = ItemHistoricoAdapter(context, mutableListOf(), ::onResumoClick)
        listaHistoricoAtendimentos.adapter = historicoAdapter
        listaHistoricoAtendimentos.layoutManager = LinearLayoutManager(context)

        doAsync {
            context?.let { AppDatabase.getDatabase(it) }?.let {
                viewModelHistorico = HistoricoAtendimentosViewModel(it)
            }

            uiThread {
                listaPCVs = viewModelHistorico.getPagamentosClientesEVeiculos()
                if (listaPCVs.isNotEmpty()) {
                    hisAtendimento_tvNoData?.visibility = View.GONE
                } else {
                    hisAtendimento_tvNoData?.visibility = View.VISIBLE
                }

                historicoAdapter.swapData(listaPCVs)
            }
        }

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        managerTopBarEvents()
    }

    private fun onResumoClick(idVeiculo: Long){
        val intent = Intent(activity, DetalhesAtendimentoActivity::class.java)
                .putExtra("VEICULO_ID", idVeiculo)
        startActivity(intent)
    }

    private fun managerTopBarEvents(){
        histAtendimento_tab.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search_top_bar -> {
                    // Handle search icon press
                    val searchView = menuItem?.actionView as SearchView
                    searchView.queryHint = getString(R.string.hint_pesquise_aqui)

                    val listener = object: SearchView.OnQueryTextListener {
                        override fun onQueryTextChange(newText: String?): Boolean =
                                onQueryTextSubmit(newText)

                        override fun onQueryTextSubmit(query: String?): Boolean {
                            Log.i(classTag, "A query para pesquisa é: $query")

                            val listaFiltrada = mutableListOf<PagamentoClienteEVeiculo>()

                            listaPCVs.forEach {
                                val locale = Locale.getDefault()
                                val queryLowerCase = query.toString().toLowerCase(locale)
                                val placaLowerCase = it.cev.veiculo.placa.toLowerCase(locale)
                                val telefoneLowerCase = it.cev.cliente.telefone

                                if (placaLowerCase.contains(queryLowerCase)) {
                                    listaFiltrada.add(it)

                                    Log.i(classTag, "atendimento $placaLowerCase")
                                } else if (telefoneLowerCase.contains(queryLowerCase) && it.cev.veiculo.categoria == "Bicicleta") {
                                    listaFiltrada.add(it)

                                    Log.i(classTag, "atendimento $telefoneLowerCase (Bicicleta)")
                                }
                            }

                            historicoAdapter.swapData(listaFiltrada)

                            return true
                        }

                    }

                    searchView.setOnQueryTextListener(listener)

                    true
                }
                else -> false
            }
        }
    }
}