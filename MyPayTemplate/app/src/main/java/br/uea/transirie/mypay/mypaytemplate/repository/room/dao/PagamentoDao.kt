package br.uea.transirie.mypay.mypaytemplate.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import br.uea.transirie.mypay.mypaytemplate.model.Pagamento
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.COLUMN_ID
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.TABLE_PAGAMENTO
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.COLUMN_ATENDIMENTO_ID

@Dao
interface PagamentoDao: BaseDao<Pagamento> {
    /**
     * Obtém todos os pagamentos cadastrados
     */
    @Query("""SELECT * FROM $TABLE_PAGAMENTO""")
    fun getAllPagamentos(): List<Pagamento>

    /**
    * Obtém um pagamento utilizando o id
    *
    * @param id é o id do pagamento a ser buscado
    * */
    @Query("""SELECT * FROM $TABLE_PAGAMENTO WHERE $COLUMN_ID = :id""")
    fun pagamentoById(id: Long): Pagamento

    @Query("""SELECT * FROM $TABLE_PAGAMENTO WHERE $COLUMN_ATENDIMENTO_ID = :atendimentoId""")
    fun pagamentoByAtendimentoId(atendimentoId: Long): List<Pagamento>

}