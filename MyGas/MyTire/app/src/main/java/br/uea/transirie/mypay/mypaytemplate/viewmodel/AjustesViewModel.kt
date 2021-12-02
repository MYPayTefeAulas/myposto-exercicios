package br.uea.transirie.mypay.mypaytemplate.viewmodel

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

class AjustesViewModel(appDatabase: AppDatabase) {
    private val _estabelecimentos = MutableLiveData<Usuario>()
    val estabelecimentos: LiveData<Usuario> = _estabelecimentos

    private var usuarioRepository = UsuarioRepository(appDatabase)

    fun consultaEstabelecimentoByEmail(email: String) =
        CoroutineScope(Dispatchers.IO).launch {
            val estabelecimento = usuarioRepository.usuarioByEmail(email)
            _estabelecimentos.postValue(estabelecimento)
        }

}
