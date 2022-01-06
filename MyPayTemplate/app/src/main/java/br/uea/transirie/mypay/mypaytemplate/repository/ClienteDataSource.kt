package br.uea.transirie.mypay.mypaytemplate.repository

import br.uea.transirie.mypay.mypaytemplate.model.Cliente
import br.uea.transirie.mypay.mypaytemplate.model.ClienteEVeiculo
import br.uea.transirie.mypay.mypaytemplate.model.ClienteMinimal

interface ClienteDataSource: BaseDataSource<Cliente> {
    fun clienteById(id: Long): Cliente
    fun getClientesMinimal(): List<ClienteMinimal>
    fun clienteMinimalById(id: Long): ClienteMinimal
    fun clienteEVeiculoByAtendimentoId(atendimentoId: Long): ClienteEVeiculo
}