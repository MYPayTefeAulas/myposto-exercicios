package br.uea.transirie.mypay.mypaytemplate.ui.impressao

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import br.uea.transirie.mypay.mypaytemplate.R
import br.uea.transirie.mypay.mypaytemplate.utils.getNewColor
import br.uea.transirie.mypay.mypaytemplate.utils.impressora.base.PrinterBase
import kotlinx.android.synthetic.main.activity_reimprimir.*
import kotlinx.android.synthetic.main.receipt.view.*
import kotlin.math.roundToInt

//O warning sendo suprimido aqui estava na fun convertDpToPixel
@Suppress("SameParameterValue")
class ReimprimirActivity : AppCompatActivity() {
    private val mContext = this@ReimprimirActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reimprimir)

        val colorDrawable = ColorDrawable(getNewColor(mContext))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

        val bitmap = createBitmapFromView()
        imageView_preview.setImageBitmap(bitmap)

        button_print.setOnClickListener {

            PrinterBase.setupPrinter(this)
            printReceipt(PrinterBase, bitmap)
        }
    }

    @SuppressLint("InflateParams")
    private fun createBitmapFromView(): Bitmap {
        val mInflate = LayoutInflater.from(this).inflate(R.layout.receipt, null)
        val reciboConstraintLayout = mInflate.recibo_cl as ConstraintLayout

        reciboConstraintLayout.measure(
            View.MeasureSpec.makeMeasureSpec(
                convertDpToPixels(650),
                View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
            )
        )
        reciboConstraintLayout.layout(
            0,
            0,
            reciboConstraintLayout.measuredWidth,
            reciboConstraintLayout.measuredHeight
        )

        val bitmap = Bitmap.createBitmap(
            reciboConstraintLayout.measuredWidth,
            reciboConstraintLayout.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        reciboConstraintLayout.draw(canvas)
        return bitmap
    }

    private fun convertDpToPixels(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            Resources.getSystem().displayMetrics
        ).roundToInt()
    }

    private fun printReceipt(printBase: PrinterBase, bitmap: Bitmap) {
        Thread {
            printBase.init()
            printBase.printBitmapWithMonoThreshold(bitmap, 200)
            printBase.step(70)
            printBase.start()
        }.start()
    }
}