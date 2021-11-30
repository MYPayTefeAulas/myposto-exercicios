package br.uea.transirie.mypay.mypaytemplate.ui.home

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityHomeFuncionarioBinding
import br.uea.transirie.mypay.mypaytemplate.ui.ajustes.AjustesFuncionarioActivity
import br.uea.transirie.mypay.mypaytemplate.ui.atendimento.ListaAtendimentosActivity
import br.uea.transirie.mypay.mypaytemplate.ui.atendimento.NovoAtendimentoActivity
import br.uea.transirie.mypay.mypaytemplate.utils.getNewColor

class HomeFuncionarioActivity : AppCompatActivity() {
    private val myContext = this@HomeFuncionarioActivity
    private val binding by lazy { ActivityHomeFuncionarioBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(R.string.app_name)

        val colorDrawable = ColorDrawable(getNewColor(myContext))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

        listeners()
    }
    private fun listeners(){
        binding.homeFBtnNovoAtendimento.setOnClickListener {
            startActivity(Intent(myContext, NovoAtendimentoActivity::class.java))
        }
        binding.homeFBtnConcluirServico.setOnClickListener {
            startActivity(Intent(myContext, ListaAtendimentosActivity::class.java))
        }
        binding.homeFBtnAjustes.setOnClickListener {
            startActivity(Intent(myContext, AjustesFuncionarioActivity::class.java))
        }
    }
}