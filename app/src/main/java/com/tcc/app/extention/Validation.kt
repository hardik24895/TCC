package com.tcc.app.extention

import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern


fun isValidName(text: String): Boolean {
    val expression = "^[a-z A-Z]{1,}$"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(text)
    return matcher.matches()
}

fun isValidEmail(target: CharSequence): Boolean {
    return if (TextUtils.isEmpty(target)) {
        false
    } else {
        Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}

fun isValidPassword(text: String): Boolean {
    val expression = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$"
    val pattern = Pattern.compile(expression)
    val matcher = pattern.matcher(text)
    return matcher.matches()
}