package br.uea.transirie.mypay.mypaytemplate.ui.cadastro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityCadastroPassoUnicoBinding
import br.uea.transirie.mypay.mypaytemplate.model.Estabelecimento
import br.uea.transirie.mypay.mypaytemplate.model.Usuario
import br.uea.transirie.mypay.mypaytemplate.utils.*

class CadastroPassoUnicoActivity : AppCompatActivity() {
    private val myContext = this
    private val tag = "CADASTRO"
    private val binding by lazy { ActivityCadastroPassoUnicoBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(R.string.title_cadastro)

        binding.cadastroEstabelecimentoBtAvancar.setOnClickListener {
            checkAndAdvance()
        }

        masks()
    }

    /**
     * Nessa função, verificamos os dados de cadastro fornecidos pelo usuário.
     *
     * A validação dos dados é feita por funções do arquivo de validações (pasta utils) que
     * verifica erros comuns no campos de entrada. Caso haja algum erro comum, será exibida
     * uma mensagem abaixo do campo que contém o problema.
     *
     * Se todos os dados forem válidos, seguimos à tela seguinte para gerar um PIN de usuário.
     */
    private fun checkAndAdvance(){
        val myValidations = MyValidations(myContext)
        val isNomeEstabelecimentoOkay =
            !myValidations.inputHasEmptyError(binding.cadastroEstabelecimentoTiNome)
        val isCnpjOkay = !myValidations.cnpjHasErrors(binding.cadastroEstabelecimentoTiCNPJ)
        val isEmailOkay = !myValidations.emailHasErrors(
            binding.cadastroEstabelecimentoTiEmail
        )
        val isNomeUsuarioOkay = !myValidations.inputHasEmptyError(
            binding.cadastroEstabelecimentoTiNomeUsuario
        )

        val isSobrenomeUsuarioOkay = !myValidations.inputHasEmptyError(
            binding.cadastroEstabelecimentoTiSobrenomeUsuario
        )

        val isTelefoneOkay = !myValidations.telefoneHasErrors(
            binding.cadastroEstabelecimentoTiTelefone
        )

        if (isCnpjOkay && isEmailOkay) {
            Log.d(tag, "O cnpj é válido.")
            Log.d(tag, "O email é válido.")

            if (isNomeEstabelecimentoOkay && isNomeUsuarioOkay && isTelefoneOkay && isSobrenomeUsuarioOkay) {
                Log.d(tag, "Todos os dados são válidos.")

                val nomeEstabelecimento = binding.cadastroEstabelecimentoEtNome.text.toString()
                val cnpj = binding.cadastroEstabelecimentoEtCNPJ.text.toString()
                val estabelecimento = Estabelecimento(nomeFantasia = nomeEstabelecimento, cnpj = cnpj)

                val email = binding.cadastroEstabelecimentoEtEmail.text.toString().trim()
                val nome = binding.cadastroEstabelecimentoEtNomeUsuario.text.toString()
                val sobrenome = binding.cadastroEstabelecimentoEtSobrenomeUsuario.text.toString()
                val nomeUsuarioCompleto = "$nome $sobrenome"
                val telefone = binding.cadastroEstabelecimentoEtTelefone.text.toString()
                val usuario = Usuario(
                    email = email,
                    nome = nomeUsuarioCompleto,
                    telefone = telefone,
                    isGerente = true,
                    estabelecimentoCNPJ = cnpj
                )

                val intent = Intent(myContext, GerarPinActivity::class.java)
                    .putExtra(getString(R.string.EXTRA_USUARIO), usuario)
                    .putExtra(getString(R.string.EXTRA_ESTABELECIMENTO), estabelecimento)
                    .putExtra(getString(R.string.EXTRA_TOPBAR_TITLE), getString(R.string.title_cadastro))
                startActivity(intent)
            }

        }
    }

    /** Função que adiciona máscara aos campos de CNPJ e de Telefone. */
    private fun masks(){
        maskEditText(getString(R.string.MASK_CNPJ), binding.cadastroEstabelecimentoEtCNPJ)
        binding.cadastroEstabelecimentoEtTelefone.let {
            it.addTextChangedListener(MyMask(MaskType.Telephone, it))
        }
    }
}