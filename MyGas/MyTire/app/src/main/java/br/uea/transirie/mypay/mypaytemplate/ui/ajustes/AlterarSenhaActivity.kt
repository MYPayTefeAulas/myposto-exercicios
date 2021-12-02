package br.uea.transirie.mypay.mypaytemplate.ui.ajustes

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.repository.room.AppDatabase
import br.uea.transirie.mypay.mypaytemplate.ui.home.AtualizarSenhaViewModel
import br.uea.transirie.mypay.mypaytemplate.databinding.ActivityAlterarSenhaBinding
import br.uea.transirie.mypay.mypaytemplate.model.Estabelecimento
import br.uea.transirie.mypay.mypaytemplate.model.Usuario
import br.uea.transirie.mypay.mypaytemplate.utils.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_alterar_senha.*
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class AlterarSenhaActivity : AppCompatActivity() {
    private val myContext = this@AlterarSenhaActivity
    private lateinit var binding: ActivityAlterarSenhaBinding
    private val atualizarSenhaViewModel = AtualizarSenhaViewModel(AppDatabase.getDatabase(myContext))
    private var estabelecimento = Usuario()
    private var emailLogado = ""
    private var novaSenha = "******"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Imediatamente após iniciar a Activity devemos inicializar o layout.
        // A variável "binding" passará a ter uma referência para cada elemento do layout
        binding = ActivityAlterarSenhaBinding.inflate(layoutInflater)

        /* Exclusivo do MyTire: altera a cor do barra superior pra amarelo*/
        val colorDrawable = ColorDrawable(getNewColor(myContext))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

        // essa linha é apenas para manter um padrão, você poderia passar "binding.root" diretamente
        val view = binding.root

        // nessa linha associamos o layout à activity e, ao terminar o "onCreate", a tela estará
        // montada
        setContentView(view)

        // o título afeta a barra superior do aplicativo
        title = "Alterar Senha"

        //Inclui o botão voltar na ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        emailLogado = AppPreferences.getEmailLogado(myContext)
        binding.inputEmailAlterarSenha.setText(emailLogado)

        doAsync {
            estabelecimento = atualizarSenhaViewModel.estabelecimentoByEmail(emailLogado)
        }

        //Trigger de evento para atualizar a Senha de um Login de Usuário já existente
        binding.btnAlterarSenha.setOnClickListener {

            binding.etNovaSenhaAlterarSenha.error = null
            binding.etConfNovaSenhaAlterarSenha.error = null
            binding.etSenhaAlterarSenha.error = null

            //if (validaNomeUsuario() && validaSenha() && validaConfSenha() && validaSenhaAtual()) {
            //    alterarSenha()
            //}
            validaDados()
        }
    }

    private fun validaDados() {
        val isEmailCorrect = !emailHasErrors(binding.etEmailAlterarSenha, myContext)
        val isSenhaAtualCorrect = !senhaHasErrors(binding.etSenhaAlterarSenha, myContext)
        val isNovaSenhaCorrect = !senhaHasErrors(binding.etNovaSenhaAlterarSenha, myContext)
        val isConfNovaSenhaCorrect = !confSenhaHasErrors(binding.etConfNovaSenhaAlterarSenha, binding.etNovaSenhaAlterarSenha, myContext)

        if(isEmailCorrect && isSenhaAtualCorrect){
            val senhaAtualDigitada = binding.inputSenhaAlterarSenha.text.toString()
            // É preciso realizar login no firebase antes de alterar a senha
            Firebase.auth.signInWithEmailAndPassword(
                emailLogado, senhaAtualDigitada
            ).addOnCompleteListener { signIn ->
                if (signIn.isSuccessful) {
                    if(isNovaSenhaCorrect && isConfNovaSenhaCorrect){
                        novaSenha = binding.inputNovaSenhaAlterarSenha.text.toString()
                        alterarSenhaUsuario()
                    }
                } else {
                    signIn.exception?.let {
                        when (it.message) {
                            getString(R.string.firebase_error_senha_incorreta) ->
                                binding.etSenhaAlterarSenha.error = "Senha atual incorreta."
                            else ->
                                Toast.makeText(
                                    myContext,
                                    "Ocorreu um erro: ${it.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                        }
                    }
                }
            }
        }
    }

    //Função que permite retorno à tela anterior através do botão voltar na ActionBar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun alterarSenhaUsuario() {

        Firebase.auth.currentUser?.updatePassword(novaSenha)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //estabelecimento.senhaObfuscated = novaSenha
                doAsync {
                    atualizarSenhaViewModel.atualizaSenha(estabelecimento)

                    uiThread {
                        Toast.makeText(myContext, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show()
                        Handler(Looper.getMainLooper()).postDelayed({
                            finish()
                        }, 2000)
                    }
                }
            } else {
                Log.d(
                    "ALTERAR_SENHA",
                    "Erro no firebase ao alterar senha: ${task.exception?.message}"
                )
            }
        }

    }
}