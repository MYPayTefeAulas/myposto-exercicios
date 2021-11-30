package br.uea.transirie.mypay.mypaytemplate.viewmodel

import android.util.Log
import br.uea.transirie.mypay.mypaytemplate.model.*
import br.uea.transirie.mypay.mypaytemplate.repository.room.*

class HistoricoAtendimentosViewModel(appDatabase: AppDatabase) {
    private val clienteRepository = ClienteRepository(appDatabase)
    private val pagamentoRepository = PagamentoRepository(appDatabase)

    private var listaPCVs: MutableList<PagamentoClienteEVeiculo> = mutableListOf()
    private val tagHistorico= "HistoricoVM"

    init {
        val listaPagamentos: List<Pagamento> = pagamentoRepository.getAllPagamentos().asReversed()

        listaPagamentos.forEach { pagamento ->
            Log.i(tagHistorico, "-----Novo Pagamento-----")
            Log.i(tagHistorico, "Id do atendimento pago: ${pagamento.atendimentoId}")

            val cev = clienteRepository.clienteEVeiculoByAtendimentoId(pagamento.atendimentoId)
            Log.i(tagHistorico, "Id do veiculo atendido: ${cev.veiculo.id}")
            Log.i(tagHistorico, "Id do cliente proprietário: ${cev.cliente.id}")

            val pcv = PagamentoClienteEVeiculo(pagamento, cev)
            listaPCVs.add(pcv)
        }
    }

    //Função que retorna a lista conjunta de pagamentos, clientes e veiculos
    fun getPagamentosClientesEVeiculos(): List<PagamentoClienteEVeiculo> = listaPCVs
}

