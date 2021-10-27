package com.example.agendav3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agendav3.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: ContatosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)

        inicializaLista()

        adapter = ContatosAdapter(mutableListOf(), ::onBtEditClick)

        binding.rvContatos.layoutManager = LinearLayoutManager(this)
        binding.rvContatos.adapter = adapter
        binding.rvContatos.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        adapter.swapData(Agenda.listaContatos)

        setContentView(binding.root)
    }

    private fun inicializaLista() {
        Agenda.listaContatos.addAll(
            listOf(
                Contato("O-Kiku","00 00000 0000"),
                Contato("Natalina","00 00000 0000"),
                Contato("Mamãe","00 00000 0000"),
                Contato("Maria","00 00000 0000"),
                Contato("Raiane","00 00000 0000"),
                Contato("Geise","00 00000 0000"),
                Contato("Mary Hellen","00 00000 0000"),
                Contato("Agnes","00 00000 0000"),
                Contato("Natan F","00 00000 0000"),
                Contato("Filipe Y","00 00000 0000"),
                Contato("Larissa","00 00000 0000"),
                Contato("LarissaBio","00 00000 0000"),
                Contato("Matheus","00 00000 0000"),
                Contato("Bebel","00 00000 0000"),
                Contato("Kaka","00 00000 0000"),
                Contato("Madrinha","00 00000 0000"),
                Contato("Cleia","00 00000 0000"),
                Contato("Cintia","00 00000 0000"),
                Contato("Henrique","00 00000 0000"),
                Contato("Cal","00 00000 0000"),
            )
        )
    }

    fun onBtEditClick(indiceLista: Int) {
        val intent = Intent(this, Edit::class.java)
        intent.putExtra("indiceContato", indiceLista)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        adapter.swapData(Agenda.listaContatos)
    }
}