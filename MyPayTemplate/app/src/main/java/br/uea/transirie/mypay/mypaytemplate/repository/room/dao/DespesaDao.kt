package br.uea.transirie.mypay.mypaytemplate.repository.room.dao

import androidx.room.Dao
import androidx.room.Query
import br.uea.transirie.mypay.mypaytemplate.model.Despesa
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.TABLE_DESPESA

@Dao
interface DespesaDao: BaseDao<Despesa> {

    @Query("""SELECT * FROM $TABLE_DESPESA""")
    fun getAllDespesa(): List<Despesa>
}