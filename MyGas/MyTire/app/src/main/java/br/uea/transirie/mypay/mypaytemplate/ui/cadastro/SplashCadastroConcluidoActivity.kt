package br.uea.transirie.mypay.mypaytemplate.ui.cadastro

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivitySplashCadastroConcluidoBinding
import br.uea.transirie.mypay.mypaytemplate.model.Usuario
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.ui.ajustes.AjustesFuncionarioActivity
import br.uea.transirie.mypay.mypaytemplate.ui.ajustes.ColaboradoresActivity
import br.uea.transirie.mypay.mypaytemplate.ui.home.EntradaActivity
import br.uea.transirie.mypay.mypaytemplate.ui.home.HomeGerenteActivity
import br.uea.transirie.mypay.mypaytemplate.viewmodel.EstabelecimentoUsuarioViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class SplashCadastroConcluidoActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashCadastroConcluidoBinding.inflate(layoutInflater) }
    private lateinit var viewModel: EstabelecimentoUsuarioViewModel
    private var pinUsuario: Int? = null
    private val context = this
    private var usuario: Usuario? = null
    private var alterandoPin = false
    private lateinit var userPrefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        alterandoPin = intent.getBooleanExtra(getString(R.string.EXTRA_ALTERANDO_PIN), false)
        userPrefs = getSharedPreferences(getString(R.string.PREF_USER_DATA), Context.MODE_PRIVATE)
        pinUsuario = userPrefs.getInt(getString(R.string.PREF_PIN), 0)
        if (pinUsuario == 0){
            val usu: Usuario? = intent.getParcelableExtra(getString(R.string.EXTRA_USUARIO))
            pinUsuario = usu!!.pin
        }
        val primeiroUso = intent.getBooleanExtra(getString(R.string.EXTRA_PRIMEIRO_USO), true)

        doAsync {
            viewModel = EstabelecimentoUsuarioViewModel(AppDatabase.getDatabase(context))
            usuario = viewModel.usuarioByPin(pinUsuario!!)
            uiThread {
                var intent = Intent(context, EntradaActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                if (!primeiroUso && usuario!!.isGerente){
                    intent = if (alterandoPin){
                        binding.splashCadConcluidoTvSucesso. text = "PIN atualizado com sucesso!"
                        Intent(context, HomeGerenteActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    }else{
                        Intent(context, ColaboradoresActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    }
                }else if (!usuario!!.isGerente){
                    binding.splashCadConcluidoTvSucesso. text = "PIN atualizado com sucesso!"
                    intent = Intent(context, AjustesFuncionarioActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                /** Conta 2s antes de passar a próxima tela **/
                val tempo = 2000L

                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(intent)
                }, tempo)
            }
        }
    }
}