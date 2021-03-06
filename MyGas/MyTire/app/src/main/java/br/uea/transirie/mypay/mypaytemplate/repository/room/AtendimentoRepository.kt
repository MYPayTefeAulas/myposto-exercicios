package br.uea.transirie.mypay.mypaytemplate.repository.room

import br.uea.transirie.mypay.mypaytemplate.repository.AtendimentoDataSource
import br.uea.transirie.mypay.mypaytemplate.model.Atendimento

class AtendimentoRepository(database: AppDatabase): AtendimentoDataSource {
    private val atendimentoDao = database.atendimentoDao()

    override fun atendimentoById(id: Long): Atendimento {
        return atendimentoDao.atendimentoById(id)
    }

    override fun atendimentoByVeiculoId(veiculoId: Long): List<Atendimento> {
        return atendimentoDao.atendimentoByVeiculoId(veiculoId)
    }

    override fun save(obj: Atendimento) {
        if(obj.id == 0L) {
            val id = insert(obj)
            obj.id = id
        } else {
            update(obj)
        }
    }

    override fun insert(obj: Atendimento): Long {
        return atendimentoDao.insert(obj)
    }

    override fun update(obj: Atendimento) {
        atendimentoDao.update(obj)
    }

    override fun delete(obj: Atendimento) {
        atendimentoDao.delete(obj)
    }

    fun getAllAtendimentos(): List<Atendimento> {
        return atendimentoDao.getAllAtendimentos()
    }
}