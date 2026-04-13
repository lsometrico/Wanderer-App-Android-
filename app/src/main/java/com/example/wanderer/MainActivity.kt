package com.example.wanderer

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wanderer.JsonStorage.loadAllTrips
import com.example.wanderer.ui.theme.WandererTheme
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

    // i know what it means: pain :^)
    // main theme integration using scaffolding
    WandererTheme {
        // scaffold is basically the way to set up layers on an app
        // you just put things on top of one another to make them look nice
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                // set up a floating button at the bottom
                FloatingActionButton(
                    onClick = { openAddTripDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Trip")
                }
            }
        ) { innerPadding ->
            // arrange elements in a column and set padding (space around)
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ){
                // application title; goes at the top
                Text(
                    text = "Wanderer",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontFamily = MaterialTheme.typography.headlineLarge.fontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 48.sp,
                        letterSpacing = 2.sp,

                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Trip list
                TripList(tripList, ::reloadJson)

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

    // more pain
    // display trip name and the trip edit button inside a card
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            // Trip name text.
            Text(
                text = trip.tripName,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            
            Row {
                // When clicked, it opens its associated CalendarActivity.
                IconButton(onClick = {
                    val intent = Intent(context, CalendarActivity::class.java)
                    intent.putExtra("tripName", trip.tripName)
                    context.startActivity(intent)
                }){
                    Icon(
                        imageVector = Icons.Default.Add, // Placeholder for "View"
                        contentDescription = "Open Calendar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                // Edit button
                IconButton(onClick = { openEditTripMenu = true }){
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Trip",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }

    // Dialog for editing the trip.
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
    WandererTheme {
        val exampleTrip = Trip(tripName = "Tahiti", arrivalDate = 0, departureDate = 2000, days=emptyArray())
        TripButton(exampleTrip, {})
    }
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

    WandererTheme {
        TripList(exampleTripList, {})
    }
}
