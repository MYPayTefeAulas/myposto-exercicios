package br.uea.transirie.mypay.mypaytemplate.viewmodel

import br.uea.transirie.mypay.mypaytemplate.model.Usuario
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.repository.room.UsuarioRepository

class UsuariosViewModel(appDatabase: AppDatabase) {
    private var usuarioRepository = UsuarioRepository(appDatabase)

    fun getAllUsuario():List<Usuario> = usuarioRepository.getAllUsuario()
}