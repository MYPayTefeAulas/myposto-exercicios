package br.uea.transirie.mypay.mypaytemplate.ui.primeiroUso

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.ui.caixa.SplashPrimeiroUsoActivity
import br.uea.transirie.mypay.mypaytemplate.ui.home.EntradaActivity
import br.uea.transirie.mypay.mypaytemplate.utils.AppPreferences
import br.uea.transirie.mypay.mypaytemplate.utils.work_manager.UpdateWorker
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    private val tag = "SplashActivity"
    private val myContext = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val userPrefs =
            getSharedPreferences(getString(R.string.PREF_USER_DATA), Context.MODE_PRIVATE)
        val ultimoUpload = AppPreferences.getUltimoUpload(myContext)
        Log.d(tag, "Último upload: $ultimoUpload")

        val uploadWorkRequest: WorkRequest = PeriodicWorkRequestBuilder<UpdateWorker>(
            20L, TimeUnit.MINUTES
        ).build()

        WorkManager
            .getInstance(myContext)
            .enqueue(uploadWorkRequest)

        Log.d(tag, "WorkManager inicializado!")

        val temUsuarioLogado =
            userPrefs.getBoolean(getString(R.string.PREF_USER_LOGADO), false)

        val nextActivity = if (temUsuarioLogado) {
            Log.i(tag, "Há um usuário logado.")
            Intent(this, SplashPrimeiroUsoActivity::class.java)
        } else {
            Log.i(tag, "Não há um usuário logado.")
            Intent(this, EntradaActivity::class.java)
        }

        val splashDuration = 3000L
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(nextActivity)
            finish()
        }, splashDuration)

    }
}