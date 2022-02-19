package com.ibnu.foodcourt.utils

import android.content.Context
import android.content.SharedPreferences
import com.ibnu.foodcourt.utils.ConstVal.KEY_EMAIL
import com.ibnu.foodcourt.utils.ConstVal.KEY_IS_ALREADY_INTRODUCED
import com.ibnu.foodcourt.utils.ConstVal.KEY_STAND_ID
import com.ibnu.foodcourt.utils.ConstVal.KEY_TOKEN
import com.ibnu.foodcourt.utils.ConstVal.KEY_USER_ID
import com.ibnu.foodcourt.utils.ConstVal.PREFS_NAME

class SharedPreferenceManager(context: Context) {
    private var prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    fun setStringPreference(prefKey: String, value: String) {
        editor.putString(prefKey, value)
        editor.apply()
    }

    fun setIntPreference(prefKey: String, value: Int) {
        editor.putInt(prefKey, value)
        editor.apply()
    }

    fun setBooleanPreference(prefKey: String, value: Boolean) {
        editor.putBoolean(prefKey, value)
        editor.apply()
    }

    fun clearPreferenceByKey(prefKey: String) {
        editor.remove(prefKey)
        editor.apply()
    }

    val getToken = prefs.getString(KEY_TOKEN, "")
    val getUserId = prefs.getInt(KEY_USER_ID, 0)
    val getStandId = prefs.getInt(KEY_STAND_ID, 0)
    val isAlreadyIntroduced = prefs.getBoolean(KEY_IS_ALREADY_INTRODUCED, false)
}