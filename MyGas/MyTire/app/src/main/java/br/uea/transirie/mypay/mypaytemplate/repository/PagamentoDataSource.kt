package br.uea.transirie.mypay.mypaytemplate.repository

import br.uea.transirie.mypay.mypaytemplate.model.Pagamento

interface PagamentoDataSource: BaseDataSource<Pagamento> {
    fun getAllPagamentos(): List<Pagamento>
    fun pagamentoByAtendimentoId(atendimentoId: Long): List<Pagamento>
}