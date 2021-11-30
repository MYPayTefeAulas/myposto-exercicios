package br.uea.transirie.mypay.mypaytemplate.ui.recuperar_pin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.uea.transirie.mypay.mypaytemplate.model.Usuario
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.repository.room.UsuarioRepository

class RecuperarPinViewModel(application: Application): AndroidViewModel(application) {

    private val usuarioRepository = UsuarioRepository(AppDatabase.getDatabase(application))

    fun usuarioByEmail(email:String) : Usuario? {
        return usuarioRepository.usuarioByEmail(email)
    }

    fun saveUsuario(estabelecimento: Usuario) {
        usuarioRepository.save(estabelecimento)
    }

    fun pinByEmail(email: String): String {
        val usu = usuarioByEmail(email)

        return if (usu != null) (usu.pin / 100).toString() else "0000"
    }
}