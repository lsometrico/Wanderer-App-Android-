package com.example.wanderer

import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

// A NOTE ABOUT SERIALIZABLE DATA CLASSES
// The variable names given here must *exactly* match the field names in the JSON format,
//  because they *are* the field names. Kotlin will use them as such.

// Also I'll move all these to their own file later.

// Data class for Activities.
@Serializable
data class Activity(val name: String, val type: String, val priority: String) {

    companion object {
        // Create a default Activity object.
        // Name, type, and priority are all initialized to empty strings.
        fun default(): Activity {
            return Activity("", "", "")
        }

    }
}

// Data class for Days.
@Serializable
data class Day(val morning: Activity, val midmorning: Activity, val noon: Activity, val afternoon: Activity, val evening: Activity){
    companion object{
        // Create a default Day object.
        // All fields are initialized to default Activities.
        fun default(): Day{
            return Day(
                morning = Activity.default(),
                midmorning = Activity.default(),
                noon = Activity.default(),
                afternoon = Activity.default(),
                evening = Activity.default()
            )
        }
    }
}

// Data class for Trips.
// Temporarily named differently due to a name conflict in MainActivity. Will fix later.
// Will need more fields later.
@Serializable
data class Trip(var tripName: String, var days: Array<Day>, var arrivalDate: Long, var departureDate: Long){
    companion object{
        // Create a new Trip.
        // Parameters:
        // tripName: Name
        // arrivalDate: Start date, in millis since epoch.
        // departureDate: End date, in millis since epoch.
        // Returns: A new Trip, with days field of appropriate length initialized to default values.
        fun new(tripName: String, arrivalDate: Long, departureDate: Long): Trip{
            // Get the arrival and departure days as LocalDate objects.
            val arrivalDay = LocalDateTime.ofInstant(Instant.ofEpochMilli(arrivalDate), ZoneId.systemDefault())
            val departureDay = LocalDateTime.ofInstant(Instant.ofEpochMilli(departureDate), ZoneId.systemDefault())

            // Get trip length; .until is normally upper-bound-exclusive,
            // but I think we should be inclusive on both arrival & departure days.
            val tripLenDays = arrivalDay.until(departureDay, ChronoUnit.DAYS) + 1

            // Make array, and initialize each value to defaults.
            val daysArr = Array(tripLenDays.toInt()){ index -> Day.default()}

            // Return the constructed trip.
            return Trip(
                tripName = tripName,
                days = daysArr,
                arrivalDate = arrivalDate,
                departureDate = departureDate
            )
        }
    }
}
