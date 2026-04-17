package com.example.wanderer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.wanderer.ui.theme.WandererTheme
import org.json.JSONObject
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wanderer.JsonStorage.loadAllTrips
import com.example.wanderer.JsonStorage.loadTripByName
import com.example.wanderer.JsonStorage.saveTripByName
import kotlinx.serialization.json.Json
import org.json.JSONArray


//screen for calendar
//Calendar: must be passed JSON name for loadJSON() method call
//needs exit button, and prev and next arrows for multi-day trips (R2)
//needs tracking for previous screens so we know when the days in trip will 'run out',
// likely a NavController and NavGraph

class CalendarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // We'll send in a tripName via an Intent,
        // then this will load the JSON and select the appropriate trip.

        // temporary JSON object
        val allTripsData = JSONArray("""[{
            "tripName":"b", 
            "arrivalDate": 0,
            "departureDate": 1,
            days = [
                {
                "morning":
                    {"name":"Koi Ponds",
                    "type":"Activity",
                    "priority":0,
                    "address":""},
                "midmorning":
                    {"name":"",
                    "type":"",
                    "priority":0,
                    "address":""},
                "noon":
                    {"name":"",
                    "type":"",
                    "priority":0,
                    "address":""},
                "afternoon":
                    {"name":"",
                    "type":"",
                    "priority":0,
                    "address":""},
                "evening":
                    {"name":"",
                    "type":"",
                    "priority":0,
                    "address":""}
                }
            ]
            }]""".trimMargin())
//        val tripData = allTripsData.getJSONObject(0)


        // The trip MUST exist in the JSON when CalendarActivity is made or it'll crash.
        // Get the trip data associated with the given passed-in tripName
        val tripIntent = intent
        val tripName = tripIntent.getStringExtra("tripName")!!

        val appData = loadAllTrips(applicationContext)
        val tripData = appData.find{trip -> trip.getString("tripName") == tripName}!!


//        val tripData = JSONObject("[{\"tripName\":\"test\"}]")

        //load Intent first so setContent has proper variables to pass
//        val tripName = tripIntent.getJSONObject("tripJSON")
//        val tripDay = tripIntent.getInt(day=1)
        setContent {
            WCalendar (tripData, ::exit)
        }
    }

    fun exit(){
        this.finish()
    }
    //both calendar Activity and MainActivity pass tripJSON
    //only calendar Activity passes day:Int. default is set as one
    // so MainActivity pass does not error

//    //function that sends UI back to TripView upon onClick ButtonExit
//    fun ExitToMain () {
//        val exitI = Intent(applicationContext.MainActivity)
//        startActiivty(intent=exitI)
//    }
//
//    //future handling for multi-day trips, error handling for if button exists in composable
//    fun NextDay() {
//        val nextI = Intent(applicationContext.CalendarActivity::class.java)
//        nextI.putExtra("trip_name", tripName)
//        nextI.puExtra("day", (day+1))
//        startActivity(nextI)
//    }
//    fun PrevDay() {
//        val nextI = Intent(applicationContext.CalendarActivity::class.java)
//        nextI.putExtra("trip_name", tripName)
//        nextI.putExtra("day", (day-1))
//        startActivity(prevI)
//    }
}

@Preview
@Composable
fun WCalendarPreview(){
    //if anyone wants to try to make our JSON in Kotlin, knock yourself out. ¯\_ (ツ)_/¯

    fun fakeExit(){}
    
    // Create a fake JSONObject for the preview
    val fakeTrip = JSONObject("""{
        "tripName":"Preview Trip",
        "arrivalDate": 0,
        "departureDate": 1,
        "days": [
            {
                "activities": [
                    {"name":"Sample Activity", "type":"Sightseeing", "priority":1, "address":"123 Main St", "hour":10, "minute":30}
                ]
            }
        ]
    }""")

    WandererTheme {
        WCalendar(fakeTrip, exit = ::fakeExit)
    }
}



// NOTE : I'm using a slightly different format. Pretty much the same.
// See example trip JSON above for details. Need to document this more formally later.
// Probably once it's more stabilized.

//assumed JSON approach:

//-days in trip in a JSON
//-sectioned day timeslots are in an array or always length 5
//TripA = { "TripName" : {
//    "Day1": [
//    { "Morning" : {
//        "Activity1": {
//        "Type" : "Flight",
//        "Priority : "1",
//        "Location" : "",
//        "Time" : "9am",
//        "Name" : "America FLTxxxx",
//        "Notes" : "Baggage tags...",
//    }
//    },
//        "Midmorning" : "",
//        "Afternoon" : "",
//        "Evening" : "",
//        "Day Notes" : "Check-in",
//    }
//    ]
//    //"Day2" : {...)
//},    //end tripname value
//    "Start-date" : "01062026",
//    "End-date" : "01062026"
//    "Total Days" : "1"
//}


