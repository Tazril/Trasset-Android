package com.cwod.trasset.helper

import android.content.Context
import com.cwod.trasset.R
import net.danlew.android.joda.JodaTimeAndroid
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Period
import org.joda.time.PeriodType
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.PeriodFormatterBuilder
import java.util.*

/*
    DateTimeFormat.fullDateTime().print(1617721556927)
    res1: kotlin.String! = Tuesday, 6 April, 2021 3:05:56 PM UTC

    DateTimeFormat.mediumDateTime().print(1617721556927)
    res2: kotlin.String! = 6 Apr, 2021 3:05:56 PM

    DateTimeFormat.shortDateTime().print(1617721556927)
    res4: kotlin.String! = 6/4/21 3:05 PM
 */
class DataFormatter constructor(private val context: Context) {

    companion object {
        private const val TAG = "DataFormatter"

        private var _instance: DataFormatter? = null
        fun createInstance(context: Context) {
            if (_instance == null) {
                _instance = DataFormatter(context)
            }
        }

        fun getInstance() = _instance!!
    }

    init {
        JodaTimeAndroid.init(context)
    }


    fun getDisplayDateMonthYear(milliseconds: Long): String? {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("dd MMM YYYY")
        return dateTimeFormatter.print(milliseconds * 1000)
    }

    fun displayDateRange(startDate: Long, endDate: Long): String {
        val dateTimeFormatter1: DateTimeFormatter = DateTimeFormat.forPattern("dd MMM")
        val dateTimeFormatter2: DateTimeFormatter = DateTimeFormat.forPattern("dd MMM")
        return String.format(
            context.getString(R.string.from_to),
            dateTimeFormatter1.print(startDate * 1000),
            dateTimeFormatter2.print(endDate * 1000)
        )
    }

    fun getDisplayTime(milliseconds: Long): String? {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("hh:mm a")
        return dateTimeFormatter.print(milliseconds)
    }


    fun isDateSame(milliseconds1: Long, milliseconds2: Long): Boolean {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("dd-MM-YYYY")
        if (dateTimeFormatter.print(milliseconds1) == dateTimeFormatter.print(milliseconds2))
            return true
        return false
    }

    fun getDateTime(millis: Long): String {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("dd-MM-YYYY hh:mm a")
        return dateTimeFormatter.print(millis)
    }

    fun getMediumDateTime(millis: Long): String = DateTimeFormat.mediumDateTime().print(millis)

    fun getDateStamp(milliseconds: Long): String {
        val date = DateTime(milliseconds * 1000)
        val dateToday = DateTime.now()

        return Days.daysBetween(date, dateToday).days.let { daysDiff ->
            if (daysDiff < 1)
                "Today"
            else if (daysDiff in 2 downTo 1)
                "Yesterday"
            else {
                val dateTimeFormatter: DateTimeFormatter =
                    DateTimeFormat.forPattern("dd MMM yyyy")
                dateTimeFormatter.print(milliseconds)
            }
        }
    }

    fun isAfterCurrentDate(milliseconds: Long): Boolean {
        val date = DateTime(milliseconds * 1000)
        val dateToday = DateTime.now()
        return dateToday.isAfter(date)
    }

    fun getTimeAgo(milliseconds: Long): String {
        val startDate = DateTime(milliseconds)
        val endDate = DateTime.now()

        val builder = PeriodFormatterBuilder()
            .appendDays()
            .appendSuffix(" day ", " days ")
        if (Days.daysBetween(startDate, endDate).days < 1) {
            builder.appendHours()
                .appendSuffix(" hour ", " hours ")
                .appendMinutes()
                .appendSuffix(" min ", " mins ")
        }

        return builder.toFormatter()
            .print(
                Period(
                    startDate,
                    endDate,
                    PeriodType.dayTime()
                )
            )
    }


    fun formatDecimalPlaces(d: Double): String {
        return String.format("%.2f", d)
    }

    fun getDisplayDayOfWeek(milliseconds: Long): String? {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormat.forPattern("EEEE")
        return dateTimeFormatter.print(milliseconds * 1000)
    }

    fun getDurationBasedFormattedDate(dateInMillis: Long): String {
        val time = getDisplayTime(dateInMillis)!!
        val smsTime = Calendar.getInstance().apply { timeInMillis = dateInMillis }
        val now = Calendar.getInstance()
        val prefix = if (now[Calendar.DATE] - smsTime[Calendar.DATE] <= 1) ""
        else if (now[Calendar.WEEK_OF_YEAR] == smsTime[Calendar.WEEK_OF_YEAR])
            getDisplayDayOfWeek(dateInMillis)!!
        else getDisplayDateMonthYear(dateInMillis)!!
        return "$prefix $time"
    }

}