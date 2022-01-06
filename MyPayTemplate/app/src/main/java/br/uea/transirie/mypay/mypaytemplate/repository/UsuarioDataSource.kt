package br.uea.transirie.mypay.mypaytemplate.repository

import br.uea.transirie.mypay.mypaytemplate.model.Usuario

interface UsuarioDataSource: BaseDataSource<Usuario> {
    fun getAllUsuario(): List<Usuario>
    fun usuarioById(idUsuario: Long): Usuario
    fun usuarioByEmail(emailUsuario: String): Usuario
    fun pinJaEmUso(pinUsuario: Int): Boolean
    fun emailJaEmuso(email: String): Boolean
    fun usuarioByPin(pin: Int): Usuario
}