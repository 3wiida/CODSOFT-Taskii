package com.mahmoudibrahem.taskii.util

import java.time.LocalDateTime

object Formatter {

    fun formatTime(dateTime: LocalDateTime): String {
        var hours = dateTime.hour.toString()
        var minutes = dateTime.minute.toString()

        if (hours.length < 2) {
            hours = "0$hours"
        }

        if (minutes.length < 2) {
            minutes = "0$minutes"
        }

        return "$hours:$minutes"
    }

    fun formatDate(dateTime: LocalDateTime): String {
        val year = dateTime.year.toString()
        var month = dateTime.monthValue.toString()
        var day = dateTime.dayOfMonth.toString()

        if(month.length<2){
            month = "0$month"
        }

        if(day.length<2){
            day = "0$day"
        }

        return "$year-$month-$day"
    }

}