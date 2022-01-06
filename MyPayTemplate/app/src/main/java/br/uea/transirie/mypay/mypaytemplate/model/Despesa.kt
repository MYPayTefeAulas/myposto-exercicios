package br.uea.transirie.mypay.mypaytemplate.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.*

@Entity(tableName = TABLE_DESPESA)
class Despesa (@PrimaryKey(autoGenerate = true)
               @ColumnInfo(name = COLUMN_ID) var id: Long = 0,
               @ColumnInfo(name = COLUMN_ESTABELECIMENTO_CNPJ) var estabelecimentoCNPJ:String = "",
               @ColumnInfo(name = COLUMN_DATA_PAGAMENTO) var dataPagamento: String?,
               @ColumnInfo(name = COLUMN_VALOR) var valor: Float = 0f,
               @ColumnInfo(name = COLUMN_TIPO_PAGAMENTO) var tipoPagamento: TipoPagamento
)