package br.uea.transirie.mypay.mypaytemplate.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityAjudaBinding

class AjudaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAjudaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAjudaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        title = "Ajuda para o Aplicativo"

        //Inclui o botão voltar na ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.txtAjuda.setOnClickListener {
            val builder: androidx.appcompat.app.AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setMessage("Email: faleconosco@transire.com")
            builder.setPositiveButton("OK"){dialog,_->
                dialog.dismiss()
            }
            builder.show()
        }
    }

    //Função que permite retorno à tela anterior através do botão voltar na ActionBar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}