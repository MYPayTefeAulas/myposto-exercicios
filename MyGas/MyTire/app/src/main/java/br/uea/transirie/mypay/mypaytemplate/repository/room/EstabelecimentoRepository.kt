package br.uea.transirie.mypay.mypaytemplate.repository.room

import br.uea.transirie.mypay.mypaytemplate.model.Estabelecimento
import br.uea.transirie.mypay.mypaytemplate.repository.EstabelecimentoDataSource

class EstabelecimentoRepository(database: AppDatabase): EstabelecimentoDataSource {
    private val estabelecimentoDao = database.estabelecimentoDao()

    //Função que recebe o valor do id do estabelecimento
    override fun estabelecimentoId(): Long =
        estabelecimentoDao.estabelecimentoIds()

    override fun estabelecimentoById(id: Long): Estabelecimento =
        estabelecimentoDao.estabelecimentoById(id)

    override fun estabelecimentoByCNPJ(cnpj: String): Estabelecimento? =
        estabelecimentoDao.estabelecimentoByCNPJ(cnpj)

    override fun getEstabelecimentos(): List<Estabelecimento> =
        estabelecimentoDao.getEstabelecimentos()

    override fun cnpjJaEmUso(cnpj: String): Boolean =
        estabelecimentoDao.cnpjJaEmUso(cnpj)

    override fun save(obj: Estabelecimento) {
        if(obj.id == 0L){
            val id = insert(obj)
            obj.id = id
        } else{
            update(obj)
        }
    }

    override fun insert(obj: Estabelecimento): Long {
        return estabelecimentoDao.insert(obj)
    }

    override fun update(obj: Estabelecimento) {
        return estabelecimentoDao.update(obj)
    }

    override fun delete(obj: Estabelecimento) {
        return estabelecimentoDao.delete(obj)
    }

    fun getAllEstabelecimentos(): List<Estabelecimento> {
        return estabelecimentoDao.getAllEstabelecimentos()
    }
}