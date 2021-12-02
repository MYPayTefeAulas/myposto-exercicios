package br.uea.transirie.mypay.mypaytemplate.ui.ajustes

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.adapters.CargoAdapter
import br.uea.transirie.mypay.mypaytemplate.adapters.ColaboradorAdapter
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityColaboradoresBinding
import br.uea.transirie.mypay.mypaytemplate.model.Usuario
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.utils.AppPreferences
import br.uea.transirie.mypay.mypaytemplate.viewmodel.UsuariosViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ColaboradoresActivity : AppCompatActivity(), ColaboradorAdapter.OnItemClickListener {
    private val context = this@ColaboradoresActivity
    private val binding by lazy { ActivityColaboradoresBinding.inflate(layoutInflater) }
    private var listaItensColaboradores = mutableListOf<List<Usuario>>()
    private var ultimoUpload = ""
    private val cargosAdapter by lazy {
        CargoAdapter(listaItensColaboradores, context, context)
    }
    private lateinit var viewModel: UsuariosViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ultimoUpload = AppPreferences.getUltimoUpload(context)

        preencherTela()
        setAppTopBarEvents()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun preencherTela(){
        val strNuvem = "Atualizado na nuvem em: $ultimoUpload"
        binding.txtNuvemAttColab.text = strNuvem
        binding.itemCargosColaboradoresRecyclerView.adapter = cargosAdapter
        binding.itemCargosColaboradoresRecyclerView.layoutManager = LinearLayoutManager(context)

        doAsync {
            viewModel = UsuariosViewModel(AppDatabase.getDatabase(context))
            val colaboradores = viewModel.getAllUsuario()
            uiThread {
                listaItensColaboradores = mutableListOf()
                val listaCargos = colaboradores.distinctBy { it.isGerente }.map { it.isGerente }

                listaCargos.forEach { isGerente ->
                    listaItensColaboradores.add(colaboradores.filter { it.isGerente == isGerente })
                }

                listaItensColaboradores = listaItensColaboradores.map {
                    it.reversed().distinctBy {
                        it.nome
                    }.reversed()
                }.toMutableList()

                cargosAdapter.listaColaboradores = listaItensColaboradores
                cargosAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun itemClick(item: Usuario) {
        if (!item.isGerente){
            val intent = Intent(context, VisualizarColaboradorActivity::class.java).putExtra(getString(R.string.EXTRA_USUARIO), item)
            startActivity(intent)
        }
    }

    private fun setAppTopBarEvents(){
        binding.listaColabTopAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.listaColabTopAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.add_colaborador ->{
                    startActivity(Intent(context, AdicionarColaboradorActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}