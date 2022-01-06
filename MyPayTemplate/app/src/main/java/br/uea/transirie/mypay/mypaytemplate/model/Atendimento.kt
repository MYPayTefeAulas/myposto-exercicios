package br.uea.transirie.mypay.mypaytemplate.model

import androidx.room.*
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.*

@Entity(tableName = TABLE_ATENDIMENTO,
    foreignKeys = [
        ForeignKey(entity = Veiculo::class,
                   parentColumns = [COLUMN_ID],
                   childColumns = [COLUMN_VEICULO_ID])
    ])
data class Atendimento (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID, index = true) var id: Long = 0,
    @ColumnInfo(name = COLUMN_VEICULO_ID) var veiculoId: Long = 0,
    @ColumnInfo(name = COLUMN_DATA_RECEBIMENTO) var dataRecebimento: String = "",
    @ColumnInfo(name = COLUMN_VALOR_TOTAL) var valorTotal: Double = 0.0
)