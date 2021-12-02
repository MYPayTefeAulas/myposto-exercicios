package br.uea.transirie.mypay.mypaytemplate.viewmodel

import android.util.Log
import br.uea.transirie.mypay.mypaytemplate.model.Servico
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.repository.room.ServicoRepository

class TabelaServicosViewModel(appDatabase: AppDatabase) {
    private var servicoRepository = ServicoRepository(appDatabase)
    private var servicoList: List<Servico>
    private val tagServicos = "TabServicosVM"

    init {
        Log.i(tagServicos, "Inicializando serviços.")
        servicoList = servicoRepository.getAllServicos()
    }

    fun getAllServicos(): List<Servico> = servicoList

    fun removerServico(servico: Servico){
        val desativado = true
        val servicoDesativado = Servico(servico.id, servico.descricao, servico.preco, desativado)

        Log.i(tagServicos, """Serviço: 
            |${servicoDesativado.descricao}
            | ${servicoDesativado.preco}
            | ${servicoDesativado.desativado}""".trimMargin())

        servicoRepository.save(servicoDesativado)
        servicoList = servicoRepository.getAllServicos()
    }
}