package br.uea.transirie.mypay.mypaytemplate.ui.cadastro

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityCadastroPassoUnicoBinding
import br.uea.transirie.mypay.mypaytemplate.model.Estabelecimento
import br.uea.transirie.mypay.mypaytemplate.model.Usuario
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.utils.*
import br.uea.transirie.mypay.mypaytemplate.viewmodel.CadastroEstabelecimentoEUsuarioViewModel

class CadastroPassoUnicoActivity : AppCompatActivity() {
    private lateinit var viewModel: CadastroEstabelecimentoEUsuarioViewModel
    private val tag = "CADASTRO"
    private val binding by lazy { ActivityCadastroPassoUnicoBinding.inflate(layoutInflater) }
    private val myContext = this
    private var estabelecimento: Estabelecimento? = null
    private var usuario: Usuario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(R.string.title_cadastro)

        val colorDrawable = ColorDrawable(getNewColor(myContext))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = CadastroEstabelecimentoEUsuarioViewModel(AppDatabase.getDatabase(myContext))

        binding.cadastroEstabelecimentoBtAvancar.setOnClickListener {
            checkAndAdvance()
        }

        masks()

    }

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

            if (isNomeEstabelecimentoOkay && isNomeUsuarioOkay && isTelefoneOkay && isSobrenomeUsuarioOkay){
                Log.d(tag, "Todos os dados são válidos.")

                val nomeEstabelecimento = binding.cadastroEstabelecimentoEtNome.text.toString()
                val cnpj = binding.cadastroEstabelecimentoEtCNPJ.text.toString()
                estabelecimento = Estabelecimento(nomeFantasia = nomeEstabelecimento,
                    cnpj = cnpj)

                val email = binding.cadastroEstabelecimentoEtEmail.text.toString().trim()
                val espacoChar = ' '
                val nome = binding.cadastroEstabelecimentoEtNomeUsuario.text.toString()
                val sobrenome = binding.cadastroEstabelecimentoEtSobrenomeUsuario.text.toString()
                val nomeUsuarioCompleto = nome + espacoChar + sobrenome
                val telefone = binding.cadastroEstabelecimentoEtTelefone.text.toString()
                usuario = Usuario(
                    email = email,
                    nome = nomeUsuarioCompleto,
                    telefone = telefone,
                    isGerente = true,
                    estabelecimentoCNPJ = cnpj
                )

                val intent = Intent(myContext, GerarPinActivity::class.java)
                    .putExtra(getString(R.string.EXTRA_USUARIO), usuario)
                    .putExtra(getString(R.string.EXTRA_ESTABELECIMENTO), estabelecimento)
                startActivity(intent)
            }

        }
    }

    private fun masks(){
        maskEditText(getString(R.string.MASK_CNPJ), binding.cadastroEstabelecimentoEtCNPJ)
        binding.cadastroEstabelecimentoEtTelefone.let {
            it.addTextChangedListener(MyMask(MaskType.Telephone, it))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}