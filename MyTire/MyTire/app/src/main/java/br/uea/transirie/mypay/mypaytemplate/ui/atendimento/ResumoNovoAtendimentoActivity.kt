package br.uea.transirie.mypay.mypaytemplate.ui.atendimento

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.adapters.ResumoNovoAtendimentoAdapter
import br.uea.transirie.mypay.mypaytemplate.model.Atendimento
import br.uea.transirie.mypay.mypaytemplate.model.Cliente
import br.uea.transirie.mypay.mypaytemplate.model.Servico
import br.uea.transirie.mypay.mypaytemplate.model.Veiculo
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.ui.caixa.SplashPrimeiroUsoActivity
import br.uea.transirie.mypay.mypaytemplate.utils.getNewColor
import br.uea.transirie.mypay.mypaytemplate.utils.toPrecoString
import br.uea.transirie.mypay.mypaytemplate.viewmodel.ResumoNovoAtendimentoViewModel
import kotlinx.android.synthetic.main.activity_novo_atendimento_resumo.*
import kotlinx.android.synthetic.main.activity_pagamento_cartao.view.*
import kotlinx.android.synthetic.main.remocao_negada.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.util.*

class ResumoNovoAtendimentoActivity : AppCompatActivity() {
    private val context = this@ResumoNovoAtendimentoActivity
    private val viewModel =
        ResumoNovoAtendimentoViewModel(AppDatabase.getDatabase(context))
    private val myAdapter =
            ResumoNovoAtendimentoAdapter(context, mutableListOf(), ::onRemoveClick)

    private var precoTotal: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novo_atendimento_resumo)

        title = getString(R.string.resumo_entrada)

        val colorDrawable = ColorDrawable(getNewColor(context))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val placa = intent.getStringExtra(getString(R.string.EXTRA_PLACA))
        val telefone = intent.getStringExtra(getString(R.string.EXTRA_TELEFONE))
        val categoria = intent.getStringExtra(getString(R.string.EXTRA_CATEGORIA))

        // Recupera a data e hora com fuso horário GMT-4
        val formatter = SimpleDateFormat(getString(R.string.formato_data), Locale.getDefault())
        val dataCompleta = formatter.format(Date())
        val data = dataCompleta.substringBefore(" ")
        val hora = dataCompleta.substringAfter(" ")

        novoAtendimento_tvPlaca.text = placa
        novoAtendimento_tvTelefone.text = telefone
        novoAtendimento_tvCategoria.text = categoria
        novoAtendimento_tvHoraEntrada.text = hora
        novoAtendimento_tvDataEntrada.text = data

        val myLayoutManager = LinearLayoutManager(context)
        myLayoutManager.orientation = LinearLayoutManager.VERTICAL

        resumoNovoAtendimento_rv.layoutManager = myLayoutManager
        resumoNovoAtendimento_rv.adapter = myAdapter

        updateAdapter()

        novoAtendimento_btAvancar.setOnClickListener {
            val cliente = Cliente(telefone = telefone.toString())
            val veiculo = Veiculo(placa = placa.toString(), categoria = categoria.toString())
            val atendimento = Atendimento(dataRecebimento = dataCompleta, valorTotal = precoTotal)

            val intent = Intent(context, SplashPrimeiroUsoActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            doAsync {
                viewModel.adicionarAtendimento(cliente, veiculo, atendimento)

                uiThread { registeredService(intent) }
            }
        }
    }

    private fun onRemoveClick(servico: Servico) {
        if (viewModel.haMaisdeUmEscolhido()) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
            builder.setMessage("Remover este item?")
            builder.setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                doAsync {
                    viewModel.removeServico(servico)
                    uiThread { updateAdapter() }
                }
                dialog.dismiss()
            }
            builder.setNegativeButton(getString(R.string.cancelar)) { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        } else {
            val dialogBuilder = AlertDialog.Builder(context).create()

            val view = layoutInflater.inflate(R.layout.remocao_negada, null)
            view.remocaoNegada_btnOK.setOnClickListener {
                dialogBuilder.dismiss()
            }

            dialogBuilder.setView(view)
            dialogBuilder.show()
        }
    }

    private fun updateAdapter() {
        val listaServicos = viewModel.getServicosEscolhidos()
        myAdapter.swapData(listaServicos)

        precoTotal = listaServicos.map{ it.preco }.sum()
        novoAtendimento_tvTotal.text = precoTotal.toPrecoString()
    }

    private fun registeredService(intent: Intent){
        val mLayoutInflater = layoutInflater
        val view: View = mLayoutInflater.inflate(R.layout.cadastro_concluido, null)

        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setView(view)
        view.findViewById<TextView>(R.id.cadastro_mensagem).text = getString(R.string.atendimento_registrado)
        view.findViewById<View>(R.id.ok_btn).setOnClickListener {
            alertDialog.dismiss()
            startActivity(intent)
            finish()
        }
        alertDialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}