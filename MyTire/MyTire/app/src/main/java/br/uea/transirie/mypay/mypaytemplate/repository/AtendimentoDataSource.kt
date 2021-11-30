package br.uea.transirie.mypay.mypaytemplate.repository

import br.uea.transirie.mypay.mypaytemplate.model.Atendimento

interface AtendimentoDataSource: BaseDataSource<Atendimento> {
    fun atendimentoById(id: Long): Atendimento
    fun atendimentoByVeiculoId(veiculoId: Long): List<Atendimento>
}