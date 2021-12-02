package br.uea.transirie.mypay.mypaytemplate.utils.impressora.base

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import br.uea.transirie.mypay.mypaytemplate.utils.impressora.enums.StatusPrinter
//import com.pax.dal.IDAL
//import com.pax.dal.IPrinter
//import com.pax.dal.entity.EFontTypeAscii
//import com.pax.dal.entity.EFontTypeExtCode
//import com.pax.dal.exceptions.PrinterDevException
//import com.pax.neptunelite.api.NeptuneLiteUser

object PrinterBase {

    private val TAG = "IMPRESSORA-BASE"

//    private var dal: IDAL? = null
//    private var neptuneLiteUser: NeptuneLiteUser? = null
//    private var iPrinter: IPrinter? = null

    fun setupPrinter(context: Context) {
//        neptuneLiteUser = NeptuneLiteUser.getInstance()
//        neptuneLiteUser?.let {
//            dal = neptuneLiteUser!!.getDal(context)
//        }
//        dal?.let {
//            iPrinter = dal!!.printer
//        }

    }

    fun printBitmapWithMonoThreshold(bitmap: Bitmap, grayThreshold: Int) {
//        try {
//            iPrinter?.printBitmapWithMonoThreshold(bitmap, grayThreshold)
//        } catch (e: PrinterDevException) {
//            Log.e(TAG, "NÃO FOI POSSÍVEL REALIZAR IMPRESSÃO")
//        }
    }

    fun step(pixel: Int) {
//        try {
//            iPrinter?.step(pixel)
//        } catch (e: PrinterDevException) {
//            Log.e(TAG, "PROBLEMAS COM STEP")
//        }
    }

    fun init() {
//        try {
//            iPrinter?.init()
//        } catch (ex: PrinterDevException) {
//            Log.e(TAG, "NÃO FOI POSSÍVEL INICIAR IMPRESSÃO")
//        }
    }

    fun start() {
//        try {
//            iPrinter?.start()
//        } catch (ex: PrinterDevException) {
//            Log.e(TAG, "NÃO FOI POSSÍVEL STARTAR IMPRESSÃO")
//        }
    }

    fun printStr(string1: String, string2: String?) {
//        try {
//            iPrinter?.printStr(string1, string2)
//        } catch (ex: PrinterDevException) {
//            Log.e(TAG, "NÃO FOI POSSÍVEL REALIZAR IMPRESSÃO")
//        }
    }

    fun printImageBase(image: Bitmap) {
//        try {
//            iPrinter?.printBitmap(image)
//        } catch (e: PrinterDevException) {
//            Log.e(TAG, "NÃO FOI POSSÍVEL REALIZAR IMPRESSÃO")
//        }
    }

//    fun setSizeFont(asciiFontType: EFontTypeAscii, cFontType: EFontTypeExtCode) {

//        try {
//            iPrinter?.fontSet(asciiFontType, cFontType)
//        } catch (e: PrinterDevException) {
//            e.printStackTrace()
//            Log.e("SETFONT", e.printStackTrace().toString())
//        }
//    }

    fun setSpaceFont(wordSpace: Byte, lineSpace: Byte) {
//        try {
//            iPrinter?.spaceSet(wordSpace, lineSpace)
//        } catch (e: PrinterDevException) {
//            e.printStackTrace()
//            Log.e("SETFONT", e.printStackTrace().toString())
//        }
    }

    fun setLeftIntent(intent: Int) {
//        try {
//            iPrinter?.leftIndent(intent)
//        } catch (e: PrinterDevException) {
//            e.printStackTrace()
//            Log.e("SETFONT", e.printStackTrace().toString())
//        }
    }

