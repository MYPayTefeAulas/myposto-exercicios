package br.uea.transirie.mypay.mypaytemplate.ui.abastecimento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityAbastecerGasolinaBinding
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.PREF_DATA_NAME
import br.uea.transirie.mypay.mypaytemplate.ui.pagamento.FormasPagamentoActivity
import br.uea.transirie.mypay.mypaytemplate.ui.pagamento.PagamentoDinheiroActivity
import br.uea.transirie.mypay.mypaytemplate.utils.arredondaCentavos
import br.uea.transirie.mypay.mypaytemplate.utils.toPrecoDouble
import br.uea.transirie.mypay.mypaytemplate.utils.toPrecoString
import kotlinx.android.synthetic.main.activity_abastecer_gasolina.*
import kotlinx.android.synthetic.main.activity_pagamento_cartao.*
import java.text.DecimalFormat

class AbastecerGasolinaActivity : AppCompatActivity() {

    private val context = this@AbastecerGasolinaActivity

    private var df = DecimalFormat("0.00")
    private var quantiaGasolina = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abastecer_gasolina)
        toolbarActivityGasolina.setNavigationOnClickListener { finish() }
        val preference = getSharedPreferences(PREF_DATA_NAME, MODE_PRIVATE)


        autoCompleteTextView4.setOnDismissListener {
            LayoutInputTipoDeCombustivel.error = null
            if (autoCompleteTextView4.text.toString() == ""){
                LayoutInputTipoDeCombustivel.error = "Seleção requerida"
            }else{
                val selecionarCombustiveis = autoCompleteTextView4.text.toString().split("")
                val valorCombustiveis = selecionarCombustiveis[0].toInt()
//                val resultadoTotalEmReais =
//                val resultadoTotalEmLitros =


                txtTotalLitrosValor.text = "R$"
            }
        }

        txtTotalLitrosValor.text = "R$ ${df.format(quantiaGasolina).replace(".",",")}"

        var totalGasolina = -1f

        btQuantiaOk.setOnClickListener {


            LayoutInputQuantiaGasolina.error = null
            if (txtQuantiaGasolina.text.toString().isEmpty()){
                LayoutInputQuantiaGasolina.error = "Campo Obrigatório"
            } else{

                val quantiaRecebida = txtQuantiaGasolina.text.toString()
                    .replace(".","")
                    .replace(",",".")
                    .toFloat()
                Log.d("AbastecerGasolina","Quantia: $quantiaRecebida")
                Log.d("AbastecerDiesel","Total: $quantiaGasolina")
                totalGasolina = arredondaCentavos(quantiaRecebida - quantiaGasolina)

                if (totalGasolina < 0f){
                    LayoutInputQuantiaGasolina.error = "Valor Inválido"
                } else{
                    val editor = preference.edit()
                    editor.putString("Quantia", quantiaRecebida.toString())
                    editor.apply()
                    txtTotalLitrosValor.text = "RS ${df.format(totalGasolina).replace(".",",")}"
               }
            }
        }



        Log.d("TotalGasolina", totalGasolina.toString())

        rbLitros.setOnClickListener {
            txtLitrosValor.text = "LITROS"
        }

        rbReal.setOnClickListener {
            txtLitrosValor.text = "VALOR"

        }

        btIrParaPagamentoActivity.setOnClickListener {
            val intent = Intent(context, FormasPagamentoActivity::class.java)

            startActivity(intent)
        }



    }

}