package com.example.wanderer

import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit


// The JSON in itineraries.json contains a list of trips.
// Each trip composes of a name, arrivalDate, departureDate, and list of days.
// Each day composes of a list of activities.
// Each activity composes of a name, type, priority, address, starting hour, and starting minute.
// Example JSON:
//[
//{
//    "tripName": "trip name",
//    "days": [
//    {
//        "activities": [
//        {
//            "name": "activity name",
//            "type": "event",
//            "priority": 2,
//            "address": "addr",
//            "hour": 7,
//            "minute": 0
//        }
//        ]
//    },
//    {
//        "activities": []
//    },
//    {
//        "activities": []
//    }
//    ],
//    "arrivalDate": 1776729600000,
//    "departureDate": 1777075200000
//}
//]

// A NOTE ABOUT SERIALIZABLE DATA CLASSES
// The variable names given here must *exactly* match the field names in the JSON format,
//  because they *are* the field names. Kotlin will use them as such.

// Data class for Activities.
@Serializable
data class Activity(val name: String, val type: String, val priority: Int, val address: String, val hour: Int, val minute: Int): Comparable<Activity> {
    // Compare this to the passed object.
    // Returns 0 if this equals the other object, negative if this is less than other,
    // and positive if this is greater than other.
    override fun compareTo(other: Activity): Int {
        // First compare hours of each object.
        if(this.hour < other.hour){
            return -1
        }else if(this.hour > other.hour){
            return 1
        }

        // Then, if the hours are equal, compare minutes.
        if(this.minute < other.minute){
            return -1
        }else if(this.minute > other.minute){
            return 1
        }

        // Then, if both are equal, return 0.
        return 0
    }

    companion object {
        // Create a default Activity object.
        // Name, type, and priority are all initialized to empty strings.
        fun default(): Activity {
            return Activity("", "", -1, "", hour = 0, minute = 0)
        }

    }
}

// Data class for Days.
@Serializable
data class Day(var activities: MutableList<Activity>){

    // Inserts a new activity into the list. This preserves the list being sorted.
    fun insertActivity(activity: Activity){
        // Find where in the list to add it.
        var index = 0;
        while(index < activities.size && activity > activities[index]){
            index++
        }

        // Add it to the list.
        activities.add(index, activity)
    }


    companion object{
        // Create a default Day object.
        // All fields are initialized to default Activities.
        fun default(): Day{
            return Day(activities = mutableListOf())
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
