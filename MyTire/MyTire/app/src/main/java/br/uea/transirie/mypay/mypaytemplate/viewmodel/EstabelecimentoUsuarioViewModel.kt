package br.uea.transirie.mypay.mypaytemplate.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.uea.transirie.mypay.mypaytemplate.model.Estabelecimento
import br.uea.transirie.mypay.mypaytemplate.model.Usuario
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.repository.room.EstabelecimentoRepository
import br.uea.transirie.mypay.mypaytemplate.repository.room.UsuarioRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EstabelecimentoUsuarioViewModel(appDatabase: AppDatabase) {
    private var estabelecimentoRepository = EstabelecimentoRepository(appDatabase)
    private val usuarioRepository = UsuarioRepository(appDatabase)
    private val usuarios = getUsuarios()

    fun getUsuarios(): List<Usuario>{
//        CoroutineScope(Dispatchers.IO).launch {
//            val usuarios = usuarioRepository.getAllUsuario()
//            _usuarios.postValue(usuarios)
//        }
        return usuarioRepository.getAllUsuario()
    }

    fun estabelecimentoByCNPJ(cnpj: String): Estabelecimento? =
        estabelecimentoRepository.estabelecimentoByCNPJ(cnpj)

    fun usuarioByPin(pin: Int): Usuario {
        val usuario = usuarioRepository.usuarioByPin(pin)

        if (usuario == null)
            Log.i("LOGIN_VIEW_MODEL", "Pin não cadastrado no banco de dados")

        return usuario
    }

    fun deleteUsuario(usuario: Usuario) = usuarioRepository.delete(usuario)

    fun getNumGerentes(): Int = usuarios.filter { it.isGerente }.size

    fun deleteEstabelecimento(estab: Estabelecimento) = estabelecimentoRepository.delete(estab)

    fun deleteAllUsuarios() {
        usuarios.forEach {
            deleteUsuario(it)
        }
    }
}