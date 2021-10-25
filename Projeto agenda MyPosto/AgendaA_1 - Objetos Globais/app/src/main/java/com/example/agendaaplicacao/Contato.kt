package com.example.agendaaplicacao

data class Contato(var nome: String, var telefone: String, var email: String) {
    override fun toString(): String {
        return "$nome ($telefone) ($email)"
    }
}
