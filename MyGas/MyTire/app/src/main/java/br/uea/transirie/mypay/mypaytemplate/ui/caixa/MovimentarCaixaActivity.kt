package br.uea.transirie.mypay.mypaytemplate.ui.caixa

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityMovimentarCaixaBinding
import br.uea.transirie.mypay.mypaytemplate.model.Despesa
import br.uea.transirie.mypay.mypaytemplate.model.TipoPagamento
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.utils.MaskBrMonetaryValue
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.time.LocalDate

class MovimentarCaixaActivity : AppCompatActivity() {
    private lateinit var usuario:String
    private lateinit var userPrefs:SharedPreferences
    private lateinit var binding: ActivityMovimentarCaixaBinding
    private lateinit var tipoPagamento: TipoPagamento
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovimentarCaixaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        userPrefs = getSharedPreferences(getString(R.string.PREF_USER_DATA), Context.MODE_PRIVATE)
        usuario = userPrefs.getString(getString(R.string.PREF_USER_ESTABELECIMENTO_CNPJ),"").toString()

        configuraCampoPreco(binding.inputValor)
        preencherDropDown()
        listeners()
    }
    private fun preencherDropDown(){
        val lista = resources.getStringArray(R.array.ModoPagamento)
        val arrayAdapter = ArrayAdapter(this,R.layout.dropdown_model, lista)
        binding.autoCompleteTextView4.setAdapter(arrayAdapter)
    }
    @SuppressLint("SetTextI18n")
    private fun configuraCampoPreco(preco: TextInputEditText?) {
        preco?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && preco.text.toString().isEmpty()) {
                preco.setText("0,00")
            } else if (!hasFocus && preco.text.toString() == "0,00") {
                preco.setText("")
            }
        }
        preco?.addTextChangedListener(MaskBrMonetaryValue.mask(preco))
    }
    private fun inserirDespesas() {
        when (binding.autoCompleteTextView4.text.toString()) {
            "Despesa no Dinheiro" -> {
                tipoPagamento = TipoPagamento.DINHEIRO
            }
            "Despesa no Cartão de Crédito" -> {
                tipoPagamento = TipoPagamento.CREDITO
            }
            "Despesa no Cartão de Débito" -> {
                tipoPagamento = TipoPagamento.DEBITO
            }
        }
        val cadastro = Despesa(
            0,
            usuario,
            Gson().toJson(LocalDate.now()),
            binding.inputValor.text.toString().replace(".", "").replace(",", ".").toFloat(),
            tipoPagamento
        )
        val db = AppDatabase.getDatabase(this)
        doAsync {
            db.despesaDao().insert(cadastro)
            uiThread {
                val toast = Toast.makeText(
                    this@MovimentarCaixaActivity, "Despesa inserida com sucesso!",
                    Toast.LENGTH_SHORT
                )
                toast.setGravity(Gravity.BOTTOM, 0, 144)
                toast.show()
                startActivity(
                    Intent(
                        this@MovimentarCaixaActivity,
                        MovimentarCaixaActivity::class.java
                    )
                )
                finish()
            }
        }
    }
    private fun validDados():Boolean{
        var x = 0
        if (binding.inputValor.text.toString().isEmpty()){
            binding.textInputLayout5.error = "Campo obrigatório"
            x+=1
        }
        if (binding.autoCompleteTextView4.text.toString().isEmpty()){
            binding.LayoutInputParcelas.error = "Campo obrigatório"
            x+=1
        }
        if (x!=0){
            return false
        }
        return true
    }
    private fun listeners(){
        binding.toolbar7.setNavigationOnClickListener {
            finish()
        }
        binding.btAdicionarDespesa.setOnClickListener {
            if (validDados()){
                inserirDespesas()
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