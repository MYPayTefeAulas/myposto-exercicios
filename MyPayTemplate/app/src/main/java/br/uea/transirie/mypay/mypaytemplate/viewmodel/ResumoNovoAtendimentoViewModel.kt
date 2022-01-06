package br.uea.transirie.mypay.mypaytemplate.viewmodel

import android.util.Log
import br.uea.transirie.mypay.mypaytemplate.model.*
import br.uea.transirie.mypay.mypaytemplate.repository.room.*
class ResumoNovoAtendimentoViewModel(appDatabase: AppDatabase) {
    private val clienteRepository = ClienteRepository(appDatabase)
    private val veiculoRepository = VeiculoRepository(appDatabase)
    private val atendimentoRepository = AtendimentoRepository(appDatabase)
    private val itemAtendimentoRepository = ItemAtendimentoRepository(appDatabase)

    private val servicosEscolhidosList = ServicosNovoAtendimentoViewModel.listaEscolhidos

    fun getServicosEscolhidos(): List<Servico> = servicosEscolhidosList

    fun removeServico(servico: Servico) {
        ServicosNovoAtendimentoViewModel.updateEscolhidos(servico, false)
        servicosEscolhidosList.remove(servico)
    }

    fun haMaisdeUmEscolhido() = (servicosEscolhidosList.size > 1)

    /**
     * Função que realiza o cadastro de um novo atendimento e seus serviços relacionados.
     * Também cadastra um novo cliente e veículo.
     */
    fun adicionarAtendimento(cliente: Cliente, veiculo: Veiculo, atendimento: Atendimento) {
        clienteRepository.save(cliente)
        val clienteId = cliente.id
        printa("Cliente (id: $clienteId) criado com sucesso.")

        veiculo.clienteId = clienteId
        veiculoRepository.save(veiculo)
        val veiculoId: Long = veiculo.id
        printa("Veiculo (id: $veiculoId) criado com sucesso.")

        atendimento.veiculoId = veiculoId
        atendimentoRepository.save(atendimento)
        val atendimentoId = atendimento.id
        printa("Atendimento (id: $atendimentoId) criado com sucesso.")

        servicosEscolhidosList.forEach {servico ->
            val itemAtendimento =
                ItemAtendimento(servicoId = servico.id, atendimentoId = atendimentoId)
            itemAtendimentoRepository.save(itemAtendimento)
            printa("ItemAtendimento para o servico ${servico.descricao} criado com sucesso.")
        }
    }

    private fun printa(msg: String) = Log.i("ResumoNATViewModel", msg)
}

