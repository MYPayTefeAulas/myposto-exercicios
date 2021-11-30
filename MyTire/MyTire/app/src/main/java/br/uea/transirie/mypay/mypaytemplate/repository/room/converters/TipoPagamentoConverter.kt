package br.uea.transirie.mypay.mypaytemplate.repository.room.converters

import androidx.room.TypeConverter
import br.uea.transirie.mypay.mypaytemplate.model.TipoPagamento

class TipoPagamentoConverter {
    @TypeConverter
    fun fromTipoPagamento(tipoPagamento: TipoPagamento): String {
        return tipoPagamento.name
    }

    @TypeConverter
    fun toTipoPagamento(strTipoPagamento: String): TipoPagamento {
        return TipoPagamento.valueOf(strTipoPagamento)
    }
}