package com.example.wanderer

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

//jsonStorage save, load, and deleting itinerary trips in a single itineraries.json file in internal storage
//might be changed to multiple .json files if memory management becomes an issue OR requirements change
//if a duplicate name is used, it is automatically renamed (e.g. "Japan Trip", "Japan Trip (2)", "Japan Trip (3)")
object JsonStorage {

    private const val FILE_NAME = "itineraries.json"

    // ─── PRIVATE HELPERS ────────────────────────────────────────────────────────

    //reads JSON array of all trips from file
    //return as empty if that doesn't exist yet
    private fun readAllTrips(context: Context): JSONArray {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) return JSONArray()
        return JSONArray(file.readText())
    }

    //write an array back to the file using JSON
    private fun writeAllTrips(context: Context, trips: JSONArray) {
        val file = File(context.filesDir, FILE_NAME)
        file.writeText(trips.toString(2))
    }

    //essentially a duplicate checker
    private fun getExistingTripNames(trips: JSONArray): Set<String> {
        val names = mutableSetOf<String>()
        for (i in 0 until trips.length()) {
            names.add(trips.getJSONObject(i).getString("tripName"))
        }
        return names
    }

    //if its an existing trip name, use the same trip name 
    //it'll just append a number to the end of it to make it unique 

    private fun resolveUniqueTripName(desiredName: String, existingNames: Set<String>): String {
        if (desiredName !in existingNames) return desiredName

        // Keep incrementing the suffix until we find a name that isn't taken
        var counter = 2
        while ("$desiredName ($counter)" in existingNames) {
            counter++
        }
        return "$desiredName ($counter)"
    }



    //saves a new trip to itineraries.json
    //if the trip name already exists, it is auto-renamed before saving

    fun saveTrip(context: Context, tripData: JSONObject): String {
        require(tripData.has("tripName")) { "tripData must contain a 'tripName' field." }

        val trips = readAllTrips(context)
        val existingNames = getExistingTripNames(trips)

        // Resolve a unique name and update the tripData object before saving
        val uniqueName = resolveUniqueTripName(tripData.getString("tripName"), existingNames)
        tripData.put("tripName", uniqueName)

        // Append the new trip and persist
        trips.put(tripData)
        writeAllTrips(context, trips)

        return uniqueName // Return so the caller knows what name was actually used
    }

    // Saves a trip to itineraries.json.
    // If the trip name already exists, it is *overridden* with the new trip data.
    // Otherwise, it's appended like normal.
    fun saveTripByName(context: Context, tripData: JSONObject) {
        require(tripData.has("tripName")) { "tripData must contain a 'tripName' field." }

        var trips = loadAllTrips(context)
        // Replace any trip with the same name as the passed one with a new trip.
        // There should be one or zero.
        // If zero we'll append normally.
        var found = false
        trips = trips.map{ trip ->
            if(trip.getString("tripName") == tripData.getString("tripName")){
                found = true
                tripData
            }else{
                trip
            }
        }

        // Append to trips if it didn't replace one already.
        if(!found){
            trips = trips.plus(tripData)
        }

        // Then write all trips.
        writeAllTrips(context, JSONArray(trips))
    }

  //load trips as JSON objects
    fun loadAllTrips(context: Context): List<JSONObject> {
        val trips = readAllTrips(context)
        return (0 until trips.length()).map { trips.getJSONObject(it) }
    }

    //load trip by its exact name
    fun loadTripByName(context: Context, tripName: String): JSONObject? {
        return loadAllTrips(context).find { it.getString("tripName") == tripName }
    }

    //delete by exact name
    fun deleteTrip(context: Context, tripName: String): Boolean {
        val trips = readAllTrips(context)
        val updatedTrips = JSONArray()
        var deleted = false

        for (i in 0 until trips.length()) {
            val trip = trips.getJSONObject(i)
            if (trip.getString("tripName") == tripName) {
                deleted = true  // Skip this trip (effectively deletes it)
            } else {
                updatedTrips.put(trip)
            }
        }

        if (deleted) writeAllTrips(context, updatedTrips)
        return deleted
    }
}
