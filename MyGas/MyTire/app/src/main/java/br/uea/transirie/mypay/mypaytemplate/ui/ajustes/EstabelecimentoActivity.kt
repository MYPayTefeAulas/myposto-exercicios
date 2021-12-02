package br.uea.transirie.mypay.mypaytemplate.ui.ajustes

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityEstabelecimentoBinding
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityVisualizarColaboradorBinding
import br.uea.transirie.mypay.mypaytemplate.model.Estabelecimento
import br.uea.transirie.mypay.mypaytemplate.model.Usuario
import br.uea.transirie.mypay.mypaytemplate.ui.primeiroUso.SplashActivity
import br.uea.transirie.mypay.mypaytemplate.utils.AppPreferences
import br.uea.transirie.mypay.mypaytemplate.viewmodel.EstabelecimentoUsuarioViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EstabelecimentoActivity : AppCompatActivity() {
    private lateinit var viewModel: EstabelecimentoUsuarioViewModel
    private val context = this
    var usuario: Usuario? = null
    var estab: Estabelecimento? = null
    private val binding by lazy { ActivityEstabelecimentoBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setAppTopBarEvents()
        usuario = intent.getParcelableExtra(getString(R.string.EXTRA_USUARIO))
        estab = intent.getParcelableExtra(getString(R.string.EXTRA_ESTABELECIMENTO))
        val nomeCompleto = usuario!!.nome.split(" ", limit = 2)

        binding.txtEmpresaColaborador.text = estab!!.nomeFantasia
        binding.txtNomeEstab.text = estab!!.nomeFantasia
        binding.txtCnpjEstab.text = estab!!.cnpj
        binding.txtCargoColaborador.text = getString(R.string.gerente)
        binding.txtEmailColaborador.text = usuario!!.email
        binding.txtNomeColaborador.text = nomeCompleto[0]
        binding.txtSobrenomeColaborador.text = nomeCompleto[1]
        binding.txtTelefoneColaborador.text = usuario!!.telefone

        binding.btDeletarColaborador.setOnClickListener {
            val builder: androidx.appcompat.app.AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setMessage("Ao deletar sua conta você estará apagando todos os dados referentes a ela. Tem certeza que quer continuar?")
            builder.setPositiveButton("DELETAR"){dialog,_->
                doAsync {
                    viewModel.deleteUsuario(usuario!!)
                    uiThread {
                        val sharedPreferences = getSharedPreferences(
                            getString(R.string.PREF_USER_DATA),
                            Context.MODE_PRIVATE
                        )
                        val editor = sharedPreferences.edit()
                        editor.putBoolean(getString(R.string.PREF_USER_LOGADO), false)
                        editor.apply()

                        finishAffinity()

                        startActivity(Intent(context, SplashActivity::class.java))
                        Toast.makeText(context, "Conta deletada com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            builder.setNegativeButton("CANCELAR"){dialog,_-> dialog.cancel()}
            builder.show()
        }
    }
    private fun setAppTopBarEvents(){
        binding.listaColabTopAppBar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.listaColabTopAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.editar_dados ->{
                    startActivity(Intent(context, EditarEstabelecimentoActivity::class.java)
                        .putExtra(getString(R.string.EXTRA_USUARIO), usuario)
                        .putExtra(getString(R.string.EXTRA_ESTABELECIMENTO), estab))
                    true
                }
                else -> false
            }
        }
    }
}