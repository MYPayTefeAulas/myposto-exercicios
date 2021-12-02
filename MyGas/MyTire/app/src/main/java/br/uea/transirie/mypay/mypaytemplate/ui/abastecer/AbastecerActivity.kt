package br.uea.transirie.mypay.mypaytemplate.ui.abastecer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityAbastecerBinding

class AbastecerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAbastecerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAbastecerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}