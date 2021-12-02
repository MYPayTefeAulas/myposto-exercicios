package br.uea.transirie.mypay.mypaytemplate.repository.room

import br.uea.transirie.mypay.mypaytemplate.model.Cliente
import br.uea.transirie.mypay.mypaytemplate.model.ClienteEVeiculo
import br.uea.transirie.mypay.mypaytemplate.model.ClienteMinimal
import br.uea.transirie.mypay.mypaytemplate.repository.ClienteDataSource

class ClienteRepository(database: AppDatabase) : ClienteDataSource {
    private val clienteDao = database.clienteDao()

    override fun clienteById(id: Long): Cliente {
        return clienteDao.clienteById(id)
    }

    override fun clienteMinimalById(id: Long): ClienteMinimal {
        return clienteDao.clienteMinimalById(id)
    }

    override fun clienteEVeiculoByAtendimentoId(atendimentoId: Long): ClienteEVeiculo {
        return clienteDao.clienteEVeiculoByAtendimentoId(atendimentoId)
    }

    override fun save(obj: Cliente) {
        // se id == 0 significa que foi instanciado com o valor padrão
        if (obj.id == 0L) {
            val id = insert(obj)
            obj.id = id
        } else {
            update(obj)
        }
    }

    override fun insert(obj: Cliente): Long {
        return clienteDao.insert(obj)
    }

    override fun update(obj: Cliente) {
        return clienteDao.update(obj)
    }

    override fun delete(obj: Cliente) {
        return clienteDao.delete(obj)
    }

    override fun getClientesMinimal(): List<ClienteMinimal> {
        return clienteDao.getClientesMinimal()
    }

    /*override fun getClienteComAtendimentosById(id: Long): List<ClienteComAtendimentos> {
        return clienteDao.getClienteComAtendimentosById(id)
    }*/
}