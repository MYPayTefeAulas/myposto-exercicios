package br.uea.transirie.mypay.mypaytemplate.viewmodel

import android.util.Log
import br.uea.transirie.mypay.mypaytemplate.model.AtendimentoClienteEVeiculo
import br.uea.transirie.mypay.mypaytemplate.repository.room.*

class ListaAtendimentosViewModel(appDatabase: AppDatabase) {
    private val clienteRepository = ClienteRepository(appDatabase)
    private val pagamentoRepository = PagamentoRepository(appDatabase)
    private val atendimentoRepository = AtendimentoRepository(appDatabase)

    private var listaACVs = mutableListOf<AtendimentoClienteEVeiculo>()
    private val tag: String = "ListaAtendimentosVM"

    init {
        constroiListaAtendimentos()
    }

    /**
     * Recupero uma lista com os ids dos atendimentos já pagos. Em seguida,
     * recupero uma lista com todos os atendimentos, do mais recente ao mais antigo e,
     * para cada atendimento, verifico se seu id consta como já pago. Se não constar,
     * adiciono à lista de atendimentos abertos.
     */
    private fun constroiListaAtendimentos() {
        val listaAtendimentos = atendimentoRepository.getAllAtendimentos().asReversed()

        val listaAtendimentosJaPagos =
                pagamentoRepository.getAllPagamentos().map { it.atendimentoId }

        listaAtendimentos.forEach { atendimento ->
            if (atendimento.id !in listaAtendimentosJaPagos) {
                val cev = clienteRepository.clienteEVeiculoByAtendimentoId(atendimento.id)
                val atendimentoVeC = AtendimentoClienteEVeiculo(atendimento.id, cev)

                listaACVs.add(atendimentoVeC)

                Log.i(tag, "Atendimento não pago: $atendimento")
            }
        }
    }

    fun getListaAtendimentosClientesEVeiculos() =
        listaACVs as List<AtendimentoClienteEVeiculo>
}