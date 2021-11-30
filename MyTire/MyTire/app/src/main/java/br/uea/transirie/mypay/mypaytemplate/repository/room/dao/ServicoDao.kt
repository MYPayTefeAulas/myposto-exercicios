package br.uea.transirie.mypay.mypaytemplate.repository.room.dao

import androidx.room.*
import br.uea.transirie.mypay.mypaytemplate.model.Servico
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.COLUMN_DESATIVADO
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.COLUMN_DESCRICAO
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.COLUMN_ID
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.TABLE_SERVICO

@Dao
interface ServicoDao: BaseDao<Servico> {

    /**
     * Obtém um Servico utilizando o id
     *
     * @param id o id do usuário a ser buscado
     */
    @Query("SELECT * FROM $TABLE_SERVICO WHERE $COLUMN_ID = :id")
    fun servicoById(id: Long): Servico

    /**
     * Busca um Servico utilizando a descrição (nome) dele
     *
     * @param query a string de busca com a descrição completa ou parcial
     */
    @Query("""SELECT * FROM $TABLE_SERVICO WHERE $COLUMN_DESCRICAO LIKE :query""")
    fun searchByDescricao(query: String): List<Servico>

    /**
     * Obtém todos os serviços cadastrados com base no atributo de desativado
     *
     *@param desativado serviço desativo ou ativado
     */
    @Query("""SELECT * FROM $TABLE_SERVICO WHERE $COLUMN_DESATIVADO = :desativado""")
    fun getAllServicos(desativado: Boolean): List<Servico>

    /**
     * Obtém todos os serviços ativos
     *
     */
    @Query("""SELECT * FROM $TABLE_SERVICO WHERE $COLUMN_DESATIVADO = 0""")
    fun getAllServicosAtivos(): List<Servico>
}