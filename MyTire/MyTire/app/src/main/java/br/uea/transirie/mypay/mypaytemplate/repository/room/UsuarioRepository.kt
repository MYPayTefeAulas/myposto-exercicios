package br.uea.transirie.mypay.mypaytemplate.repository.room

import br.uea.transirie.mypay.mypaytemplate.model.Usuario
import br.uea.transirie.mypay.mypaytemplate.repository.UsuarioDataSource

class UsuarioRepository(database: AppDatabase): UsuarioDataSource{
    private val usuarioDao = database.usuarioDao()

    override fun usuarioByEmail(emailUsuario: String): Usuario {
        return usuarioDao.usuarioByEmail(emailUsuario)
    }

    override fun usuarioById(idUsuario: Long): Usuario {
        return usuarioDao.usuarioById(idUsuario)
    }

    override fun getAllUsuario(): List<Usuario> {
        return usuarioDao.getAllUsuario()
    }

    override fun pinJaEmUso(pinUsuario: Int): Boolean {
        return usuarioDao.pinJaEmUso(pinUsuario)
    }

    override fun emailJaEmuso(email: String): Boolean =
        usuarioDao.emailJaEmUso(email)

    override fun usuarioByPin(pin: Int): Usuario =
        usuarioDao.usuarioByPin(pin)

    override fun delete(obj: Usuario) {
        return usuarioDao.delete(obj)
    }

    override fun insert(obj: Usuario): Long {
        return usuarioDao.insert(obj)
    }

    override fun save(obj: Usuario) {
        if (obj.id == 0L) {
            val id = insert(obj)
            obj.id = id
        } else {
            update(obj)
        }
    }

    override fun update(obj: Usuario) {
        return usuarioDao.update(obj)
    }
}
