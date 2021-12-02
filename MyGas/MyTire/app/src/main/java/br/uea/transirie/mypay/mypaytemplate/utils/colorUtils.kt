package br.uea.transirie.mypay.mypaytemplate.utils

import android.content.Context
import br.uea.transirie.mypay.mypaytemplate.R

fun getNewColor(context: Context, color: Int = R.color.color_ActionBar): Int {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        context.getColor(color)
    } else {
        //Essa função tá deprecated para api > 21, mas as menores ainda deve funcionar
        context.resources.getColor(color)
    }
}