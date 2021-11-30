package br.uea.transirie.mypay.mypaytemplate.utils

import android.content.Context
import android.util.Log
import br.uea.transirie.mypay.mypaytemplate.R

object AppPreferences {
    private const val logtag = "APP_PREFERENCES"

    fun getEmailLogado(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.PREF_USER_DATA),
            Context.MODE_PRIVATE
        )

        return sharedPreferences.getString(
            context.getString(R.string.PREF_USER_USUARIO_EMAIL),
            ""
        ).toString()
    }

    fun getCNPJLogado(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.PREF_USER_DATA),
            Context.MODE_PRIVATE
        )

        val cnpj = sharedPreferences.getString(
            context.getString(R.string.PREF_USER_ESTABELECIMENTO_CNPJ),
            ""
        )

        Log.d(logtag, "CNPJ: ${cnpj?:"vazio."}")

        return cnpj.toString()
    }

    fun getUltimoUpload(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.PREF_USER_DATA),
            Context.MODE_PRIVATE
        )

        val ultimoUpload = sharedPreferences.getString(
            context.getString(R.string.PREF_USER_ULTIMO_UPLOAD),
            context.getString(R.string.ultimo_upload_sem_data)
        )

        Log.d(logtag, "Último upload: $ultimoUpload")

        return ultimoUpload.toString()
    }

    fun setUltimoUpload(ultimoUpload: String, context: Context) {
        val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.PREF_USER_DATA),
            Context.MODE_PRIVATE
        )

        sharedPreferences.edit()
            .putString(context.getString(R.string.PREF_USER_ULTIMO_UPLOAD), ultimoUpload)
            .apply()
    }

    fun getPIN(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.PREF_USER_DATA),
            Context.MODE_PRIVATE
        )

        return sharedPreferences.getInt(context.getString(R.string.PREF_PIN), 0)
    }

    fun setUserLogado(status: Boolean, context: Context) {
        val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.PREF_USER_DATA),
            Context.MODE_PRIVATE
        )

        sharedPreferences.edit()
            .putBoolean(context.getString(R.string.PREF_USER_LOGADO), status)
            .apply()
    }

    fun clearPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.PREF_USER_DATA),
            Context.MODE_PRIVATE
        )

        sharedPreferences.edit().clear().apply()
    }
}