package com.example.agendav4.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.agendav4.databinding.FragmentAjustesBinding
import com.example.agendav4.enums.TipoOrdenacao
import com.example.agendav4.utils.PrefsConstants

class AjustesFragment: Fragment() {
    private var _binding: FragmentAjustesBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAjustesBinding.inflate(inflater, container, false)
        val view = binding.root

        val config = requireActivity().getSharedPreferences(PrefsConstants.FILE_CONFIGURACOES,0)

        val ordenacaoSelecionada_str = config.getString(
            PrefsConstants.KEY_TIPO_ORDENACAO_CONTATOS,
            TipoOrdenacao.ORDEM_INCERCAO.toString()
        )
        val ordenacaoSelecionada: TipoOrdenacao = TipoOrdenacao.valueOf(ordenacaoSelecionada_str!!)
        when(ordenacaoSelecionada){
            TipoOrdenacao.ALFABEICA_AZ -> binding.radioOrdenacaoAZ.isChecked = true
            TipoOrdenacao.ALFABETICA_ZA -> binding.radioOrdenacaoZA.isChecked = true
            TipoOrdenacao.ORDEM_INCERCAO -> binding.radioOrdenacaoInsercao.isChecked = true
        }

        binding.radioGroupOrdenacao.setOnCheckedChangeListener { _, checkedId ->
            var novoTipoOrdenacao = when(checkedId){
                binding.radioOrdenacaoAZ.id -> TipoOrdenacao.ALFABEICA_AZ
                binding.radioOrdenacaoZA.id -> TipoOrdenacao.ALFABETICA_ZA
                binding.radioOrdenacaoInsercao.id -> TipoOrdenacao.ORDEM_INCERCAO
                else -> TipoOrdenacao.ORDEM_INCERCAO
            }

            val editor = config.edit()
            editor.putString(PrefsConstants.KEY_TIPO_ORDENACAO_CONTATOS, novoTipoOrdenacao.toString())
            editor.apply()
        }

        return view
    }
}