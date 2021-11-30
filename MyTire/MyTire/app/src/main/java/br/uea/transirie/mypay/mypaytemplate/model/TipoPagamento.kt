package br.uea.transirie.mypay.mypaytemplate.model

enum class TipoPagamento(val descricao: String) {
    CREDITO("Crédito"),
    DEBITO("Débito"),
    DINHEIRO("Dinheiro"),
    PIX("Pix")
}