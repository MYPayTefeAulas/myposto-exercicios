package br.uea.transirie.mypay.mypaytemplate.viewmodel

import br.uea.transirie.mypay.mypaytemplate.model.Servico
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.repository.room.ServicoRepository

class CadastrarServicoViewModel(appDatabase: AppDatabase) {
    private val servicoRepository = ServicoRepository(appDatabase)

    fun saveServico(servico: Servico) {
        servicoRepository.save(servico)
    }
}