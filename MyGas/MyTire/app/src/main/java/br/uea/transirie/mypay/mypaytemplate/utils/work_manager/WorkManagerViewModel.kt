package br.uea.transirie.mypay.mypaytemplate.utils.work_manager

import br.uea.transirie.mypay.mypaytemplate.model.Estabelecimento
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.repository.room.EstabelecimentoRepository
import br.uea.transirie.mypay.mypaytemplate.repository.room.UsuarioRepository

class WorkManagerViewModel(appDatabase: AppDatabase) {
    val estabelecimentoRepository = EstabelecimentoRepository(appDatabase)
    private val usuarioRepository = UsuarioRepository(appDatabase)

    fun saveEstabelecimento(estabelecimento: Estabelecimento) {
        return estabelecimentoRepository.save(estabelecimento)
    }

    fun estabelecimentoByCNPJ(cnpj: String): Estabelecimento? {
        return estabelecimentoRepository.estabelecimentoByCNPJ(cnpj)
    }

    fun getUsuarios() = usuarioRepository.getAllUsuario()
}