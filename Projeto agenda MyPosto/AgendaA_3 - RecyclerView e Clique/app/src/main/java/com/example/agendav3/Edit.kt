package com.example.agendav3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.agendav3.databinding.ActivityEditBinding

class Edit : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater)

        setTitle("Editar Contato")

        val indiceContato = intent.getIntExtra("indiceContato", -1)

        val nome: String = Agenda.listaContatos[indiceContato].nome
        val telefone: String = Agenda.listaContatos[indiceContato].telefone
        binding.txtNome.setText(telefone)
        binding.txtTelefone.setText(nome)

        binding.btSalvar.setOnClickListener {
            Agenda.listaContatos[indiceContato].nome = binding.txtNome.text.toString()
            Agenda.listaContatos[indiceContato].telefone = binding.txtTelefone.text.toString()
            Toast.makeText(this, "Contato salvo!", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btCancelar.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }


        setContentView(binding.root)
    }
}