package br.uea.transirie.mypay.mypaytemplate.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import br.uea.transirie.mypay.mypaytemplate.model.Atendimento
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.COLUMN_ID
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.COLUMN_VEICULO_ID
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.TABLE_ATENDIMENTO

@Dao
interface AtendimentoDao: BaseDao<Atendimento> {

    /**
     * Obtém todos os atendimento cadastrados
     *
     */
    @Query("""SELECT * FROM $TABLE_ATENDIMENTO""")
    fun getAllAtendimentos(): List<Atendimento>

    /**
    * Obtém um Atendimento utilizando o id
    *
    * @param id id do atendimento a ser buscado
    */
    @Query("""SELECT * FROM $TABLE_ATENDIMENTO WHERE $COLUMN_ID = :id""")
    fun atendimentoById(id: Long): Atendimento

    /**
     * Obtém todos os atendimento associados a um veículo
     *
     * @param veiculoId o id do veículo onde o atendimento foi feito
     */
    @Query("""SELECT * FROM $TABLE_ATENDIMENTO WHERE $COLUMN_VEICULO_ID = :veiculoId""")
    fun atendimentoByVeiculoId(veiculoId: Long): List<Atendimento>

}