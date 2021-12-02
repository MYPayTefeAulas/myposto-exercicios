package br.uea.transirie.mypay.mypaytemplate.repository

import br.uea.transirie.mypay.mypaytemplate.model.Veiculo

interface VeiculoDataSource: BaseDataSource<Veiculo> {
    fun veiculoById(id: Long): Veiculo
    fun veiculoByClienteId(clienteId: Long): List<Veiculo>
    fun getAllVeiculos(): List<Veiculo>
}