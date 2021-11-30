package br.uea.transirie.mypay.mypaytemplate.ui.cadastro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityTelaCarregamentoBinding

class TelaCarregamentoActivity : AppCompatActivity() {
    private val myContext = this
    private val binding by lazy { ActivityTelaCarregamentoBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar!!.hide()

        binding.gerandoPinIvRoda.animation =
            AnimationUtils.loadAnimation(myContext, R.anim.rotate_carregamento)

        /** Conta 2s antes de passar a próxima tela **/
        val tempo = 2000L
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, tempo)
    }
}