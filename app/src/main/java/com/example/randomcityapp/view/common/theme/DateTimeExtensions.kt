package com.example.randomcityapp.view.common.theme

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toFormattedDateTime(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(Date(this))
}