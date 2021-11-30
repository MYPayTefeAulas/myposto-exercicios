package br.uea.transirie.mypay.mypaytemplate.viewmodel

import android.util.Log
import br.uea.transirie.mypay.mypaytemplate.model.Pagamento
import br.uea.transirie.mypay.mypaytemplate.repository.room.*

class PagamentoViewModel(appDatabase: AppDatabase) {
    private val atendimentoRepo = AtendimentoRepository(appDatabase)
    private val veiculoRepo = VeiculoRepository(appDatabase)
    private val pagamentoRepo = PagamentoRepository(appDatabase)
    private val logTag = "PagamentoVM"

    fun cadastraPagamento(pagamento: Pagamento) {
        pagamentoRepo.save(pagamento)
        val pagamentoId = pagamento.id
        Log.i(logTag, "Pagamento #$pagamentoId cadastrado com sucesso!")
    }

    fun placaByAtendimentoId(atendimentoId: Long): String {
        val veiculoId = atendimentoRepo.atendimentoById(atendimentoId).veiculoId
        return veiculoRepo.veiculoById(veiculoId).placa
    }

    fun horaAtendimentoById(atendimentoId: Long): String {
        return atendimentoRepo.atendimentoById(atendimentoId)
                .dataRecebimento.substringAfter(" ")
    }
}