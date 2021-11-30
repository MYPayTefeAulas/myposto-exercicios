package br.uea.transirie.mypay.mypaytemplate.model

import androidx.room.Embedded

data class AtendimentoClienteEVeiculo(
    val atendimentoId: Long,
    @Embedded val cev: ClienteEVeiculo
)
