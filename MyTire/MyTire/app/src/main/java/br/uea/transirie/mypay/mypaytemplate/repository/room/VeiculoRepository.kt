package br.uea.transirie.mypay.mypaytemplate.repository.room

import br.uea.transirie.mypay.mypaytemplate.model.Veiculo
import br.uea.transirie.mypay.mypaytemplate.repository.VeiculoDataSource

class VeiculoRepository(database: AppDatabase): VeiculoDataSource {
    private val veiculoDao = database.veiculoDao()

    override fun veiculoById(id: Long): Veiculo {
        return veiculoDao.veiculoById(id)
    }

    override fun getAllVeiculos(): List<Veiculo>{
        return veiculoDao.getAllVeiculos()
    }

    override fun veiculoByClienteId(clienteId: Long): List<Veiculo> {
        return veiculoDao.veiculoByClienteId(clienteId)
    }

    override fun save(obj: Veiculo) {
        if(obj.id == 0L) {
            val id = insert(obj)
            obj.id = id
        } else {
            update(obj)
        }
    }

    override fun insert(obj: Veiculo): Long {
        return veiculoDao.insert(obj)
    }

    override fun update(obj: Veiculo) {
        return veiculoDao.update(obj)
    }

    override fun delete(obj: Veiculo) {
        return veiculoDao.delete(obj)
    }

}