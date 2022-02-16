package com.ibnu.foodcourt.utils.ext

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import kotlin.system.exitProcess

fun Activity.showExitFoodCourtDialog(){
    AlertDialog.Builder(this).apply {
        setTitle("Keluar Aplikasi")
        setMessage("Apakah Anda yakin untuk keluar dari aplikasi FoodCourt?")
        setNegativeButton("Tidak") { p0, _ ->
            p0.dismiss()
        }
        setPositiveButton("IYA") { _, _ ->
            finish()
            exitProcess(0)
        }
    }.create().show()
}

fun Context.showOKDialog(title: String, message: String){
    AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton("OK") { p0, _ ->
            p0.dismiss()
        }
    }.create().show()
}