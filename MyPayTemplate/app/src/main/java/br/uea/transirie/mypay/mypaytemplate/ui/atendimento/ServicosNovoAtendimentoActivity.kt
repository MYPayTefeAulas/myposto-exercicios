package br.uea.transirie.mypay.mypaytemplate.ui.atendimento

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.recyclerview.widget.LinearLayoutManager
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.adapters.ServicosNovoAtendimentoAdapter
import br.uea.transirie.mypay.mypaytemplate.model.Servico
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.viewmodel.ServicosNovoAtendimentoViewModel
import kotlinx.android.synthetic.main.activity_novo_atendimento_servicos.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ServicosNovoAtendimentoActivity : AppCompatActivity() {
    private val context = this@ServicosNovoAtendimentoActivity
    private lateinit var adapter: ServicosNovoAtendimentoAdapter
    private var viewModel: ServicosNovoAtendimentoViewModel? = null

    private var intentPlaca: String? = null
    private var intentTelefone: String? = null
    private var intentCategoria: String? = null
    private val dezMilhoes = 10_000_000.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupInicial()

        doAsync {
            viewModel = ServicosNovoAtendimentoViewModel(AppDatabase.getDatabase(context))
            uiThread {
                updateAdapter()
            }
        }

        setListeners()
    }

    private fun setupInicial() {
        setContentView(R.layout.activity_novo_atendimento_servicos)

        title = getString(R.string.servicos)

        intentPlaca = this.intent.getStringExtra(getString(R.string.EXTRA_PLACA))
        intentTelefone = intent.getStringExtra(getString(R.string.EXTRA_TELEFONE))
        intentCategoria = this.intent.getStringExtra(getString(R.string.EXTRA_CATEGORIA))

        adapter = ServicosNovoAtendimentoAdapter(
            context,
            mutableListOf(),
            mutableListOf(),
            ::onCheckBoxClick
        )

        selecionarServicos_rv.adapter = adapter
        selecionarServicos_rv.layoutManager = LinearLayoutManager(context)
    }

    private fun setListeners() {
        novoAtendimento_btRegistrar.setOnClickListener {
            viewModel?.let {
                /*
                O texto desse componente é redefinido para que a mensagem de erro
                desapareça, se não houver mais erros.
                */
                tvSelecioneUm.text = getString(R.string.selecione_ao_menos_um_servico)

                if (it.getServicosEscolhidos().size > 0) {
                    val sum = it.getServicosEscolhidos().map { servico -> servico.preco }.sum()

                    if (sum > dezMilhoes) {
                        val erroStr = getString(R.string.erro_soma_servicos_ultrapassa_dez_mi)
                        tvSelecioneUm.text = toErrorString(erroStr)
                    } else {
                        val intent =
                            Intent(this, ResumoNovoAtendimentoActivity::class.java)
                                .putExtra(getString(R.string.EXTRA_PLACA), intentPlaca)
                                .putExtra(getString(R.string.EXTRA_TELEFONE), intentTelefone)
                                .putExtra(getString(R.string.EXTRA_CATEGORIA), intentCategoria)

                        startActivity(intent)
                    }
                } else {
                    val erroStr = getString(R.string.selecione_ao_menos_um_servico)

                    tvSelecioneUm.text = toErrorString(erroStr)
                }
            }
        }
    }

    /**
     * É necessário chamar updateAdapter no onResume
     * para que atualize, principalmente, a lista de escolhidos
     */
    override fun onResume() {
        updateAdapter()
        super.onResume()
    }

    private fun updateAdapter() {
        viewModel?.let {
            val listaAtivos = it.getAllServicosAtivos()
            val listaEscolhidos = it.getServicosEscolhidos()
            adapter.swapData(listaAtivos, listaEscolhidos)
        }
    }

    private fun onCheckBoxClick(servico: Servico, isChecked: Boolean) =
        viewModel?.updateEscolhidos(servico, isChecked)

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * @return retorna a string informada junto de um prefixo "Erro: ", este último pintado de
     * vermelho.
     *
     * @param errorString o texto a ser concatenada com "Erro: ".
     */
    private fun toErrorString(errorString: String): SpannableString {
        val strErro = getString(R.string.erro_dois_pontos) + errorString
        val spannableString = SpannableString(strErro)
        val fColor = ForegroundColorSpan(Color.RED)

        spannableString.setSpan(fColor, 0, 5, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        return spannableString
    }
}