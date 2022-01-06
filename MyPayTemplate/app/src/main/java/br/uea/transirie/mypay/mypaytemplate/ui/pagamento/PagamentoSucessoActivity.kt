package br.uea.transirie.mypay.mypaytemplate.ui.pagamento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.ui.caixa.SplashPrimeiroUsoActivity

class PagamentoSucessoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagamento_sucesso)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, SplashPrimeiroUsoActivity::class.java)
            finishAffinity()
            startActivity(intent)
        }, 1500)

    }
}