package com.example.agenda_5_toolbarcombusca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.agenda_5_toolbarcombusca.databinding.ActivityEditarContatoBinding

class EditarContatoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarContatoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditarContatoBinding.inflate(layoutInflater)

        setTitle("Editar Contato")

        val indiceContato = intent.getIntExtra("indiceContato", -1)

        val nome: String = Agenda.listaContatos[indiceContato].nome
        val telefone: String = Agenda.listaContatos[indiceContato].telefone
        val email: String = Agenda.listaContatos[indiceContato].email
        binding.agendaTxtTelefone.setText(telefone)
        binding.agendaTxtNome.setText(nome)
        binding.agendaTxtEmail.setText(email)

        binding.agendaBtSalvar.setOnClickListener {
            Agenda.listaContatos[indiceContato].nome = binding.agendaTxtNome.text.toString()
            Agenda.listaContatos[indiceContato].telefone = binding.agendaTxtTelefone.text.toString()
            Agenda.listaContatos[indiceContato].email = binding.agendaTxtEmail.text.toString()
            Toast.makeText(this, "Contato Salvo com Sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        }

        setContentView(binding.root)
    }
}