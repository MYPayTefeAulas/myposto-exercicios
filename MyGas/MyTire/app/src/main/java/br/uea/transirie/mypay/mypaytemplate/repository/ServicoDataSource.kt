package br.uea.transirie.mypay.mypaytemplate.repository

import br.uea.transirie.mypay.mypaytemplate.model.Servico

interface ServicoDataSource: BaseDataSource<Servico> {
    fun servicoById(id: Long): Servico
    fun searchByDescricao(termoBusca: String): List<Servico>
    fun getAllServicosAtivos(): List<Servico>
}