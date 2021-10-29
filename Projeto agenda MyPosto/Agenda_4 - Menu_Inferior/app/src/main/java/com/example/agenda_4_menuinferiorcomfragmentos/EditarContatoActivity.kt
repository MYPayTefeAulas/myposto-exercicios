package com.example.agenda_4_menuinferiorcomfragmentos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.agenda_4_menuinferiorcomfragmentos.databinding.ActivityEditarContatoBinding

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
        binding.txtTelefone.setText(telefone)
        binding.txtNome.setText(nome)
        binding.txtEmail.setText(email)

        binding.btSalvar.setOnClickListener {
            Agenda.listaContatos[indiceContato].nome = binding.txtNome.text.toString()
            Agenda.listaContatos[indiceContato].telefone = binding.txtTelefone.text.toString()
            Agenda.listaContatos[indiceContato].email = binding.txtEmail.text.toString()
            Toast.makeText(this, "Contato Salvo com Sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        }

        setContentView(binding.root)
    }
}