//WCalendar main function, called with JSON data for a single day.
@Composable
fun WCalendar (tripData: JSONObject, exit: () -> Unit) {

    // Stores current day.
    var day by remember{ mutableIntStateOf(0) }
    //day is the passed day that is somehow saved/counted from previous Calendar traversals. the Navigator may need a variable for this
    //must pass trip name from onClick from tripView or something?
//    val tripJSONObject = loadTripByName(LocalContext.current, trip_name)!!
    //pass day via NavController by checking against prev screens???
//    val tripDay: JSONArray = tripData.getJSONArray(day.toString())

    //iterate thru array: key is main breakout, value key is activity name, value's value is all related variable information
    //if priority: set color
    //if type: set logo
    //if notes not null, make activity click to view notes, else no notes view

    //Buttons:
    //ButtonExit() ~~always there, take back to tripView

    //if (day != tripData.startdate) { ButtonPrev() } //may need to relate to state or NavController for this logic
    //if (day != tripData.enddate) { ButtonNext() } //as above

    // Test to decode JSON into classes.
    // Next I want to convert the whole trip JSON to a Trip object in a Remember block so it can be easily edited.
//    val serializedDay = Json.decodeFromString<Day>(tripData
//                .getJSONArray("days")
//                .getJSONObject(0)
//                .toString())
    var trip by remember{mutableStateOf(Json.decodeFromString<Trip>(tripData.toString()))}

    var forceRecompose by remember{mutableStateOf(true)}
    var showAddActivityMenu by remember{mutableStateOf(false)}

    val context = LocalContext.current

    // Reloads the JSON.
    fun reloadJson(){
        // Reload JSON; get trip name, then read JSON & deserialize trip.
        val tripName = trip.tripName
        val tripData = loadTripByName(context, tripName)
        trip = Json.decodeFromString<Trip>(tripData.toString())
    }

    // Reloads the JSON and closes the menu.
    fun confirmAddEditActivity(){
        reloadJson()
        // Close activity menu
        showAddActivityMenu = false
    }

    // Function to swap activities. Not done yet since I realized you can't edit JSON objects directly.
    fun swapActivity(time1: String, time2: String){
//        // Takes in a time, and returns the corresponding activity of the relevant day of trip.
//        fun timeToActivity(time: String): Activity{
//            return when (time) {
//                "morning" -> {
//                    trip.days[day].morning
//                }
//                "midmorning" -> {
//                    trip.days[day].midmorning
//                }
//                "noon" -> {
//                    trip.days[day].noon
//                }
//                "afternoon" -> {
//                    trip.days[day].afternoon
//                }
//                else -> /* time1 == "evening"*/ {
//                    trip.days[day].evening
//                }
//            }
//        }
//
//        // Takes in an activity and a time, and sets the corresponding field of the relevant day of trip.
//        fun setActivityFromTime(activity: Activity, time: String){
//            when (time) {
//                "morning" -> {
//                    trip.days[day].morning = activity
//                }
//                "midmorning" -> {
//                    trip.days[day].midmorning = activity
//                }
//                "noon" -> {
//                    trip.days[day].noon = activity
//                }
//                "afternoon" -> {
//                    trip.days[day].afternoon = activity
//                }
//                else -> /* time1 == "evening"*/ {
//                    trip.days[day].evening = activity
//                }
//            }
//        }
//
//        // Standard swap but using the above functions.
//
//        // Assign activity 1 to temp.
//        val temp = timeToActivity(time1)
//
//        // Assign activity 2 to activity 1.
//        setActivityFromTime(timeToActivity(time2), time1)
//
//        // Assign temp to activity 2.
//        setActivityFromTime(temp, time2)
//
//
//        // Save the JSON.
//        saveTripByName(context, JSONObject(Json.encodeToString(trip)))
//
//        // Hack to force a recompose
//        forceRecompose = true;
//        forceRecompose = false;
    } // end swapActivity


    // deep in the trenches here now
    // this is the whole menu themeing in one function using everything
    WandererTheme{
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                // this is a bottom bar to flip between days and add new stuff
                // note: i need to find a way to make it scrollable because the screen
                // can only fit so much in
                Surface(tonalElevation = 3.dp) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // previous day button
                            FilledTonalButton(
                                onClick = { day -= 1 },
                                enabled = day > 0,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null)
                                Text("Prev")
                            }

                            Spacer(Modifier.width(8.dp))

                            // add a new activity to the day
                            Button(
                                onClick = { showAddActivityMenu = true },
                                modifier = Modifier.weight(1.2f)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Text("New")
                            }

                            Spacer(Modifier.width(8.dp))

                            // next day button
                            FilledTonalButton(
                                onClick = { day += 1 },
                                enabled = day < trip.days.size - 1,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Next")
                                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                            }
                        }

                        // exit / go back to trip view
                        TextButton(
                            onClick = exit,
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        ) {
                            Text("Exit to Trips", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        ) { innerPadding ->
            // where the displayed activities will be at
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                Spacer(Modifier.height(24.dp))
                // show trip name and date
                Text(
                    text = trip.tripName,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = MaterialTheme.typography.headlineLarge.fontFamily
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                // display what day in the trip it is
                Text(
                    text = "Day ${day + 1}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(Modifier.height(16.dp))

                // List of time slots.
                ActivityList(trip, day, ::reloadJson)

                // Needed to do the hacky force recompose thing.
                // If it's not here then it just seems to ignore writes to forceRecompose.
                if(forceRecompose){
                }else{
                }


                if(showAddActivityMenu){
                    ActivityEditor(onConfirm = ::confirmAddEditActivity,
                                    onCancel = {showAddActivityMenu = false}, trip, day)
                }
            }
        }
    }
}


