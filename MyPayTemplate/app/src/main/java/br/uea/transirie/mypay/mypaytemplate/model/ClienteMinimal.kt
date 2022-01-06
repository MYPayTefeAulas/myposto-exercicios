package br.uea.transirie.mypay.mypaytemplate.model

import androidx.room.ColumnInfo
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.COLUMN_ID
import br.uea.transirie.mypay.mypaytemplate.repository.sqlite.COLUMN_TELEFONE

/**
 * Esta classe é utilizada para simplificar o retorno de uma consulta por clientes. Ao invés de
 * retornar a classe completa, retorna apenas os atributos mais relevantes.
 * O nome das propriedades deve casar com o nome da classe que ela simplifica (nesse caso, Cliente)
 *
 */
data class ClienteMinimal(
    @ColumnInfo(name = COLUMN_ID, index = true) var id: Long = 0,
    @ColumnInfo(name = COLUMN_TELEFONE) var telefone: String = "",
)