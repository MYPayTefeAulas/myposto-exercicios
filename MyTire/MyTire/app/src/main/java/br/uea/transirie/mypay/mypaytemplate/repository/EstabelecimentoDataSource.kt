package br.uea.transirie.mypay.mypaytemplate.repository

import br.uea.transirie.mypay.mypaytemplate.model.Estabelecimento

interface EstabelecimentoDataSource: BaseDataSource<Estabelecimento> {
    fun estabelecimentoId(): Long
    fun estabelecimentoById(id: Long): Estabelecimento
    fun estabelecimentoByCNPJ(cnpj: String): Estabelecimento?
    fun getEstabelecimentos(): List<Estabelecimento>
    fun cnpjJaEmUso(cnpj: String): Boolean
}