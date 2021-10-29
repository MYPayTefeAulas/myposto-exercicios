package com.example.agenda_4_menuinferiorcomfragmentos

data class Contato(var nome: String, var telefone: String, var email: String){
    val id = getProximoId()
    companion object{
        var lastId = -1
    }
    fun getProximoId(): Int{
        return lastId++
    }
}
