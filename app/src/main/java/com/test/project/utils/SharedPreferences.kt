package com.test.project.utils

import android.content.Context

class SharedPreferences(
    val context: Context
) {
    fun saveUserId(mail: String) {
        context.getSharedPreferences("user", Context.MODE_PRIVATE).edit().putString("mail", mail)
            .apply()
    }

    fun restoreUserId(): String {
        return context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("mail", "")
            ?: ""
    }
}