    fun setIntentFont(intent: Int) {
//        try {
//            iPrinter?.setGray(intent)
//        } catch (e: PrinterDevException) {
//            e.printStackTrace()
//            Log.e("SETFONT", e.printStackTrace().toString())
//        }

    }

//    fun getStatus(): StatusPrinter {
//        return try {
//            val status = iPrinter?.status
//            var result = 300
//            status?.let {
//                result = it
//            }
//            getStatusPrinterMapper(result)
//
//        } catch (e: PrinterDevException) {
//             getStatusPrinterMapper(300)
//        }
//
//    }

//    private fun getStatusPrinterMapper(status: Int) =
//        when (status) {
//            0 -> StatusPrinter.SUCCESS
//            1 -> StatusPrinter.PRINTER_IS_BUSY
//            2 -> StatusPrinter.OUT_OF_PAPER
//            3 -> StatusPrinter.THE_FORMAT_OF_PRINT_DATA_PACKER_ERROR
//            4 -> StatusPrinter.PRINTER_MALFUNCTIONS
//            8 -> StatusPrinter.PRINTER_OVER_HEATS
//            9 -> StatusPrinter.VOLTAGE_IS_TOO_LOW
//            240 -> StatusPrinter.PRINTING_IS_UNFINISHED
//            252 -> StatusPrinter.NOT_INSTALLED_FONT_LIBRARY
//            254 -> StatusPrinter.DATA_PACKAGE_IS_TOO_LONG
//            else -> StatusPrinter.PRINTING_IS_UNFINISHED
//        }
//
//    public fun  print(bitmap: Bitmap, cutMode: Int, listener: IPrinter.IPinterListener) {
//        try {
//            iPrinter!!.print(bitmap, cutMode, listener)
//        }
//        catch (e: PrinterDevException){
//
//        }
//    }
//
//    public fun setFont(asciiFontType: EFontTypeAscii, cFontType: EFontTypeExtCode) {
//        try {
//            iPrinter!!.fontSet(asciiFontType, cFontType)
//        }
//        catch (e: PrinterDevException) {
//            e.printStackTrace()
//            Log.e("SETFONT", e.printStackTrace().toString())
//        }
//    }
//
//    public fun setFontPath(path: String) {
//        try {
//            iPrinter!!.setFontPath(path)
//        }
//        catch (e: PrinterDevException) {
//            e.printStackTrace()
//        }
//    }
//
//    public fun spaceSet(wordSpace: Byte, lineSpace: Byte) {
//        try {
//            iPrinter!!.spaceSet(wordSpace, lineSpace)
//        }
//        catch (e: PrinterDevException){
//
//        }
//    }
//
//    public fun printText(text: String, charset: String?) {
//        try {
//            iPrinter!!.printStr(text, charset)
//        }
//        catch (e: PrinterDevException){
//
//        }
//    }
//
//    public fun printImage(image : Bitmap) {
//        try{
//            iPrinter!!.printBitmap(image)
//        }
//        catch (e: PrinterDevException) {
//
//        }
//    }
//
//    public fun leftIntent(intent : Int) {
//        try {
//            iPrinter!!.leftIndent(intent)
//        }
//        catch (e: PrinterDevException) {
//
//        }
//    }
//
//    public fun getDotLine(): Int {
//        return try {
//            iPrinter!!.dotLine
//        }
//        catch (e: PrinterDevException){
//            -2
//        }
//    }
//
//    public fun setGray(level : Int) {
//        try {
//            iPrinter?.setGray(level)
//        }
//        catch (e: PrinterDevException) {
//
//        }
//    }
//
//    public fun setDoubleWidth(isAscDouble: Boolean, isLocalDouble: Boolean) {
//        try {
//            iPrinter!!.doubleWidth(isAscDouble, isAscDouble)
//        }
//        catch (e: PrinterDevException) {
//            e.printStackTrace()
//        }
//    }
//
//    public fun setDoubleHeight(isAscDouble: Boolean, isLocalDouble: Boolean) {
//        try {
//            iPrinter!!.doubleHeight(isAscDouble, isAscDouble)
//        }
//        catch (e: PrinterDevException) {
//            e.printStackTrace()
//        }
//    }
//
//    public fun setInvert(isInvert : Boolean) {
//        try {
//            iPrinter!!.invert(isInvert)
//        }
//        catch (e: PrinterDevException) {
//            e.printStackTrace()
//        }
//    }
//
//    public  fun cutPaper(mode : Int): String {
//        return try {
//            iPrinter!!.cutPaper(mode)
//            "Cut paper sucessful"
//        }
//        catch (e: PrinterDevException) {
//            e.toString()
//        }
//    }
//
//    public  fun getCutMode(): String {
//        return try {
//            return when(iPrinter!!.cutMode) {
//                0 -> "Only support full paper cut"
//                1 -> "Only support partial paper cutting"
//                2 -> "support partial paper and full paper cutting "
//                -1-> "No cutting knife,not support"
//                else -> {
//                    "unknown"
//                }
//            }
//        }
//        catch (e: PrinterDevException) {
//            e.toString()
//        }
//    }

}