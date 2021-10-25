package com.example.agendaaplicacao

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.agendaaplicacao.databinding.ActivityTelaInicialBinding

class TelaInicialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTelaInicialBinding

    private lateinit var adapter: ArrayAdapter<Contato>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTelaInicialBinding.inflate(layoutInflater)

        Agenda.listaContatos.add(Contato("1 Rodrigo", "1111","rodrigo@uea.edu.br"))
        Agenda.listaContatos.add(Contato("2 Maria", "22222", "maria.uea.edu.br"))
        Agenda.listaContatos.add(Contato("3 Willian", "929144", "wramos@uea.edu.br"))
        Agenda.listaContatos.add(Contato("4 Thalia", "2426598","thalia@uea.edu.br"))

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, Agenda.listaContatos)
        binding.lvContatos.adapter = adapter
        binding.lvContatos.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, EditarContatoActivity::class.java)
            intent.putExtra("indiceContato", position)
            startActivity(intent)
        }

        setContentView(binding.root)
    }
    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}