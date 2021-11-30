package br.uea.transirie.mypay.mypaytemplate.utils

import android.content.Context
import br.uea.transirie.mypay.mypaytemplate.R
import com.google.android.material.textfield.TextInputLayout

class MyValidations(context: Context) {

    private val erroCampoObrigatorio = context.getString(R.string.erro_campo_obrigatorio)
    private val erroInsiraCnpjValido = context.getString(R.string.erro_insira_cnpj_valido)
    private val erroTelefoneInvalido = context.getString(R.string.erro_telefone_invalido)
    private val erroEmailInvalido = context.getString(R.string.erro_insira_email_valido)
    private val erroSenhaSeisOuMaisCaracteres = context.getString(R.string.erro_senha_6_ou_mais_caracteres)
    private val erroSenhasIncompativeis = context.getString(R.string.erro_senhas_incompativeis)

    /**
     * Verifica se o textInputLayout está vazio.
     * Caso esteja, atribui uma mensagem de erro. Caso não, limpa as mensagens de erro.
     *
     * @return um booleano simbolizando se o campo está vazio.
     * @param textInput o campo de texto a verificar.
     */
    fun inputHasEmptyError(textInput: TextInputLayout): Boolean {
        var hasEmptyError = false

        if (textInput.editText?.text.toString().isBlank()) {
            textInput.error = erroCampoObrigatorio
            hasEmptyError = true
        }
        else textInput.error = null

        return hasEmptyError
    }

    /**
     * Verifica se há erros no campo de CNPJ.
     * Se houver, atribui mensagens de erro. Se não, limpa as mensagens de erro.
     * Os erros verificados são: se está vazio; se os dados não tem formato de um cnpj;
     * se os dígitos verificadores são válidos.
     *
     * @return um booleano simbolizando se há erros.
     * @param tiCNPJ o campo de texto que contém o cnpj.
     */
    fun cnpjHasErrors(tiCNPJ: TextInputLayout): Boolean {
        var hasErrors = false
        val cnpj = tiCNPJ.editText?.text.toString()

        if (cnpj.isBlank()) {
            tiCNPJ.error = erroCampoObrigatorio
            hasErrors = true
        } else {
            val regexCNPJ = "^[0-9]{2}[.][0-9]{3}[.][0-9]{3}[/][0-9]{4}-[0-9]{2}$".toRegex()
            val cnpjMatches = regexCNPJ.matches(cnpj)

            if (!cnpjMatches) {
                tiCNPJ.error = erroInsiraCnpjValido
                hasErrors = true
            } else {
                val cnpjFirstDigit = 1
                val cnpjLastDigit = 15

                val cnpjList = cnpj.filter { it.isDigit() }
                    .split("")
                    .subList(cnpjFirstDigit, cnpjLastDigit)
                    .map { it.toInt() }

                val cnpjFirstVerificationDigit = 12
                val cnpjSecondVerificationDigit = 13

                val primDigitoVerificadorValido = verificarDigitoCNPJ(
                    cnpjList.subList(0, 12).reversed(),
                    cnpjList[cnpjFirstVerificationDigit]
                )

                val segundoDigitoVerificadorValido = verificarDigitoCNPJ(
                    cnpjList.subList(0, 13).reversed(),
                    cnpjList[cnpjSecondVerificationDigit]
                )

                if (!primDigitoVerificadorValido || !segundoDigitoVerificadorValido) {
                    tiCNPJ.error = erroInsiraCnpjValido
                    hasErrors = true
                }
                else tiCNPJ.error = null
            }

        }

        return hasErrors
    }

    /**
     * Valida os dígitos verificadores de um CNPJ.
     *
     * @param cnpjReversed uma lista com os dígitos do CNPJ do último pro primeiro
     * @param digito a posição do dígito verificador. As opções são somente duas: o dígito 12 ou
     * o dígito 13.
     */
    private fun verificarDigitoCNPJ(cnpjReversed: List<Int>, digito: Int): Boolean {
        val cnpj = cnpjReversed.toMutableList()

        for (i in 0 until cnpj.size) cnpj[i] = cnpj[i] * ((i % 8) + 2)

        val soma = cnpj.reduce { acc, i -> acc + i }
        val mod = soma % 11
        val digitoCorreto = if (11 - mod >= 10) 0 else 11 - mod

        return digitoCorreto == digito
    }

