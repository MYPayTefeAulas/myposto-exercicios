package br.uea.transirie.mypay.mypaytemplate.ui.caixa

import java.time.LocalDate

data class CaixaStatus(var status:Boolean, var data:LocalDate)
// false = caixa aberto, true = caixa fechado