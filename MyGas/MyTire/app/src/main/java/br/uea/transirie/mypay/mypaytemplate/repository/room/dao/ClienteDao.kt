package br.uea.transirie.mypay.mypaytemplate.repository.room.dao

//import br.uea.transirie.mypay.mypaytemplate.model.ClienteComAtendimentos
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import br.uea.transirie.mypay.mypaytemplate.model.Cliente
import br.uea.transirie.mypay.mypaytemplate.model.ClienteEVeiculo
import br.uea.transirie.mypay.mypaytemplate.model.ClienteMinimal
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.*

@Dao
interface ClienteDao: BaseDao<Cliente> {

    /**
     * Obtém um Cliente utilizando o id
     *
     * @param id é o id do Cliente a ser buscado
     */
    @Query("""SELECT * FROM $TABLE_CLIENTE WHERE $COLUMN_ID = :id""")
    fun clienteById(id: Long): Cliente

    /**
     * Obtém apenas os dados mínimos de um cliente através do id
     *
     * @param id é o id do Cliente a ser buscado
     */
    @Query("SELECT $COLUMN_ID, $COLUMN_TELEFONE FROM $TABLE_CLIENTE WHERE $COLUMN_ID = :id")
    fun clienteMinimalById(id: Long): ClienteMinimal

    /**
     * Obtém apenas os dados mínimos de todos os clientes para exibição em lista
     */
    @Query("SELECT $COLUMN_ID, $COLUMN_TELEFONE FROM $TABLE_CLIENTE")
    fun getClientesMinimal(): List<ClienteMinimal>

    /**
     * Obtém um objeto ClienteEVeiculo através do id de um atendimento
     *
     * @param atendimentoId é o id do atendimento
     */
    @Transaction
    @Query("SELECT $TABLE_CLIENTE.$COLUMN_ID, $TABLE_CLIENTE.$COLUMN_TELEFONE FROM $TABLE_CLIENTE " +
            "INNER JOIN $TABLE_VEICULO ON $TABLE_VEICULO.$COLUMN_CLIENTE_ID = $TABLE_CLIENTE.$COLUMN_ID " +
            "INNER JOIN $TABLE_ATENDIMENTO ON $TABLE_ATENDIMENTO.$COLUMN_VEICULO_ID = $TABLE_VEICULO.$COLUMN_ID " +
            "WHERE $TABLE_ATENDIMENTO.$COLUMN_ID = :atendimentoId"
    )
    fun clienteEVeiculoByAtendimentoId(atendimentoId: Long): ClienteEVeiculo

}