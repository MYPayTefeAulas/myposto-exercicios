package br.uea.transirie.mypay.mypaytemplate.ui.caixa

import java.time.LocalDate

data class AbrirCaixaStatus (var status:Boolean, var data: LocalDate, var valor: Float)