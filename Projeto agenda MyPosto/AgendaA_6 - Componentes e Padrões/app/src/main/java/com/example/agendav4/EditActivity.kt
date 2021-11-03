package com.example.agendav4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.agendav4.databinding.ActivityEditBinding
import com.example.agendav4.utils.IntentsConstants
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater)

        setTitle(getString(R.string.editar_contato))

        val indiceContato = intent.getIntExtra(IntentsConstants.INT_INDICE_CONTATO, -1)

        val nome: String = Agenda.listaContatos[indiceContato].nome
        val telefone: String = Agenda.listaContatos[indiceContato].telefone
        binding.agendaTxtTelefone.setText(telefone)
        binding.agendaTxtNome.setText(nome)
        binding.switchContatoFavorito.isChecked = Agenda.listaContatos[indiceContato].favorito

        binding.agendaBtSalvar.setOnClickListener {
            Agenda.listaContatos[indiceContato].nome = binding.agendaTxtNome.text.toString()
            Agenda.listaContatos[indiceContato].telefone = binding.agendaTxtTelefone.text.toString()
            Toast.makeText(this, getString(R.string.contato_salvo), Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btDeletar.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.excluir_contato))
                .setMessage(getString(R.string.realmente_excluir))
                .setNegativeButton(getString(R.string.cancelar), null)
                .setPositiveButton(getString(R.string.excluir)) { _, _ ->
                    Agenda.listaContatos.removeAt(indiceContato)
                    Toast.makeText(this,getString(R.string.contato_excluido), Toast.LENGTH_SHORT).show()
                    finish()
                }
            dialog.show()
        }

        binding.switchContatoFavorito.setOnCheckedChangeListener { _, isChecked ->
            Agenda.listaContatos[indiceContato].favorito = isChecked
        }

        setContentView(binding.root)
    }
}