package br.uea.transirie.mypay.mypaytemplate.model

import androidx.room.*
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.*

@Entity(
    tableName = TABLE_VEICULO,
    foreignKeys = [
        ForeignKey(
            entity = Cliente::class,
            parentColumns = [COLUMN_ID],
            childColumns = [COLUMN_CLIENTE_ID]
        )
    ]
)
data class Veiculo (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID, index = true) var id: Long = 0,
    @ColumnInfo(name = COLUMN_CLIENTE_ID) var clienteId: Long = 0,
    @ColumnInfo(name = COLUMN_PLACA) var placa: String = "",
    @ColumnInfo(name = COLUMN_CATEGORIA) var categoria: String = ""
)