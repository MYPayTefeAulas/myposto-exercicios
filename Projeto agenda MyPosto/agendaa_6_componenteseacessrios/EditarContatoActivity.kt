package com.example.agendaa_6_componenteseacessrios

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.agendaa_6_componenteseacessrios.Utils.IntentsConstants
import com.example.agendaa_6_componenteseacessrios.databinding.ActivityEditarContatoBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EditarContatoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarContatoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditarContatoBinding.inflate(layoutInflater)

        setTitle("editar_contato")

        val indiceContato = intent.getIntExtra(IntentsConstants.INT_INDICE_CONTATO, -1)

        val nome: String = Agenda.listaContatos[indiceContato].nome
        val telefone: String = Agenda.listaContatos[indiceContato].telefone
        val email: String = Agenda.listaContatos[indiceContato].email
        binding.agendaTxtTelefone.setText(telefone)
        binding.agendaTxtNome.setText(nome)
        binding.agendaTxtEmail.setText(email)
        binding.switchContatoFavorito.isChecked = Agenda.listaContatos[indiceContato].favorito

        binding.agendaBtSalvar.setOnClickListener {
            Agenda.listaContatos[indiceContato].nome = binding.agendaTxtNome.text.toString()
            Agenda.listaContatos[indiceContato].telefone = binding.agendaTxtTelefone.text.toString()
            Toast.makeText(this, "contato salvo com Sucesso", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btDeletar.setOnClickListener {
            /* Não rodou usando as orientaçoes 1b! */
            val dialog = MaterialAlertDialogBuilder(this)
                .setTitle("deletar_contato")
                .setMessage("realmente_deletar")
                .setNegativeButton("cancelar", null)
                .setPositiveButton("deletar") { _, _ ->
                    Agenda.listaContatos.removeAt(indiceContato)
                    Toast.makeText(this,"contato removido com Sucesso", Toast.LENGTH_SHORT).show()
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