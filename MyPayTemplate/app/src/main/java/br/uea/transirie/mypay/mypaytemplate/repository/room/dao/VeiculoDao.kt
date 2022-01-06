package br.uea.transirie.mypay.mypaytemplate.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import br.uea.transirie.mypay.mypaytemplate.model.Veiculo
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.COLUMN_CLIENTE_ID
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.COLUMN_ID
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.TABLE_VEICULO

@Dao
interface VeiculoDao: BaseDao<Veiculo> {

    /**
     * Obtém todos os veiculos cadastrados
     *
     */
    @Query("""SELECT * FROM $TABLE_VEICULO""")
    fun getAllVeiculos(): List<Veiculo>

    /**
     * Obtém um Veiculo utilizando o id
     *
     * @param id é o id do veiculo a ser buscado
     */
    @Query("""SELECT * FROM $TABLE_VEICULO WHERE $COLUMN_ID = :id""")
    fun veiculoById(id: Long): Veiculo

    /**
     * Obtém os veículos associados a um cliente
     *
     * @param clienteId é o id do cliente dono do veículo
     */
    @Query("""SELECT * FROM $TABLE_VEICULO WHERE $COLUMN_CLIENTE_ID = :clienteId""")
    fun veiculoByClienteId(clienteId: Long): List<Veiculo>
}