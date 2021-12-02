package br.uea.transirie.mypay.mypaytemplate.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.ui.abastecer.AbastecerActivity
import br.uea.transirie.mypay.mypaytemplate.ui.atendimento.ListaAtendimentosActivity
import br.uea.transirie.mypay.mypaytemplate.ui.atendimento.NovoAtendimentoActivity
import br.uea.transirie.mypay.mypaytemplate.ui.servicos.TabelaServicosActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btAbastecer.setOnClickListener{
            startActivity(Intent(activity, AbastecerActivity::class.java))
        }
        btServico.setOnClickListener {
            startActivity(Intent(activity, ListaAtendimentosActivity::class.java))
        }

//        home_btnTabelaServicos.setOnClickListener {
//            startActivity(Intent(activity, TabelaServicosActivity::class.java))
//        }

    }

}