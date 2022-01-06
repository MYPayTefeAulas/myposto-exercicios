package br.uea.transirie.mypay.mypaytemplate.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = TABLE_USUARIO
)
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID, index = true) var id: Long = 0,
    @ColumnInfo(name = COLUMN_ESTABELECIMENTO_CNPJ) var estabelecimentoCNPJ: String = "",
    @ColumnInfo(name = COLUMN_PIN) var pin: Int = 0,
    @ColumnInfo(name = COLUMN_NOME) var nome: String = "",
    @ColumnInfo(name = COLUMN_EMAIL) var email: String = "",
    @ColumnInfo(name = COLUMN_TELEFONE) var telefone: String = "",
    @ColumnInfo(name = COLUMN_IS_GERENTE) var isGerente: Boolean = false
): Parcelable