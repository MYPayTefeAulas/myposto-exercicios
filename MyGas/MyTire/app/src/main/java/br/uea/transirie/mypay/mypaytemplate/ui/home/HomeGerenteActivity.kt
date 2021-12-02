package br.uea.transirie.mypay.mypaytemplate.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.ui.caixa.CaixaStatus
import br.uea.transirie.mypay.mypaytemplate.ui.fragments.AjustesFragment
import br.uea.transirie.mypay.mypaytemplate.ui.fragments.CaixaFragment
import br.uea.transirie.mypay.mypaytemplate.ui.fragments.HistoricoAtendimentosFragment
import br.uea.transirie.mypay.mypaytemplate.ui.fragments.HomeFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import java.time.LocalDate

class HomeGerenteActivity : AppCompatActivity() {

    companion object {
        private const val FRAGMENT_HOME = "FRAGMENT_HOME"
        private const val FRAGMENT_CAIXA = "FRAGMENT_CAIXA"
        private const val FRAGMENT_HISTORICO = "FRAGMENT_HISTORICO"
        private const val FRAGMENT_AJUSTES = "FRAGMENT_AJUSTES"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        loadFragments(HomeFragment(), FRAGMENT_HOME)

        val dataHoje = LocalDate.now()
        // resgata usuário que está logado
        val userPrefs = getSharedPreferences(getString(R.string.PREF_USER_DATA), Context.MODE_PRIVATE)
        val usuario = userPrefs.getString(getString(R.string.PREF_USER_ESTABELECIMENTO_CNPJ),"").toString()
        val edit = userPrefs.edit()
        val statusCaixa = Gson().fromJson(userPrefs.getString(usuario, Gson().toJson(CaixaStatus(status = false, data = LocalDate.now()))),
            CaixaStatus::class.java)
        Log.i("statusCaixa", statusCaixa.status.toString())
        if (statusCaixa.data.dayOfMonth == dataHoje.dayOfMonth &&
            statusCaixa.data.monthValue == dataHoje.monthValue &&
            statusCaixa.data.year == dataHoje.year &&
            statusCaixa.status){
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@HomeGerenteActivity)
            builder.setMessage("Você já encerrou suas atividades do dia. Tem certeza que quer continuar?")
            builder.setPositiveButton("SIM"){dialog,_->
                statusCaixa.status = false
                edit.putString(usuario, Gson().toJson(statusCaixa))
                edit.apply()
                dialog.dismiss()
            }
            builder.setNegativeButton("CANCELAR"){dialog,_->
                finishAffinity()
                startActivity(Intent(this@HomeGerenteActivity, EntradaActivity::class.java))
                edit.putBoolean(getString(R.string.PREF_USER_LOGADO),false)
                edit.apply()
                dialog.dismiss()
            }
            builder.show()
        }

        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> {
                    loadFragments(HomeFragment(), FRAGMENT_HOME)
                    true
                }
                R.id.ic_caixa -> {
                    loadFragments(CaixaFragment(), FRAGMENT_CAIXA)
                    true
                }
                R.id.ic_historico -> {
                    loadFragments(HistoricoAtendimentosFragment(), FRAGMENT_HISTORICO)
                    true
                }
                R.id.ic_ajustes -> {
                    loadFragments(AjustesFragment(), FRAGMENT_AJUSTES)
                    true
                }
                else -> false
            }
        }
    }

    //Carrega os fragments e os empilha
    private fun loadFragments(fragment: Fragment, tag: String){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragment, tag)
            addToBackStack(null)
            commit()
        }
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        val fragmentHome = fragmentManager.findFragmentByTag(FRAGMENT_HOME)

        if (fragmentHome != null) {
            if (fragmentHome.isVisible) {
                finish()
                return
            }
        }
        super.onBackPressed()
        fragmentManager.popBackStack()
        selectorIcons(fragmentManager)
    }

    //Gerencia o icon do bottomNavigation, quando onBackPressed() for apertado
    private fun selectorIcons(fragmentManager: FragmentManager) {
        val fragmentHome = fragmentManager.findFragmentByTag(FRAGMENT_HOME)
        if(fragmentHome != null){
            if (fragmentHome.isVisible){
                bottomNavigation.selectedItemId = R.id.ic_home
            }
        }

        val fragmentCaixa = fragmentManager.findFragmentByTag(FRAGMENT_CAIXA)
        if(fragmentCaixa != null){
            if (fragmentCaixa.isVisible){
                bottomNavigation.selectedItemId = R.id.ic_caixa
            }
        }

        val fragmentHistorico = fragmentManager.findFragmentByTag(FRAGMENT_HISTORICO)
        if(fragmentHistorico != null){
            if (fragmentHistorico.isVisible){
                bottomNavigation.selectedItemId = R.id.ic_historico
            }
        }

        val fragmentAjustes = fragmentManager.findFragmentByTag(FRAGMENT_AJUSTES)
        if(fragmentAjustes != null) {
            if (fragmentAjustes.isVisible) {
                bottomNavigation.selectedItemId = R.id.ic_ajustes
            }
        }
    }
}