// Displays one activity.
// Displays name stacked with type and priority, and up/down/edit buttons to the right of that.
// Parameters:
// activity: The JSON for the activity.
// trip: The containing trip. Needed for editing the activity.
// day: The containing day. Needed for editing the activity.
// activityIndex: The index of the activity inside the day. Needed for editing the activity.
// onEditConfirm: Function to call when an edit is completed. Parent should reload JSON & recompose when this is called.
// swapUp: The function to call if the "up" button is pressed, or null; if null the Up button won't display.
// swapDown: Same as above but for the down button.
@Composable
fun DisplayActivity(activity: Activity,
                    trip: Trip,
                    day: Int,
                    activityIndex: Int,
                    onEditConfirm: (()->Unit),
                    swapUp: (() -> Unit)? = null,
                    swapDown: (() -> Unit)? = null) {
    var editActivityOpen by remember{mutableStateOf(false)}
    var deleteDialogOpen by remember{mutableStateOf(false)}

    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display information about the activity; name, type, priority
            Column(modifier = Modifier.weight(1f)) {
                TimeDisplay(activity.hour, activity.minute)
                Text(
                    text = activity.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${activity.type} • Priority ${activity.priority}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }

            // Buttons for swapping up and down; only appears if you *can* swap up and down.
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column {
                    if (swapUp != null) {
                        IconButton(onClick = swapUp, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Move Up", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    if (swapDown != null) {
                        IconButton(onClick = swapDown, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Move Down", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                // Button to edit the activity.
                IconButton(onClick = {editActivityOpen = true}){
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                }

                // Button to delete activity
                IconButton(onClick = {deleteDialogOpen = true}){
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }

    // Delete dialog
    if(deleteDialogOpen){
        AlertDialog(
            onDismissRequest = { deleteDialogOpen = false },
            title = { Text("Delete Activity") },
            text = { Text("Are you sure you want to delete this activity?") },
            confirmButton = {
                TextButton(onClick = {
                    trip.days[day].activities.removeAt(activityIndex)
                    saveTripByName(context, JSONObject(Json.encodeToString(trip)))
                    onEditConfirm()
                    deleteDialogOpen = false
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteDialogOpen = false }) {
                    Text("Cancel")
                }
            }
        )
    }


    // Activity Editor dialog.
    if(editActivityOpen){
        ActivityEditor( onConfirm = {onEditConfirm(); editActivityOpen = false},
                        onCancel = {editActivityOpen = false},
                        trip = trip,
                        day = day,
                        activity = activity,
                        activityIndex = activityIndex)
    }
}

// Create a list of activities.
@Composable
fun ActivityList(trip: Trip, day: Int, onEditConfirm: () -> Unit){
    // Get context, for use in swap.
    val context = LocalContext.current

    // Swap activities at index1 and index2.
    fun swap(index1: Int, index2: Int){
        // Swap the activities at index1 and index2.
        val temp = trip.days[day].activities[index1]
        trip.days[day].activities[index1] = trip.days[day].activities[index2]
        trip.days[day].activities[index2] = temp

        // Save the JSON and call onEditConfirm; parent should reload JSON in onEditConfirm.
        saveTripByName(context, JSONObject(Json.encodeToString(trip)))
        onEditConfirm()
    }

    Column{
        for(i in 0 until trip.days[day].activities.size){

            // If this isn't the first one in the list, pass a swapUp function; otherwise pass null.
            val swapUp = if(i > 0){
                {swap(i, i-1)}
            }else{
                null
            }

            // If this isn't the last one in the list, pass a swapDown function; otherwise pass null.
            val swapDown = if(i + 1 < trip.days[day].activities.size){
                {swap(i, i+1)}
            }else{
                null
            }

            // Display the activity.
            DisplayActivity(activity = trip.days[day].activities[i],
                            trip = trip,
                            day = day,
                            activityIndex = i,
                            onEditConfirm = onEditConfirm,
                            swapUp = swapUp,
                            swapDown = swapDown)
        }
    }
} // end activityList

// Displays the time in the format HH:MM [AM/PM]; ex. 01:53 PM
// Parameters:
// hour: The hour. Should be between 0 and 23. This uses 24-hour time as inputs.
// minute: The minute. Should be between 0 and 59.
@SuppressLint("DefaultLocale")
@Composable
fun TimeDisplay(hour: Int, minute: Int){
    val ampm = if (hour < 12) {"AM"} else {"PM"}
    var displayHour = hour % 12
    if (displayHour == 0){
        displayHour = 12
    }

    Text(
        text = String.format("%02d:%02d %s", displayHour, minute, ampm),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold
    )
}
