package com.example.revoluttestapp.presentation.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.regex.Pattern

class CurrencyTextWatcher (private val editText: EditText, private val action: (String) -> Unit) : TextWatcher {

    private var previousText = ""

    private val allowedPattern = Pattern.compile("\\d{0,7}(\\.\\d{0,2})?")

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s?.isEmpty()!!) {
            editText.setText("0")
        } else if (s.length == 2 && s[0] == '0' && s[1] == '0') {
            editText.setText("0")
        } else if (s.length == 2 && s[0] == '0' && s[1] != '0' && s[1] != '.') {
            editText.setText(s[1].toString())
        }
    }

    override fun afterTextChanged(s: Editable?) {
        if (isInputStringCorrect(s?.toString() ?: "")) {
            previousText = s.toString()
            action.invoke(previousText)
        } else {
            s?.replace(0, s.length, previousText)
        }
    }

    fun isInputStringCorrect(s: String) =
        allowedPattern.matcher(s).matches()
}