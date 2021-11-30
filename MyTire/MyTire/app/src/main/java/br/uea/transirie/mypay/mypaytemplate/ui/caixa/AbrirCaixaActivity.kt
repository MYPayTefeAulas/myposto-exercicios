package br.uea.transirie.mypay.mypaytemplate.ui.caixa

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityAbrirCaixaBinding
import br.uea.transirie.mypay.mypaytemplate.model.Pagamento
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.PREF_VERIF_ABRIR_CAIXA
import br.uea.transirie.mypay.mypaytemplate.ui.home.HomeGerenteActivity
import br.uea.transirie.mypay.mypaytemplate.utils.Mascara
import br.uea.transirie.mypay.mypaytemplate.utils.getNewColor
import com.google.gson.Gson
import java.time.LocalDate

class AbrirCaixaActivity : AppCompatActivity() {
    private lateinit var usuario:String
    private lateinit var preference: SharedPreferences
    private val binding by lazy { ActivityAbrirCaixaBinding.inflate(layoutInflater) }
    private val myContext = this@AbrirCaixaActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        preference = getSharedPreferences(PREF_VERIF_ABRIR_CAIXA, MODE_PRIVATE)
        usuario = preference.getString(getString(R.string.PREF_USER_ESTABELECIMENTO_CNPJ), "").toString()

        /* Exclusivo do MyTire: altera a cor do barra superior pra amarelo*/
        val colorDrawable = ColorDrawable(getNewColor(myContext))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

        binding.txtCaixaInicialAbrirCaixa.let { editText ->
            editText.doAfterTextChanged { text ->
                val newText = Mascara.formatMoney(text.toString())
                if (newText != text.toString()) {
                    editText.setText(newText)
                    editText.setSelection(editText.text.toString().lastIndex + 1)
                }
                binding.btAbrirCaixa.isEnabled = text.toString().isNotBlank()
            }
        }
        binding.btAbrirCaixa.setOnClickListener {
            if (binding.txtCaixaInicialAbrirCaixa.text.isNullOrBlank()) {
                binding.textInputLayoutCaixa.error = "Campo obrigatório"
            } else {
                val caixaInicial =
                    binding.txtCaixaInicialAbrirCaixa.text.toString().replace(",", ".").toFloat()
                val sharedEditor = preference.edit()
                sharedEditor.putString(
                    usuario,
                    Gson().toJson(
                        AbrirCaixaStatus(
                            status = true,
                            data = LocalDate.now(),
                            valor = caixaInicial
                        )
                    )
                )
                sharedEditor.apply()
                Pagamento.caixaInicial.valorCaixaInicial = caixaInicial
                startActivity(Intent(this, HomeGerenteActivity::class.java))
                finish()
            }
        }
    }
    // função para quando o foco dos editTexts mudarem, o teclado se recolher
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}