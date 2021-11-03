package com.example.agendav4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.agendav4.databinding.ActivityHomeBinding
import com.example.agendav4.fragments.AjustesFragment
import com.example.agendav4.fragments.ListaContatosFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)

        inicializaLista()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, ListaContatosFragment())
            .commit()

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> {
                    loadFragments(ListaContatosFragment(), FRAGMENT_HOME)
                    true
                }
                R.id.ic_ajustes -> {
                    loadFragments(AjustesFragment(), FRAGMENT_AJUSTES)
                    true
                }
                else ->
                    false
            }
        }
        setContentView(binding.root)
    }

    private fun loadFragments(fragment: Fragment, tag: String){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragment, tag)
            commit()
        }
    }

    private fun inicializaLista(){
        Agenda.listaContatos.addAll(
            listOf(
                Contato("Natalina","00 00000 0000"),
                Contato("O-Kikunojo","00 00000 0000"),
                Contato("Mamãe","00 00000 0000"),
                Contato("Geise","00 00000 0000"),
                Contato("Maria","00 00000 0000"),
                Contato("Raiane","00 00000 0000"),
                Contato("Natan F","00 00000 0000"),
                Contato("Rayanna","00 00000 0000"),
                Contato("Mimiu","00 00000 0000"),
                Contato("Doido","00 00000 0000"),
                Contato("Henrique","00 00000 0000"),
                Contato("Cal","00 00000 0000"),
                Contato("Eduh","00 00000 0000"),
                Contato("Filipe","00 00000 0000"),
                Contato("Agnes","00 00000 0000"),
                Contato("Mary Hellen","00 00000 0000"),
                Contato("Aurianderson","00 00000 0000"),
                Contato("Bruna","00 00000 0000"),
                Contato("Nayara","00 00000 0000"),
                Contato("Ionara","00 00000 0000"),
            )
        )
    }

    companion object{
        private const val FRAGMENT_HOME = "FRAGMENT_HOME"
        private const val FRAGMENT_AJUSTES = "FRAGMENT_AJUSTES"
    }
}