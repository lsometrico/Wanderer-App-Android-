package com.example.wanderer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Button
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.wanderer.ui.theme.WandererTheme


class MainActivity : ComponentActivity() {
//    val text = mutableStateOf("test1")

    override fun onCreate(savedInstanceState: Bundle?) {
//        val someText by text

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainPreview()
//            WandererTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Column{
//                        Greeting(
//                            name = someText,
//                            modifier = Modifier.padding(innerPadding)
//                        )
//                        Button { click() }
//                    }
//                }
//            }
        }
    }
//
//    fun click(){
//        text.value = "test2"
//    }
}

// Fake not-real pretend JSON handling so I can make sure the trip editor logic works.
var PRETEND_JSON_HANDLING: ArrayList<Trip> = ArrayList<Trip>()

class Trip
constructor(name: String, arrivalDate: Long, departureDate: Long)
{
    val name: String = name
    // Arrival & departure are time at start of date from epoch in milliseconds.
    // Not fully sure which epoch. Should be obtained from DatePickerState.selectedDateMillis.
    val arrivalDate: Long = arrivalDate
    val departureDate: Long = departureDate
}

@Composable
fun Button(onClick: () -> Unit){
    Button(onClick){
        Text("button")
    }
}


@Preview(showBackground = true)
@Composable
fun MainPreview(){
    // A text value that we can update, and associated UI elements will automatically update with it.
    val text = remember<MutableState<String>>({mutableStateOf("test1")})
    val someText by text
    // A list of text that we can update, and associated UI elements will automatically update with it.
    val textList = remember( {mutableStateListOf<String>("a", "b")})

    var openAddTripDialog by remember{mutableStateOf(false)}

    // Function for button; this will change the top text to "test2" and will add new text ("4").
    fun click(){
        text.value = "test2"
        textList.add("4")
    }

    // Load initial JSON here.
    // For now, I'll just have an example trip list.
    var triplist = ArrayList<Trip>(listOf(
        Trip(name = "Denver", arrivalDate = 0, departureDate = 2000),
        Trip(name = "Aurora", arrivalDate = 0, departureDate = 2000),
        Trip(name = "Boulder", arrivalDate = 0, departureDate = 2000)
    ))

    // Reloads the JSON.
    // Called when a trip gets added.
    // TODO: Non-temporary code
    fun reloadJson(){
        // Temporary code until we merge real JSON handling in.
        triplist = PRETEND_JSON_HANDLING
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
                TripList(triplist, ::reloadJson)

                // Add trip button
                Button(onClick = {openAddTripDialog = true}){
                    Text("+")
                }

                if(openAddTripDialog){
                    TripEditor(::reloadJson, { openAddTripDialog = false })
                }
//                // See Greeting function above; adds some text saying "Hello <name>!".
//                // We use someText here so we can modify it by the button.
//                // Don't know what the modifier means yet.
//                Greeting(
//                    name = someText,
//                    modifier = Modifier.padding(innerPadding)
//                )
//                // Add a button that calls click() when you click it.
//                Button { click() }
//                // "LazyColumn" allows for a dynamically-updating column of things.
//                // I'm thinking we'll use a LazyColumn for the trip view list.
//                LazyColumn{
//                    // This will read textList and create corresponding Text UI elements.
//                    items(textList){ text ->
//                        Text(text)
//                    }
//                }
            }
        }
    }
}

@Composable
fun TripButton(trip: Trip, onConfirm: () -> Unit){
    var openEditTripMenu by remember{mutableStateOf(false)}

    // Display the trip name and the trip edit button.
    // TODO: Display dates
    Row{
        Button(onClick = {}){
            Text(trip.name)
        }
        Button(onClick = {openEditTripMenu = true}){
            Text("Edit")
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
            }
        )
    }
}

@Preview
@Composable
fun TripButtonPreview(){
    val exampleTrip = Trip(name = "Tahiti", arrivalDate = 0, departureDate = 2000)

    TripButton(exampleTrip, {})
}

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

@Preview
@Composable
fun TripListPreview(){
    val exampleTripList = listOf(
        Trip(name = "Denver", arrivalDate = 0, departureDate = 2000),
        Trip(name = "Aurora", arrivalDate = 0, departureDate = 2000),
        Trip(name = "Boulder", arrivalDate = 0, departureDate = 2000)
    )

    TripList(exampleTripList, {})
}

// This doesn't seem to work
// I'm thinking we put the bulk of the activity thing in a separate function,
// that way we can use the same thing in the activity and preview without changes.
@Preview(showBackground = true)
@Composable
fun MainPreview2(){
    MainActivity()
}
