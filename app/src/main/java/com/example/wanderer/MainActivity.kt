package com.example.wanderer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.wanderer.ui.theme.WandererTheme
import androidx.compose.ui.platform.LocalContext
import com.example.wanderer.JsonStorage.loadAllTrips
import kotlinx.serialization.json.Json
import org.json.JSONObject


class MainActivity : ComponentActivity() {
    // Ran on startup.
    override fun onCreate(savedInstanceState: Bundle?) {
        // Start up CalendarActivity.
//        val intent = Intent(applicationContext, CalendarActivity::class.java)
//        intent.putExtra("tripName", "iwi")
//        startActivity(intent)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainPreview()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview(){
    // Controls whether the add trip dialog is open or not.
    var openAddTripDialog by remember{mutableStateOf(false)}
    // The application context; needed for loadJson.
    val context = LocalContext.current

    // Load the JSON and return it as a list of trips.
    fun loadJson(): List<Trip>{
        return loadAllTrips(context)
            .map<JSONObject, Trip> { obj: JSONObject -> Json.decodeFromString<Trip>(obj.toString()) }
    }

    // Stores the list of trips.
    var tripList = loadJson()

    // Reloads the JSON and closes the addActivity menu.
    // Called when a trip gets added (confirm button is pressed).
    fun reloadJson(){
        tripList = loadJson()
        // Bizarrely, it doesn't seem to recompose properly on trip edit unless I assign to this a couple times.
        // Very weird hack.
        openAddTripDialog = false
        openAddTripDialog = true
        openAddTripDialog = false
    }

    // Main display thing.
    // Don't know what the theme means yet.
    WandererTheme {
        // Don't know what scaffold means yet.
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            // Arrange elements in a column.
            Column(modifier = Modifier.padding(innerPadding)){
                // Title
                Text("Wanderer")

                // Trip list
                TripList(tripList, ::reloadJson)

                // Add trip button
                Button(onClick = {openAddTripDialog = true}){
                    Text("+")
                }

                // The add/edit trip dialog.
                if(openAddTripDialog){
                    TripEditor(::reloadJson, { openAddTripDialog = false })
                }
            }
        }
    }
}

@Composable
fun TripButton(trip: Trip, onConfirm: () -> Unit){
    var openEditTripMenu by remember{mutableStateOf(false)}
    val context = LocalContext.current
    // Display the trip name and the trip edit button.
    // TODO: Display dates
    Row{
        // Trip name button.
        // When clicked, it opens its associated CalendarActivity.
        Button(onClick = {
            val intent = Intent(context, CalendarActivity::class.java)
            intent.putExtra("tripName", trip.tripName)
            context.startActivity(intent)
        }){
            Text(trip.tripName)
        }
        Button(onClick = {openEditTripMenu = true}){
            Text("Edit")
        }
    }

    // Dialog for editing the trip.
    // TODO: Hook up actual editing in TripEditor
    if (openEditTripMenu){
        TripEditor(
            onConfirm = {
                openEditTripMenu = false
                onConfirm()
            },
            onCancel = {
                openEditTripMenu = false
            },
            trip = trip
        )
    }
}

// A preview for TripButton.
@Preview
@Composable
fun TripButtonPreview(){
    val exampleTrip = Trip(tripName = "Tahiti", arrivalDate = 0, departureDate = 2000, days=emptyArray())

    TripButton(exampleTrip, {})
}

// Display a list of trips (as buttons), with associated edit buttons as well.
@Composable
fun TripList(tripList: List<Trip>, onConfirm: () -> Unit){
    // Put each trip into a column.
    Column{
        tripList.forEach{
            // "it" is the current iterator element.
            // Akin to ``for it in tripList:`` in Python and other languages.
            TripButton(it, onConfirm)
        }
    }
}

// A preview for TripList.
@Preview
@Composable
fun TripListPreview(){
    val exampleTripList = listOf(
        Trip(tripName = "Denver", arrivalDate = 0, departureDate = 2000, days=emptyArray()),
        Trip(tripName = "Aurora", arrivalDate = 0, departureDate = 2000, days=emptyArray()),
        Trip(tripName = "Boulder", arrivalDate = 0, departureDate = 2000, days=emptyArray())
    )

    TripList(exampleTripList, {})
}