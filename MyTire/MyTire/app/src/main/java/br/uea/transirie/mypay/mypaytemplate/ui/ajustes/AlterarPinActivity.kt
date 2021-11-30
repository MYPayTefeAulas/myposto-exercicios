package br.uea.transirie.mypay.mypaytemplate.ui.ajustes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityAlterarPinBinding
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.ui.cadastro.GerarPinActivity
import br.uea.transirie.mypay.mypaytemplate.utils.confPinHasErrors
import br.uea.transirie.mypay.mypaytemplate.utils.senhaHasErrors
import br.uea.transirie.mypay.mypaytemplate.viewmodel.EstabelecimentoUsuarioViewModel
import org.jetbrains.anko.doAsync

class AlterarPinActivity : AppCompatActivity() {
    private lateinit var viewModel: EstabelecimentoUsuarioViewModel
    private lateinit var userPrefs: SharedPreferences
    private var pinUsuario: Int? = null
    private val context = this
    private val binding by lazy { ActivityAlterarPinBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Alterar seu PIN"

        userPrefs = getSharedPreferences(getString(R.string.PREF_USER_DATA), Context.MODE_PRIVATE)
        pinUsuario = userPrefs.getInt(getString(R.string.PREF_PIN), 0)

        binding.alterarPinBtAvancar.setOnClickListener {
            checkAndAdvance()
        }
    }
    private fun checkAndAdvance(){
        val isPinAtualCorrect = !senhaHasErrors(binding.alterarPinTiPin, context)
        val isPinAtualConfCorrect = !confPinHasErrors(binding.alterarPinTiConfirmePin, binding.alterarPinTiPin, context)

        if (isPinAtualConfCorrect && isPinAtualCorrect ){
            if (binding.alterarPinEtPin.text.toString().toInt()*100 == pinUsuario){
                doAsync {
                    viewModel = EstabelecimentoUsuarioViewModel(AppDatabase.getDatabase(context))
                    val usuario = viewModel.usuarioByPin(pinUsuario!!)
                    val intent = Intent(context, GerarPinActivity::class.java).putExtra(getString(R.string.EXTRA_USUARIO), usuario)
                        .putExtra(getString(R.string.EXTRA_ALTERANDO_PIN), true)
                    startActivity(intent)
                }
            }else{
                binding.alterarPinTiPin.error = "PIN incorreto"
                binding.alterarPinTiConfirmePin.error = "PIN incorreto"
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}