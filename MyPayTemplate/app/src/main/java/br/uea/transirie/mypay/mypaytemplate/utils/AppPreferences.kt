package br.uea.transirie.mypay.mypaytemplate.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import br.uea.transirie.mypay.mypaytemplate.R

object AppPreferences {
    private const val logtag = "APP_PREFERENCES"

    private fun getSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.PREF_USER_DATA),
            Context.MODE_PRIVATE
        )
    }

    private fun getEditor(context: Context, callback: (SharedPreferences.Editor) -> Unit) {
        val sharedPreferences = getSharedPrefs(context)

        val editor = sharedPreferences.edit()
        callback(editor)
        editor.apply()
    }

    fun getCNPJLogado(context: Context): String {
        val sharedPreferences = getSharedPrefs(context)

        val cnpj = sharedPreferences.getString(
            context.getString(R.string.PREF_USER_ESTABELECIMENTO_CNPJ),
            context.getString(R.string.cnpj_desconhecido)
        )

        Log.d(logtag, "CNPJ: $cnpj")

        return cnpj.toString()
    }

    fun putCnpj(cnpj: String, context: Context) {
        getEditor(context) {
            it.putString(
                context.getString(R.string.PREF_USER_ESTABELECIMENTO_CNPJ),
                cnpj
            )
        }
    }

    fun getPIN(context: Context): Int {
        val sharedPreferences = getSharedPrefs(context)

        return sharedPreferences.getInt(context.getString(R.string.PREF_PIN), 0)
    }

    fun putPin(pin: Int, context: Context) {
        getEditor(context) { editor ->
            editor.putInt(context.getString(R.string.PREF_PIN), pin)
        }
    }

    fun getUltimoUpload(context: Context): String {
        val sharedPreferences = getSharedPrefs(context)

        val ultimoUpload = sharedPreferences.getString(
            context.getString(R.string.PREF_USER_ULTIMO_UPLOAD),
            context.getString(R.string.ultimo_upload_sem_data)
        )

        Log.d(logtag, "Último upload: $ultimoUpload")

        return ultimoUpload.toString()
    }

    fun setUltimoUpload(ultimoUpload: String, context: Context) {
        getEditor(context) {
            it.putString(context.getString(R.string.PREF_USER_ULTIMO_UPLOAD), ultimoUpload)
        }
    }

    fun setUserLogado(status: Boolean, context: Context) {
        getEditor(context) {
            it.putBoolean(context.getString(R.string.PREF_USER_LOGADO), status)
        }
    }

    fun clearPreferences(context: Context) {
        getEditor(context) {
            it.clear()
        }
    }
}