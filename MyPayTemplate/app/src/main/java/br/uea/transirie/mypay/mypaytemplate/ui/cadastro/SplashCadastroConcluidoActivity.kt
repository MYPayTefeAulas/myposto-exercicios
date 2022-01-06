package br.uea.transirie.mypay.mypaytemplate.ui.cadastro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivitySplashCadastroConcluidoBinding
import br.uea.transirie.mypay.mypaytemplate.ui.home.EntradaActivity
import br.uea.transirie.mypay.mypaytemplate.ui.home.HomeFuncionarioActivity
import br.uea.transirie.mypay.mypaytemplate.ui.home.HomeGerenteActivity

class SplashCadastroConcluidoActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashCadastroConcluidoBinding.inflate(layoutInflater) }
    private val context = this
    private var topbarTitle: String? = "Cadastro"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        topbarTitle = intent.getStringExtra(getString(R.string.EXTRA_TOPBAR_TITLE))
        val usuarioIsGerente =
            intent.getBooleanExtra(getString(R.string.EXTRA_USUARIO_IS_GERENTE), false)

        val nextActivity = when (topbarTitle) {
            getString(R.string.title_alterar_pin) -> {
                binding.splashCadConcluidoTvSucesso.text = getString(R.string.pin_atualizado_com_sucesso)
                binding.splashCadConcluidoTv2.visibility = View.VISIBLE

                if (usuarioIsGerente)
                    Intent(context, HomeGerenteActivity::class.java)
                else Intent(context, HomeFuncionarioActivity::class.java)
            }
            else -> Intent(context, EntradaActivity::class.java)
        }
        nextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        /** Conta 2s antes de passar a próxima tela **/
        val tempo = 2000L

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(nextActivity)
        }, tempo)
    }
}