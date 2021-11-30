package br.uea.transirie.mypay.mypaytemplate.repository

import br.uea.transirie.mypay.mypaytemplate.model.ItemAtendimento

interface ItemAtendimentoDataSource: BaseDataSource<ItemAtendimento> {
    fun itemAtendimentoById(id: Long): ItemAtendimento
    fun itemAtendimentoByAtendimentoId(atendimentoId: Long): List<ItemAtendimento>
    fun itemAtendimentoByServicoId(servicoId: Long): List<ItemAtendimento>
}