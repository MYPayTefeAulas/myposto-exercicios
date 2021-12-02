package br.uea.transirie.mypay.mypaytemplate.ui.servicos

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.adapters.ServicoAdapter
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityTabelaServicosBinding
import br.uea.transirie.mypay.mypaytemplate.model.Servico
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.utils.getNewColor
import br.uea.transirie.mypay.mypaytemplate.viewmodel.TabelaServicosViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class TabelaServicosActivity : AppCompatActivity() {
    private val logTag = "TABELA_SERVICOS"
    private val myContext = this@TabelaServicosActivity
    private val binding by lazy { ActivityTabelaServicosBinding.inflate(layoutInflater) }
    private lateinit var viewModel: TabelaServicosViewModel
    private val myAdapter =
            ServicoAdapter(myContext, mutableListOf(), ::onEditClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(R.string.tabela_servicos)

        setTopAppBarEvents()

        with (binding.editServicosRvListaServicos) {
            layoutManager = LinearLayoutManager(myContext)
            adapter = myAdapter
        }

        val colorDrawable = ColorDrawable(getNewColor(myContext))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

        binding.floatingActionButton.setOnClickListener {
            Log.i(logTag, "Adição!")

            startForResult.launch(Intent(myContext, CadastrarServicoActivity::class.java))

            closeQuery()
        }
    }

    /**
     * Esta var é utilizada para exibir um Toast informando
     * o sucesso na operação recém-feita na tabela de serviços.
     */
    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                val intent = result.data
                val servicoOp = intent?.getStringExtra(getString(R.string.EXTRA_SERVICO_OP))
                Log.d(logTag,"Operação $servicoOp finalizada nos serviços.")

                val toastMsg = when (servicoOp) {
                    getString(R.string.SERVICO_OP_CADASTRO) -> "Serviço cadastrado com sucesso!"
                    getString(R.string.SERVICO_OP_EDITAR) -> "Serviço editado com sucesso!"
                    getString(R.string.SERVICO_OP_DELETAR) -> "Serviço deletado com sucesso!"
                    else -> ""
                }

                if (toastMsg.isNotBlank())
                    Toast.makeText(myContext, toastMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        doAsync {
            viewModel = TabelaServicosViewModel(AppDatabase.getDatabase(myContext))
            updateListaServico()
        }
    }

    private fun onEditClick(servico: Servico) {
        Log.i(logTag, "Edição!")

        val intent = Intent(myContext, EditarServicoActivity::class.java)
                .putExtra(getString(R.string.extra_editar_servico_id), servico.id)

        startForResult.launch(intent)
    }

    private fun updateListaServico() {
        doAsync {
            val listaServicos = viewModel.getAllServicos()
            uiThread { myAdapter.swapData(listaServicos) }
        }
    }

    private fun onDeleteClick(servico: Servico){
        val builder: AlertDialog.Builder = AlertDialog.Builder(myContext, R.style.AlertDialogTheme)
        builder.setTitle("ATENÇÃO!")
        builder.setMessage("Desativar este item?")
        builder.setPositiveButton("OK") { dialog, _ ->
            doAsync {
                viewModel.removerServico(servico)
                uiThread { updateListaServico() }
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun setTopAppBarEvents() {
        with (binding.tabelaServicosTopAppBar) {
            setNavigationOnClickListener {
                onBackPressed()
            }

            setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId) {
                    R.id.search_top_bar -> {
                        val searchView = menuItem?.actionView as SearchView
                        searchView.queryHint = getString(R.string.hint_pesquise_aqui)

                        val listener = object: SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(query: String?): Boolean {
                                query?.let {
                                    Log.i(logTag, "A query para pesquisa é: $query")

                                    val listaFiltrada = mutableListOf<Servico>()

                                    viewModel.getAllServicos().forEach {
                                        val locale = Locale.getDefault()
                                        val queryLowerCase = query.toString().lowercase(locale)
                                        val servicoLowerCase = it.descricao.lowercase(locale)

                                        if (servicoLowerCase.contains(queryLowerCase)) {
                                            listaFiltrada.add(it)

                                            val msg = "serviço $servicoLowerCase"
                                            Log.i(logTag, msg)
                                        }
                                    }

                                    myAdapter.swapData(listaFiltrada)
                                }

                                return true
                            }

                            override fun onQueryTextChange(newText: String?): Boolean =
                                onQueryTextSubmit(newText)
                        }

                        searchView.setOnQueryTextListener(listener)

                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun closeQuery() {
        binding.tabelaServicosTopAppBar.collapseActionView()
    }

}