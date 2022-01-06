package br.uea.transirie.mypay.mypaytemplate.viewmodel

import br.uea.transirie.mypay.mypaytemplate.model.Servico
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.repository.room.ServicoRepository
import br.uea.transirie.mypay.mypaytemplate.utils.toPrecoString

class EditarServicoViewModel(appDatabase: AppDatabase) {
    private var servicoRepository = ServicoRepository(appDatabase)
    private var servico = Servico()

    fun start(servicoId: Long) {
        servico = servicoRepository.servicoById(servicoId)
    }

    fun atualizarServico(updatedServico: Servico) {
        servicoRepository.save(updatedServico)
    }

    fun getServicoDescricao() = servico.descricao

    fun getServicoPreco() = servico.preco.toPrecoString(withPrefix = false)

    fun deleteServico() = servicoRepository.delete(servico)

}