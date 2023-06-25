package com.example.cubecinema.utils

object DateUtil {

    private val monthMap = mapOf(
        0 to "Jan",
        1 to "Feb",
        2 to "Mar",
        3 to "Apr",
        4 to "May",
        5 to "Jun",
        6 to "Jul",
        7 to "Aug",
        8 to "Sep",
        9 to "Oct",
        10 to "Nov",
        11 to "Dec"
    )

    fun convertYYYYMMDDToReadableFormat(date: String): String {
        val year = date.substring(0, 4)
        val month = date.substring(5, 7)
        val day = date.substring(8, 10)

        return "$day ${monthMap[month.toInt()]}, $year"
    }
}