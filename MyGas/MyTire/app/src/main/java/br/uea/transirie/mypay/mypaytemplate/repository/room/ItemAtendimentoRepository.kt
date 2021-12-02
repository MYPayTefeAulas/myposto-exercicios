package br.uea.transirie.mypay.mypaytemplate.repository.room

import br.uea.transirie.mypay.mypaytemplate.repository.ItemAtendimentoDataSource
import br.uea.transirie.mypay.mypaytemplate.model.ItemAtendimento

class ItemAtendimentoRepository(database: AppDatabase) : ItemAtendimentoDataSource {
    private val itemAtendimentoDao = database.itemAtendimentoDao()
    override fun itemAtendimentoById(id: Long): ItemAtendimento {
        return itemAtendimentoDao.itemAtendimentoById(id)
    }

    override fun itemAtendimentoByAtendimentoId(atendimentoId: Long): List<ItemAtendimento> {
        return itemAtendimentoDao.itemAtendimentoByAtendimentoId(atendimentoId)
    }

    override fun itemAtendimentoByServicoId(servicoId: Long): List<ItemAtendimento> {
        return itemAtendimentoDao.itemAtendimentoByServicoId(servicoId)
    }

    override fun save(obj: ItemAtendimento) {
        if(obj.id == 0L) {
            val id = insert(obj)
            obj.id = id
        } else {
            update(obj)
        }
    }

    override fun insert(obj: ItemAtendimento): Long {
        return itemAtendimentoDao.insert(obj)
    }

    override fun update(obj: ItemAtendimento) {
        itemAtendimentoDao.update(obj)
    }

    override fun delete(obj: ItemAtendimento) {
        itemAtendimentoDao.delete(obj)
    }

}