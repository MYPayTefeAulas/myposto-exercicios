package br.uea.transirie.mypay.mypaytemplate.ui.abastecimento

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityChecaguemDeBombasBinding

class ChecaguemDeBombasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChecaguemDeBombasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChecaguemDeBombasBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}