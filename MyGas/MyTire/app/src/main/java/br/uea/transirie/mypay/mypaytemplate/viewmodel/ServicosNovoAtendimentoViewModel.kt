package br.uea.transirie.mypay.mypaytemplate.viewmodel

import android.util.Log
import br.uea.transirie.mypay.mypaytemplate.model.Servico
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.repository.room.ServicoRepository

class ServicosNovoAtendimentoViewModel(appDatabase: AppDatabase) {
    private val servicoRepository = ServicoRepository(appDatabase)
    private val listaAtivos = mutableListOf<Servico>()

    companion object Singleton {
        private const val nameString = "ServicosNATViewModel"
        var listaEscolhidos = mutableListOf<Servico>()

        fun updateEscolhidos(servico: Servico, include: Boolean) {
            var msg = "${servico.descricao}, ${servico.id} "

            if (include) {
                msg += "(Serviço incluído)"
                listaEscolhidos.add(servico)
            } else {
                msg += "(Serviço removido)"
                listaEscolhidos.remove(servico)
            }

            Log.i(nameString, msg)
            Log.i(nameString, "# de escolhidos: ${listaEscolhidos.size}")
        }
    }

    init {
        servicoRepository.getAllServicosAtivos().let {
            listaAtivos.addAll(it)
        }
        resetaEscolhidos()
    }

    private fun resetaEscolhidos() {
        val tamAntigo = listaEscolhidos.size
        val logMsg = "Reset solicitado\n" +
                "Tamanho antigo da lista de escolhidos: $tamAntigo"

        if (tamAntigo > 0) {
            listaEscolhidos.clear()
        }

        Log.i(nameString, logMsg)
    }

    fun getAllServicosAtivos(): List<Servico> = listaAtivos

    fun getServicosEscolhidos(): MutableList<Servico> = listaEscolhidos

    fun updateEscolhidos(servico: Servico, include: Boolean) =
        Singleton.updateEscolhidos(servico, include)
}