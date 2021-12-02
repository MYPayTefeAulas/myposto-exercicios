package br.uea.transirie.mypay.mypaytemplate.ui.cadastro

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityGerarPinBinding
import br.uea.transirie.mypay.mypaytemplate.model.Estabelecimento
import br.uea.transirie.mypay.mypaytemplate.model.Usuario
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.utils.MyPinGenerator
import br.uea.transirie.mypay.mypaytemplate.utils.getNewColor
import br.uea.transirie.mypay.mypaytemplate.viewmodel.CadastroEstabelecimentoEUsuarioViewModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class GerarPinActivity : AppCompatActivity() {
    private lateinit var viewModel: CadastroEstabelecimentoEUsuarioViewModel
    private val myContext = this
    private var estabelecimento: Estabelecimento? = null
    private lateinit var userPrefs:SharedPreferences
    private var usuario: Usuario? = null
    private var alterandoPin = false
    private var primeiroUso:Boolean = true
    private var cnpjEstabelecimento:String? = null
    private val binding by lazy { ActivityGerarPinBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val colorDrawable = ColorDrawable(getNewColor(myContext))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

        /** Recupera o Estabelecimento e Usuário recém-cadastrados nas telas anteriores **/
        estabelecimento = intent.getParcelableExtra(getString(R.string.EXTRA_ESTABELECIMENTO))
        usuario = intent.getParcelableExtra(getString(R.string.EXTRA_USUARIO))
        alterandoPin = intent.getBooleanExtra(getString(R.string.EXTRA_ALTERANDO_PIN), false)

        userPrefs = getSharedPreferences(getString(R.string.PREF_USER_DATA), Context.MODE_PRIVATE)
        cnpjEstabelecimento = userPrefs.getString(getString(R.string.PREF_USER_ESTABELECIMENTO_CNPJ), "")
        preencherTela()

        /** Inicializa o viewmodel para cadastrar localmente no fim */
        viewModel = CadastroEstabelecimentoEUsuarioViewModel(AppDatabase.getDatabase(myContext))

        binding.btGerarPin.setOnClickListener {
            doAsync {
                val pin = MyPinGenerator(AppDatabase.getDatabase(myContext)).newPin()
                uiThread {
                    usuario!!.pin = pin
                    binding.viewPreGeracaoPin.visibility = View.INVISIBLE

                    startActivity(Intent(this@GerarPinActivity, TelaCarregamentoActivity::class.java))

                    val pinString = pin.toString()
                    binding.txtPinNumero1.text = pinString[0].toString()
                    binding.txtPinNumero2.text = pinString[1].toString()
                    binding.txtPinNumero3.text = pinString[2].toString()
                    binding.txtPinNumero4.text = pinString[3].toString()
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.viewPosGeracaoPin.visibility = View.VISIBLE
                    }, 2000L)
                }
            }
        }

        binding.btConcluirPin.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.memorize_seu_pin))
            builder.setMessage(getString(R.string.memorize_seu_pin_txt2))
            builder.setPositiveButton(getString(R.string.continuar)) { _, _ ->
                verificarUsuario()
                val intent = Intent(myContext, SplashCadastroConcluidoActivity::class.java)
                    .putExtra(getString(R.string.EXTRA_PRIMEIRO_USO), primeiroUso)
                    .putExtra(getString(R.string.EXTRA_USUARIO), usuario)
                    .putExtra(getString(R.string.EXTRA_ALTERANDO_PIN), alterandoPin)
                startActivity(intent)
            }
            builder.setNegativeButton(getString(R.string.voltar)) { dialog, _ -> dialog.cancel() }
            builder.show()
        }
    }
    private fun verificarUsuario(){
        if (alterandoPin){
            doAsync {
                viewModel.updateUsuario(usuario!!)
                uiThread {
                    val userPrefs = getSharedPreferences(getString(R.string.PREF_USER_DATA), Context.MODE_PRIVATE)
                    val editor = userPrefs.edit()
                    editor.putInt(getString(R.string.PREF_PIN), usuario!!.pin)
                    editor.apply()
                }
            }
        }else{
            createEstabelecimentoEUsuario()
        }
    }
    private fun createEstabelecimentoEUsuario(){
        if (cnpjEstabelecimento.isNullOrEmpty()){
            doAsync {
                viewModel.saveEstabelecimento(estabelecimento!!)
                viewModel.saveUsuario(usuario!!)
                uiThread {
                    val edit = userPrefs.edit()
                    edit.putString(getString(R.string.PREF_USER_ESTABELECIMENTO_CNPJ), estabelecimento?.cnpj)
                    edit.apply()
                }
            }
        }else{
            doAsync {
                viewModel.saveUsuario(usuario!!)
            }
        }
    }
    private fun preencherTela(){
        if (cnpjEstabelecimento!!.isNotEmpty()){
            binding.txtGerarPin.setText(getString(R.string.gerar_pin_colbaorador))
            primeiroUso = false
        }
    }
}