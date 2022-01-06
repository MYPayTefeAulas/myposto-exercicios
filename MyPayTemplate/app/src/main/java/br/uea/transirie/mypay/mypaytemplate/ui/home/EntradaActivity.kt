package br.uea.transirie.mypay.mypaytemplate.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityEntradaBinding
import br.uea.transirie.mypay.mypaytemplate.ui.cadastro.CadastroPassoUnicoActivity
import br.uea.transirie.mypay.mypaytemplate.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_entrada.*

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

    private fun preencherTela(){
        val cnpjEstabelecimento = AppPreferences.getCNPJLogado(myContext)
        val desconhecido = getString(R.string.cnpj_desconhecido)

        if (cnpjEstabelecimento == desconhecido) {
            with (binding) {
                entrada_btnLoginCad.visibility = View.GONE
                entradaBtnCadastro.visibility = View.VISIBLE
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