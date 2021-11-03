package com.example.agenda_5_toolbarcombusca

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.agenda_5_toolbarcombusca.databinding.FragmentAjustesBinding
import com.example.agenda_5_toolbarcombusca.databinding.FragmetAjustesBinding

class AjustesFragmet: Fragment() {
    private var _binding: FragmetAjustesBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmetAjustesBinding.inflate(inflater, container, false)

        val config = requireActivity().getSharedPreferences("configuracoes", 0)

        binding.switch1.isChecked = config.getBoolean("listaContatosAlfabetico", false)
        binding.switch1.setOnCheckedChangeListener { compoundButton, b ->
            val editor = config.edit()
            editor.putBoolean("listaContatosAlfabetico", compoundButton.isChecked)
            editor.apply()
        }
        return binding.root
    }

}