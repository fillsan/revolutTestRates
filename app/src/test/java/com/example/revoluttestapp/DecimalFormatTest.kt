package com.example.revoluttestapp

import android.widget.EditText
import com.example.revoluttestapp.presentation.utils.CurrencyTextWatcher
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DecimalFormatTest {

    @Test
    fun `test valid amounts`() {
        assertTrue(CurrencyTextWatcher(EditText(null), {}).isInputStringCorrect("123"))
        assertTrue(CurrencyTextWatcher(EditText(null), {}).isInputStringCorrect("123.45"))
        assertTrue(CurrencyTextWatcher(EditText(null), {}).isInputStringCorrect("1234567"))
        assertTrue(CurrencyTextWatcher(EditText(null), {}).isInputStringCorrect("1234567.89"))
        assertTrue(CurrencyTextWatcher(EditText(null), {}).isInputStringCorrect("123.4"))
        assertTrue(CurrencyTextWatcher(EditText(null), {}).isInputStringCorrect("0.00"))
        assertTrue(CurrencyTextWatcher(EditText(null), {}).isInputStringCorrect("0.01"))
    }

    @Test
    fun `test invalid amounts`() {
        assertFalse(CurrencyTextWatcher(EditText(null), {}).isInputStringCorrect("123.456"))
        assertFalse(CurrencyTextWatcher(EditText(null), {}).isInputStringCorrect("12345678"))
    }
}