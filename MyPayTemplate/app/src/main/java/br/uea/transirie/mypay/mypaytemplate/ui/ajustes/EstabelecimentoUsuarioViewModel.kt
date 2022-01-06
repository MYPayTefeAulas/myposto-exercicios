package br.uea.transirie.mypay.mypaytemplate.ui.ajustes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.model.Estabelecimento
import br.uea.transirie.mypay.mypaytemplate.model.Usuario
import br.uea.transirie.mypay.mypaytemplate.repository.room.UsuarioRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EstabelecimentoUsuarioViewModel(appDatabase: AppDatabase) {
    private var usuarioRepository = UsuarioRepository(appDatabase)
    private val _usuarios = MutableLiveData<Usuario>()
    val usuarios: LiveData<Usuario> = _usuarios

    /**
     * Acessa repositório de cliente para atualizar um existente.
     */
    fun atualizarUsuario(usuario: Usuario) {
        usuarioRepository.update(usuario)
    }

    /**
     * Acessa repositório do estabelecimento para deletar um existente.
     */
    fun deletarUsuario(usuario: Usuario) {
        usuarioRepository.delete(usuario)
    }

    fun consultaEstabelecimentoByEmail(email: String) =
        CoroutineScope(Dispatchers.IO).launch {
            val estabelecimento = usuarioRepository.usuarioByEmail(email)
            _usuarios.postValue(estabelecimento)
        }
}