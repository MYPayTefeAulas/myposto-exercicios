package br.uea.transirie.mypay.mypaytemplate.ui.abastecimento

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityChecaguemDeBombasBinding
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.PREF_DATA_NAME
import br.uea.transirie.mypay.mypaytemplate.utils.MaskBrMonetaryValue
import br.uea.transirie.mypay.mypaytemplate.utils.arredondaCentavos
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_checaguem_de_bombas.*
import java.text.DecimalFormat

class ChecaguemDeBombasActivity : AppCompatActivity() {
    private var df = DecimalFormat("0.0")
    private var quantidadeNoTanque = 0f
    private lateinit var usuario:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checaguem_de_bombas)
        configuraCampoQuantidade(txtMedida)
//        toolbar15D.setNavigationOnClickListener { finish() }
        val preference = getSharedPreferences(PREF_DATA_NAME, MODE_PRIVATE)
        usuario = preference.getString("nome_usuario", "").toString()

        val quantidadeIserida = preference.getString("quantidadeNoTanque", "0").toString().toFloat()

        quantidadeNoTanque = quantidadeIserida * 1000

 //       txtMedida.text = "${df.format(quantidadeIserida).replace(".",",")}"
 //       txtL.text = "${df.format(quantidadeNoTanque).replace(".",",")}"

        var resultado = -1f

        btOK.setOnClickListener {

            LayoutInputQuantiaEmMetrosCubicos.error = null
            if (txtMedida.text.toString().isEmpty()){
                LayoutInputQuantiaEmMetrosCubicos.error = "Campo Obrigatório"
            }else {

                val medidaFeita = txtMedida.text.toString()
                    .replace(".","")
                    .replace(",",".")
                    .toFloat()
                Log.d("PagamentoDinheiro", "Recebido: $medidaFeita")
                Log.d("PagamentoDinheiro", "Total: $quantidadeNoTanque")
                resultado = arredondaCentavos(medidaFeita * 1000)

                if (resultado < 0f) {
                    LayoutInputQuantiaEmMetrosCubicos.error = "Valor Inválido"
                } else {
                    val editor = preference.edit()
                    editor.putString("recebido", medidaFeita.toString())
                    editor.apply()
                    txtL.text = "${df.format(resultado).replace(".", ",")} L"
                }
            }
            Log.d("resultado", resultado.toString())
        }

    }

    @SuppressLint("SetTextI18n")

    private fun configuraCampoQuantidade(quatidade: TextInputEditText?) {
        quatidade?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && quatidade.text.toString().isEmpty()) {
                quatidade.setText("0,00")
            } else if (!hasFocus && quatidade.text.toString() == "0,00") {
                quatidade.setText("")
            }
        }
        quatidade?.addTextChangedListener(MaskBrMonetaryValue.mask(quatidade))
    }

}