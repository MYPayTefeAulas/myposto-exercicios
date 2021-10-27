package com.example.agendaa_2_recyclerview

data class Contato(var nome: String, var telefone: String, var email: String)
{
    val id = getProximoId()
/* Declaração de um objeto dentro de uma class */
    companion object {
        var lastId = -1

        fun getProximoId(): Int {
            return lastId++
        }
    }
}

