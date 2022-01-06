package br.uea.transirie.mypay.mypaytemplate.ui.recuperar_pin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import br.uea.transirie.mypay.mypaytemplate.R

class SplashEmailRecuperacaoEnviadoActivity : AppCompatActivity() {
    val context = this@SplashEmailRecuperacaoEnviadoActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_email_recuperacao_enviado)

        supportActionBar?.hide()

        val email = intent.getStringExtra(getString(R.string.EXTRA_USUARIO_EMAIL)).toString()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(context, EscanearCodigoActivity::class.java)
                .putExtra(getString(R.string.EXTRA_USUARIO_EMAIL), email)
            startActivity(intent)

            finish()
        }, 2000)
    }

}