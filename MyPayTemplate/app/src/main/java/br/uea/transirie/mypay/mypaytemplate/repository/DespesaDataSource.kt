package br.uea.transirie.mypay.mypaytemplate.repository

import br.uea.transirie.mypay.mypaytemplate.model.Despesa


interface DespesaDataSource: BaseDataSource<Despesa> {
    fun getAllDespesa(): List<Despesa>
}