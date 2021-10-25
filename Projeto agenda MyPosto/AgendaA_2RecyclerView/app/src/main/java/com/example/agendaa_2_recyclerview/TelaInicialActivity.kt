package com.example.agendaa_2_recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agendaa_2_recyclerview.databinding.ActivityTelaInicialBinding

class TelaInicialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTelaInicialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTelaInicialBinding.inflate(layoutInflater)

        incializaLista()
        binding.rvContatos.layoutManager = LinearLayoutManager(this)
        binding.rvContatos.adapter = ContatosAdapter(Agenda.listaContatos)
        binding.rvContatos.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        setContentView(binding.root)
    }

    private fun incializaLista() {
        Agenda.listaContatos.addAll(
            listOf(
                Contato("1 Rodrigo", "1111","rodrigo@uea.edu.br"),
                Contato("2 Willian", "22222", "wramos@uea.edu.br"),
                Contato("3 Raiane", "333333", "raiane@uea.edu.br"),

            )
        )
    }
}