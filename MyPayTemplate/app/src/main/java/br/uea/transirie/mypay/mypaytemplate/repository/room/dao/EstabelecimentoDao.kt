package br.uea.transirie.mypay.mypaytemplate.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import br.uea.transirie.mypay.mypaytemplate.model.Estabelecimento
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.*

@Dao
interface EstabelecimentoDao: BaseDao<Estabelecimento>{

    @Query("""SELECT * FROM $TABLE_ESTABELECIMENTO WHERE $COLUMN_ID = :id""")
    fun estabelecimentoById(id: Long): Estabelecimento

    @Query("""SELECT * FROM $TABLE_ESTABELECIMENTO WHERE $COLUMN_CNPJ = :cnpj""")
    fun estabelecimentoByCNPJ(cnpj: String): Estabelecimento?

    @Query("""SELECT * FROM $TABLE_ESTABELECIMENTO""")
    fun getEstabelecimentos(): List<Estabelecimento>

    @Query("SELECT * FROM $TABLE_ESTABELECIMENTO")
    fun getAllEstabelecimentos(): List<Estabelecimento>

    //Faz a consulta do id na tabela
    @Query("SELECT _id FROM $TABLE_ESTABELECIMENTO LIMIT 1")
    fun estabelecimentoIds(): Long

    /**
     * Retorna true caso haja um estabelecimento cadastrado com o cnpj informado
     * @param cnpj o cnpj informado
     */
    @Query("SELECT 1 FROM $TABLE_ESTABELECIMENTO WHERE $COLUMN_CNPJ = :cnpj")
    fun cnpjJaEmUso(cnpj: String): Boolean
}