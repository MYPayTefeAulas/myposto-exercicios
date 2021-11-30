package br.uea.transirie.mypay.mypaytemplate.ui.home

import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.model.Estabelecimento
import br.uea.transirie.mypay.mypaytemplate.model.Usuario
import br.uea.transirie.mypay.mypaytemplate.repository.room.EstabelecimentoRepository
import br.uea.transirie.mypay.mypaytemplate.repository.room.UsuarioRepository

class AtualizarSenhaViewModel(appDatabase: AppDatabase) {
    private var estabelecimentoRespository = UsuarioRepository(appDatabase)

    fun recuperaTodosEstabelecimentos(): List<Usuario> {
        return estabelecimentoRespository.getAllUsuario()
    }

    fun atualizaSenha(objEstablecimento: Usuario) {
        return estabelecimentoRespository.update(objEstablecimento)
    }

    fun estabelecimentoByEmail(email: String): Usuario {
        return estabelecimentoRespository.usuarioByEmail(email)
    }
}