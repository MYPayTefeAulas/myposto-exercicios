package br.uea.transirie.mypay.mypaytemplate.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityEntradaBinding
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.ui.cadastro.CadastroPassoUnicoActivity
import br.uea.transirie.mypay.mypaytemplate.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_entrada.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EntradaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEntradaBinding
    private val myContext: Context = this@EntradaActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntradaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listeners()
    }

    override fun onResume() {
        super.onResume()
        preencherTela()
    }

    @SuppressLint("SetTextI18n")
    private fun preencherTela(){
        val userPrefs = getSharedPreferences(getString(R.string.PREF_USER_DATA), Context.MODE_PRIVATE)
        val cnpjEstabelecimento = userPrefs.getString(getString(R.string.PREF_USER_ESTABELECIMENTO_CNPJ), "")
        viewEstabelecimentoNaoCadastrado.visibility = View.INVISIBLE
        viewEstabelecimentoCadastrado.visibility = View.INVISIBLE

        if (cnpjEstabelecimento.isNullOrEmpty()){
            viewEstabelecimentoNaoCadastrado.visibility = View.VISIBLE
        }else{
            doAsync {
                val viewModel = LoginViewModel(AppDatabase.getDatabase(this@EntradaActivity))
                val estabelecimento = viewModel.estabelecimentoByCNPJ(cnpjEstabelecimento)
                uiThread {
                    binding.entradaTxtContaNome.text = "Entrar como ${estabelecimento!!.nomeFantasia}"
                    viewEstabelecimentoCadastrado.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun listeners(){
        /** realizar Cadastro **/
        entrada_btnCadastro.setOnClickListener {
            startActivity(Intent(myContext, CadastroPassoUnicoActivity::class.java))
        }

        /** realizar Login com cadastro local **/
        entrada_btnLoginCad.setOnClickListener {
            startActivity(Intent(myContext, LoginActivity::class.java))
        }
    }
}