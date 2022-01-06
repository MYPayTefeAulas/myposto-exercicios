package br.uea.transirie.mypay.mypaytemplate.ui.atendimento

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import kotlinx.android.synthetic.main.activity_novo_atendimento.*
import br.uea.transirie.mypay.mypaytemplate.utils.*
import br.uea.transirie.mypay.mypaytemplate.viewmodel.DetalhesAtendimentoViewModel
import kotlinx.android.synthetic.main.activity_cadastrar_servico.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class NovoAtendimentoActivity : AppCompatActivity() {
    private val myContext = this@NovoAtendimentoActivity
    private var placa: String = ""
    private var telefone: String = ""
    private var categoria: String = ""
    private lateinit var viewModelDadosVeiculo: DetalhesAtendimentoViewModel
    private var listaPlacas = listOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novo_atendimento)

        title = getString(R.string.nova_entrada)

        //filtro para que todas as letras digitadas sejam maiúsculas
        novoAtendimento_etPlaca.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        maskEditText(getString(R.string.MASK_PLACA), novoAtendimento_etPlaca)
        novoAtendimento_etTelefone.let { it.addTextChangedListener(MyMask(MaskType.Telephone, it)) }

        nAtend_btnCarroOutlined.setOnClickListener { categoryClickListener("Carro") }
        nAtend_btnMotoOutlined.setOnClickListener { categoryClickListener("Moto") }
//        nAtend_btnCaminhaoOutlined.setOnClickListener { categoryClickListener("Caminhão") }
//        nAtend_btnBicicletaOutlined.setOnClickListener { categoryClickListener("Bicicleta") }

        nAtend_btnCarro.setOnClickListener { categoryClickListener() }
        nAtend_btnMoto.setOnClickListener { categoryClickListener() }
//        nAtend_btnCaminhao.setOnClickListener { categoryClickListener() }
//        nAtend_btnBicicleta.setOnClickListener { categoryClickListener() }

        novoAtendimento_btProximo.setOnClickListener {
            placa = novoAtendimento_tiPlaca.editText?.text.toString()
            telefone = novoAtendimento_tiTelefone.editText?.text.toString()

            doAsync {
                viewModelDadosVeiculo = DetalhesAtendimentoViewModel(AppDatabase.getDatabase(myContext))
                listaPlacas = viewModelDadosVeiculo.dadosVeiculo()
                uiThread {
                    if (inputCheck()){
                        nextActivity()
                    }
                }
            }
        }
    }

    private fun nextActivity() {
        val intent = Intent(this, ServicosNovoAtendimentoActivity::class.java)
            .putExtra("EXTRA_TELEFONE", telefone)
            .putExtra("EXTRA_PLACA", placa)
            .putExtra("EXTRA_CATEGORIA", categoria)

        startActivity(intent)
    }

    private fun categoryClickListener(opt: String = "") {
        val invisible = View.INVISIBLE
        val visible = View.VISIBLE

        categoria = opt

        novoAtendimento_tiPlaca.isEnabled = true
        if (categoria == "Carro") {
            nAtend_btnCarro.visibility = visible
            nAtend_btnCarroOutlined.visibility = invisible
        } else {
            nAtend_btnCarro.visibility = invisible
            nAtend_btnCarroOutlined.visibility = visible
        }

        if (categoria == "Moto"){
            nAtend_btnMoto.visibility = visible
            nAtend_btnMotoOutlined.visibility = invisible
        } else {
            nAtend_btnMoto.visibility = invisible
            nAtend_btnMotoOutlined.visibility = visible
        }
/*
        if (categoria == "Caminhão") {
            nAtend_btnCaminhao.visibility = visible
            nAtend_btnCaminhaoOutlined.visibility = invisible
        } else {
            nAtend_btnCaminhao.visibility = invisible
            nAtend_btnCaminhaoOutlined.visibility = visible
        }

        if (categoria == "Bicicleta") {
            nAtend_btnBicicleta.visibility = visible
            nAtend_btnBicicletaOutlined.visibility = invisible
            novoAtendimento_tiPlaca.isEnabled = false
        } else {
            nAtend_btnBicicleta.visibility = invisible
            nAtend_btnBicicletaOutlined.visibility = visible
        }
*/
    }

    private fun inputCheck(): Boolean {
        var isInputOkay = true

        if (categoria == "Bicicleta") {
            placa = "SEM PLACA"
        }else {
            if (placaHasErrors(novoAtendimento_tiPlaca)) {
                isInputOkay = false
            }
            else if (listaPlacas.contains(placa)){
                isInputOkay = false
                novoAtendimento_tiPlaca.error = "Veículo já em atendimento."
            }
        }

        if (telefoneHasErrors(novoAtendimento_tiTelefone)) {
            isInputOkay = false
        }

        if (categoria.isEmpty()) {
            nAtend_tvCategoria.text = getString(R.string.selecione_uma_categoria_error)
            nAtend_tvCategoria.setTextColor(getNewColor(myContext, R.color.error_red))

            isInputOkay = false
        } else {
            nAtend_tvCategoria.text = getString(R.string.selecione_uma_categoria)
            nAtend_tvCategoria.setTextColor(getNewColor(myContext, R.color.categoria_txt_color))
        }

        return isInputOkay
    }
}