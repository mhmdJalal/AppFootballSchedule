package com.mahlultech.footballclubs3.utils

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

var tz : TimeZone = TimeZone.getTimeZone("WIB")

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

@SuppressLint("SimpleDateFormat")
fun parseDate(strDate: String?): String {
    val firstdate: Date = SimpleDateFormat("yyyy-MM-dd").parse(strDate)
    val formatter = SimpleDateFormat("EEE, dd MMM yyyy")
    return formatter.format(firstdate)
}

fun String.timeFormatter(inputFormat: String = "HH:mm",
                         outputFormat: String = "HH:mm"): String {

    val timeFormat = SimpleDateFormat(inputFormat, Locale.ENGLISH)
    timeFormat.timeZone = tz
    val time = timeFormat.parse(this)

    val returnFormat = SimpleDateFormat(outputFormat, Locale.ENGLISH)
    return returnFormat.format(time)
}

val TextView.regular: Typeface
    get() =
        Typeface.createFromAsset(context.assets, "fonts/roboto_regular.ttf")
val TextView.bold: Typeface
    get() =
        Typeface.createFromAsset(context.assets, "fonts/roboto_bold.ttf")