    /**
     * Verifica se há erros no campo de telefone.
     * Se houver, atribui mensagens de erro. Se não, limpa as mensagens de erro.
     * Os erros verificados são: se está vazio; se os dados não tem formato de um telefone.
     *
     * @return um booleano simbolizando se há erros.
     * @param tiTelefone o campo de texto que contém o telefone.
     */
    fun telefoneHasErrors(tiTelefone: TextInputLayout): Boolean {
        var hasErrors = false
        val telefone = tiTelefone.editText?.text.toString()

        if (telefone.isBlank()) {
            tiTelefone.error = erroCampoObrigatorio
            hasErrors = true
        } else {
            val telefoneRegex = "^\\(?\\d{2}\\)? ?(([1-7])|(9\\d))\\d{3}[\\-]?\\d{4}$"
            val isValid = telefone.matches(Regex(telefoneRegex))

            if (!isValid) {
                tiTelefone.error = erroTelefoneInvalido
                hasErrors = true
            }
            else tiTelefone.error = null
        }

        return hasErrors
    }

    /**
     * Verifica se há erros no textInputLayout de email.
     * Se houver, atribui mensagens de erro. Se não, limpa as mensagens de erro.
     * Os erros verificados são: se está vazio; se os dados não tem formato de um email.
     *
     * @return um booleano simbolizando se há erros.
     * @param tiEmail o campo de texto que contém o email.
     */
    fun emailHasErrors(tiEmail: TextInputLayout): Boolean {
        var hasErrors = false
        val email = tiEmail.editText?.text.toString()

        if(email.isBlank()) {
            tiEmail.error = erroCampoObrigatorio
            hasErrors = true
        } else {
            val regexEmail = "^[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{1,25})+$"

            if (!email.matches(Regex(regexEmail))) {
                tiEmail.error = erroEmailInvalido
                hasErrors = true
            }
            else tiEmail.error = null
        }

        return hasErrors
    }

    /**
     * Verifica se há erros em um campo de senha.
     * Se houver, atribui mensagens de erro. Se não, limpa as mensagens de erro.
     * Os erros verificados são: se está vazio; se a entrada é menor que 6 caracteres.
     *
     * @return um booleano simbolizando se há erros.
     * @param tiSenha o campo de texto que contém a senha.
     */
    fun senhaHasErrors(tiSenha: TextInputLayout): Boolean {
        var hasErrors = false
        val senha = tiSenha.editText?.text.toString()
        val tamMinimo = 6

        when {
            senha.isBlank() -> {
                tiSenha.error = erroCampoObrigatorio
                hasErrors = true
            }
            senha.length < tamMinimo -> {
                tiSenha.error = erroSenhaSeisOuMaisCaracteres
                hasErrors = true
            }
            else -> tiSenha.error = null
        }

        return hasErrors
    }

    /**
     * Verifica se há erros no campo de confirmação de senha.
     * Se houver, atribui mensagens de erro. Se não, limpa as mensagens de erro.
     * Os erros verificados são: se está vazio; se o campo é diferente da senha.
     *
     * @return um booleano simbolizando se há erros.
     * @param tiConfSenha o campo de texto que contém a confirmação de senha.
     * @param tiSenha o campo de texto que contém a senha.
     */
    fun confSenhaHasErrors(tiConfSenha: TextInputLayout, tiSenha: TextInputLayout): Boolean {
        var hasErrors = false
        val senha = tiSenha.editText?.text.toString()
        val confSenha = tiConfSenha.editText?.text.toString()

        when {
            confSenha.isBlank() -> {
                tiConfSenha.error = erroCampoObrigatorio
                hasErrors = true
            }
            confSenha != senha -> {
                tiConfSenha.error = erroSenhasIncompativeis
                hasErrors = true
            }
            else -> tiConfSenha.error = null
        }

        return hasErrors
    }
}