package br.uea.transirie.mypay.mypaytemplate.model

import androidx.room.Embedded

data class PagamentoClienteEVeiculo(
    @Embedded val pagamento: Pagamento,
    @Embedded val cev: ClienteEVeiculo
)
