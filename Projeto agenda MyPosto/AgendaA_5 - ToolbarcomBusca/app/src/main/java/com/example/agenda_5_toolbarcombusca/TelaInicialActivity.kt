package com.example.agenda_5_toolbarcombusca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.agenda_5_toolbarcombusca.databinding.ActivityTelaInicialBinding

class TelaInicialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTelaInicialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTelaInicialBinding.inflate(layoutInflater)

        inicializaLista()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, ListaContatosFragmet())
            .commit()

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.ic_home -> {
                    loadFragments(ListaContatosMelhoradaFragmet(), FRAGMENT_HOME)
                    true
                }
                R.id.ic_ajustes -> {
                    loadFragments(AjustesFragmet(), FRAGMENT_AJUSTES)
                    true
                }
                else ->
                    false
            }
        }

        setContentView(binding.root)
    }

    //Carrega os fragments e os empilha
    private fun loadFragments(fragment: Fragment, tag: String){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragment, tag)
            commit()
        }
    }

    private fun inicializaLista() {
        Agenda.listaContatos.addAll(
            listOf(
                Contato("Genival Lima", "12345","rodrigo@uea.edu.br"),
                Contato("Willian", "12345", "wramos@uea.edu.br"),
                Contato("Raiane", "12345", "raiane@uea.edu.br"),
                Contato("Maria", "33333", "maria@uea.edu.br"),
            )
        )
    }

    companion object {
        private const val FRAGMENT_HOME = "FRAGMENT_HOME"
        private const val FRAGMENT_AJUSTES = "FRAGMENT_AJUSTES"
    }
}