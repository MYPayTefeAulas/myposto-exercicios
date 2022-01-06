package br.uea.transirie.mypay.mypaytemplate.viewmodel

import br.uea.transirie.mypay.mypaytemplate.model.*
import br.uea.transirie.mypay.mypaytemplate.repository.room.*
import br.uea.transirie.mypay.mypaytemplate.utils.toPrecoString

class ResumoSaidaViewModel(
    appDatabase: AppDatabase,
    atendimentoId: Long
) {
//    private val className = "RESUMO_SAIDA_VIEW_MODEL"
    private val servicoRepo = ServicoRepository(appDatabase)
    private val itemAtendimentoRepo = ItemAtendimentoRepository(appDatabase)
    private val atendimentoRepo = AtendimentoRepository(appDatabase)
    private val clienteRepo = ClienteRepository(appDatabase)
    private val veiculoRepo = VeiculoRepository(appDatabase)

    private val espaco = ' '
    private var atendimento = Atendimento()
    private var veiculo = Veiculo()
    private var cliente = Cliente()
    private val listaItems = mutableListOf<ItemAtendimento>()
    private val listaServicos = mutableListOf<Servico>()

    init {
        atendimento = atendimentoRepo.atendimentoById(atendimentoId)
        veiculo = veiculoRepo.veiculoById(atendimento.veiculoId)
        cliente = clienteRepo.clienteById(veiculo.clienteId)

        /* Constroi lista de itens e serviços relacionados ao atendimento */
        itemAtendimentoRepo.itemAtendimentoByAtendimentoId(atendimentoId).map { itemAtendimento ->
            listaItems.add(itemAtendimento)
            servicoRepo.servicoById(itemAtendimento.servicoId).let { servico ->
                listaServicos.add(servico)
            }
        }
    }

    fun getPrecoTotal() = atendimento.valorTotal

    fun getPrecoTotalString(): String = atendimento.valorTotal.toPrecoString()

    fun getDataRecebimento(): String = atendimento.dataRecebimento.substringBefore(espaco)

    fun getHoraRecebimento(): String = atendimento.dataRecebimento.substringAfter(espaco)

    fun getPlaca(): String = veiculo.placa

    fun getCategoria(): String = veiculo.categoria

    fun getTelefone(): String = cliente.telefone

    fun getListaServicos(): List<Servico> = listaServicos


}