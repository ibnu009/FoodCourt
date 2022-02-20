package com.ibnu.foodcourt.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.NumberFormatException
import java.text.DecimalFormat
import java.text.ParseException

class NumberTextWatcher(et: EditText) : TextWatcher {
    private val decimalFormat: DecimalFormat = DecimalFormat("#,###.##")
    private val dfnd: DecimalFormat
    private var hasFractionalPart: Boolean
    private val et: EditText
    override fun afterTextChanged(s: Editable) {
        et.removeTextChangedListener(this)
        try {
            val inilen: Int = et.text.length
            val v = s.toString().replace(decimalFormat.decimalFormatSymbols.groupingSeparator.toString(), "")
            val n = decimalFormat.parse(v)
            val cp = et.selectionStart
            if (hasFractionalPart) {
                et.setText(decimalFormat.format(n))
            } else {
                et.setText(dfnd.format(n))
            }
            val endlen: Int = et.text.length
            val sel = cp + (endlen - inilen)
            if (sel > 0 && sel <= et.text.length) {
                et.setSelection(sel)
            } else {
                et.setSelection(et.text.length - 1)
            }
        } catch (nfe: NumberFormatException) {
        } catch (e: ParseException) {
        }
        et.addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        hasFractionalPart =
            s.toString().contains(decimalFormat.decimalFormatSymbols.decimalSeparator.toString())
    }

    init {
        decimalFormat.isDecimalSeparatorAlwaysShown = true
        dfnd = DecimalFormat("#,###")
        this.et = et
        hasFractionalPart = false
    }
}