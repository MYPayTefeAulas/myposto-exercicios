package br.uea.transirie.mypay.mypaytemplate.model

import androidx.room.Embedded
import androidx.room.Relation

data class ClienteEVeiculo(
    @Embedded val cliente: ClienteMinimal,
    @Relation(
        parentColumn = "_id",
        entityColumn = "clienteId"
    )
    val veiculo: Veiculo
)
