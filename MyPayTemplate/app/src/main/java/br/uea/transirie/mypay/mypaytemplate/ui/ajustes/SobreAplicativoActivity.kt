package br.uea.transirie.mypay.mypaytemplate.ui.ajustes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.uea.transirie.mypay.mypaytemplate.R
import kotlinx.android.synthetic.main.activity_sobre_aplicativo.*

class SobreAplicativoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sobre_aplicativo)

        //set back button
        sobre_toolBar.setNavigationOnClickListener { onBackPressed() }
    }

}