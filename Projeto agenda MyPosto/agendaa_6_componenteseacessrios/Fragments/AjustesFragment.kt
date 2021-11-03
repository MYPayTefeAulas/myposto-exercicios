package com.example.agendaa_6_componenteseacessrios.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.agendaa_6_componenteseacessrios.Utils.PrefsConstants
import com.example.agendaa_6_componenteseacessrios.databinding.FragmentAjustesBinding
import com.example.agendaa_6_componenteseacessrios.enums.TipoOrdenacao

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

        val config = requireActivity().getSharedPreferences(PrefsConstants.FILE_CONFIGURACOES, 0)

        // descobre configuração atual de ordenação e marca RadioButton apropriado
        val ordenacaoSelecionada_str = config.getString(
            PrefsConstants.KEY_TIPO_ORDENACAO_CONTATOS,
            TipoOrdenacao.ORDEM_INSERCAO.toString()
        )
        val ordenacaoSelecionada: TipoOrdenacao = TipoOrdenacao.valueOf(ordenacaoSelecionada_str!!)
        when(ordenacaoSelecionada) {
            TipoOrdenacao.ALFABETICA_AZ -> binding.radioOrdenacaoAZ.isChecked = true
            TipoOrdenacao.ALFABETICA_ZA -> binding.radioOrdenacaoZA.isChecked = true
            TipoOrdenacao.ORDEM_INSERCAO -> binding.radioOrdenacaoInsercao.isChecked = true
        }

        binding.radioGroupOrdenacao.setOnCheckedChangeListener { _, checkedId ->
            var novoTipoOrdenacao = when(checkedId) {
                binding.radioOrdenacaoAZ.id -> TipoOrdenacao.ALFABETICA_AZ
                binding.radioOrdenacaoZA.id -> TipoOrdenacao.ALFABETICA_ZA
                binding.radioOrdenacaoInsercao.id -> TipoOrdenacao.ORDEM_INSERCAO
                else -> TipoOrdenacao.ORDEM_INSERCAO
            }

            val editor = config.edit()
            editor.putString(PrefsConstants.KEY_TIPO_ORDENACAO_CONTATOS, novoTipoOrdenacao.toString())
            editor.apply()
        }

        return view
    }
}