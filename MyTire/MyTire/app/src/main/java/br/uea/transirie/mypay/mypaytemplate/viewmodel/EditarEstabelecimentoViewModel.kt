package br.uea.transirie.mypay.mypaytemplate.viewmodel

import br.uea.transirie.mypay.mypaytemplate.model.Estabelecimento
import br.uea.transirie.mypay.mypaytemplate.model.Usuario
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.repository.room.EstabelecimentoRepository
import br.uea.transirie.mypay.mypaytemplate.repository.room.UsuarioRepository

class EditarEstabelecimentoViewModel(appDatabase: AppDatabase) {
    private val estabelecimentoRepository = UsuarioRepository(appDatabase)

    fun updateDataEstabelecimento(estabelecimento: Usuario){
        estabelecimentoRepository.save(estabelecimento)
    }

    fun estabelecimentoByEmail(email: String): Usuario? =
            estabelecimentoRepository.usuarioByEmail(email)

}
