package br.uea.transirie.mypay.mypaytemplate.viewmodel

import br.uea.transirie.mypay.mypaytemplate.model.Atendimento
import br.uea.transirie.mypay.mypaytemplate.model.Pagamento
import br.uea.transirie.mypay.mypaytemplate.model.Servico
import br.uea.transirie.mypay.mypaytemplate.model.Veiculo
import br.uea.transirie.mypay.mypaytemplate.repository.room.*

class DetalhesAtendimentoViewModel(appDatabase: AppDatabase) {
    private val servicoRepository = ServicoRepository(appDatabase)
    private val pagamentoRepository = PagamentoRepository(appDatabase)
    private val veiculoRepository = VeiculoRepository(appDatabase)
    private val itemAtendimentoRepo = ItemAtendimentoRepository(appDatabase)
    private val atendimentoRepo = AtendimentoRepository(appDatabase)
    private var mAtendimentoId: Long = 0L

    fun pagamentoByAtendimentoId(veiculoId: Long): Pagamento{
        val atendimento = atendimentoRepo.atendimentoByVeiculoId(veiculoId).first()
        mAtendimentoId = atendimento.id

        return pagamentoRepository.pagamentoByAtendimentoId(mAtendimentoId).first()
    }

    fun veiculoId(veiculoId: Long): Veiculo = veiculoRepository.veiculoById(veiculoId)

    fun listaServicosVeiculo(): List<Servico>{
        val servicos= itemAtendimentoRepo.itemAtendimentoByAtendimentoId(mAtendimentoId)

        return servicos.map { servicoRepository.servicoById(it.servicoId) }
    }

    fun dadosVeiculo(): List<String>{
        return veiculoRepository.getAllVeiculos().map { it.placa }
    }

    fun atendimentoId(veiculoId: Long): Atendimento{
        return atendimentoRepo.atendimentoByVeiculoId(veiculoId).first()
    }
}