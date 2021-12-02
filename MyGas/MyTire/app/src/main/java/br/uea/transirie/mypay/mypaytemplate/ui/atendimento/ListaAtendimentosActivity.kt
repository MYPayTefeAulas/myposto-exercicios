package br.uea.transirie.mypay.mypaytemplate.ui.atendimento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.adapters.ListaAtendimentosAdapter
import br.uea.transirie.mypay.mypaytemplate.model.AtendimentoClienteEVeiculo
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.viewmodel.ListaAtendimentosViewModel
import kotlinx.android.synthetic.main.activity_lista_atendimentos.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class ListaAtendimentosActivity : AppCompatActivity() {

    private var listaACVs = listOf<AtendimentoClienteEVeiculo>()
    private lateinit var viewModel: ListaAtendimentosViewModel
    private val myContext = this@ListaAtendimentosActivity
    private val myAdapter: ListaAtendimentosAdapter =
            ListaAtendimentosAdapter(myContext, mutableListOf(), ::onPlacaClick)
    private val classTag = "LISTA_ATENDIMENTO"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_atendimentos)

        title = getString(R.string.saida)

        setAppTopBarEvents()

        listaAtendimentos_rv.layoutManager = LinearLayoutManager(myContext)
        listaAtendimentos_rv.adapter = myAdapter

        doAsync {
            viewModel = ListaAtendimentosViewModel(AppDatabase.getDatabase(myContext))
            listaACVs = viewModel.getListaAtendimentosClientesEVeiculos()

            uiThread {
                if (listaACVs.isEmpty()) {
                    listaAtendimento_tvNoData.visibility = View.VISIBLE
                } else {
                    listaAtendimento_tvNoData.visibility = View.GONE
                }

                myAdapter.swapData(listaACVs)
            }
        }
    }

    private fun onPlacaClick(atend: AtendimentoClienteEVeiculo) {
        val intent = Intent(this, ResumoSaidaActivity::class.java)
            .putExtra("EXTRA_ATENDIMENTO_ID", atend.atendimentoId)

        startActivity(intent)
    }

    private fun setAppTopBarEvents() {
        listaAtend_topAppBar.setNavigationOnClickListener {
            onBackPressed()
        }

        listaAtend_topAppBar.setOnMenuItemClickListener { menuItem ->
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

                            val listaFiltrada = mutableListOf<AtendimentoClienteEVeiculo>()

                            listaACVs.forEach {
                                val locale = Locale.getDefault()
                                val queryLowerCase = query.toString().toLowerCase(locale)
                                val placaLowerCase = it.cev.veiculo.placa.toLowerCase(locale)
                                val telefoneLowerCase = it.cev.cliente.telefone

                                if (placaLowerCase.contains(queryLowerCase) ||
                                        telefoneLowerCase.contains(queryLowerCase)) {
                                    listaFiltrada.add(it)

                                    val msg = "atendimento $placaLowerCase | $telefoneLowerCase"
                                    Log.i(classTag, msg)
                                }
                            }

                            myAdapter.swapData(listaFiltrada)

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