package com.example.wanderer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.wanderer.ui.theme.WandererTheme
import org.json.JSONObject
import java.util.*
//import file with JSON functions
//import .JSON-handling.kt
import android.content.Intent
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.wanderer.JsonStorage.loadAllTrips
import com.example.wanderer.JsonStorage.loadTripByName
import com.example.wanderer.JsonStorage.saveTripByName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.json.JSONArray
import kotlin.jvm.java

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

@Composable
fun WCalendarPreview(tripData: JSONObject){
    //if anyone wants to try to make our JSON in Kotlin, knock yourself out. ¯\_ (ツ)_/¯

    fun fakeExit(){}

    WandererTheme {
        WCalendar(tripData, exit = ::fakeExit)
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

    WandererTheme{
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                // Display trip name and date.
                Text(trip.tripName)
                Text("Day $day")

                // List of time slots.
                ActivityList(trip, 0, ::reloadJson)
//                Text("Morning")
//                // display morning activity
//                DisplayActivity(activity = trip.days[day].activities[0],
//                                trip = trip,
//                                day = day,
//                                timeSlot = "morning",
//                                onEditConfirm = ::reloadJson,
//                                swapUp = null,
//                                swapDown = {swapActivity("morning", "midmorning")})
//
//                Text("Midmorning")
//                // display midmorning activity
//                DisplayActivity(trip.days[day].activities[1],
//                                trip = trip,
//                                day = day,
//                                timeSlot = "midmorning",
//                                onEditConfirm = ::reloadJson,
//                                swapUp = {swapActivity("morning", "midmorning")},
//                                swapDown = {swapActivity("midmorning", "noon")})
//
//                Text("Noon")
//                // display noon activity
//                DisplayActivity(trip.days[day].activities[2],
//                                trip = trip,
//                                day = day,
//                                timeSlot = "noon",
//                                onEditConfirm = ::reloadJson,
//                                swapUp = {swapActivity("midmorning", "noon")},
//                                swapDown = {swapActivity("noon", "afternoon")})
//
//                Text("Afternoon")
//                // display afternoon activity
//                DisplayActivity(trip.days[day].activities[3],
//                                trip = trip,
//                                day = day,
//                                timeSlot = "afternoon",
//                                onEditConfirm = ::reloadJson,
//                                swapUp = {swapActivity("noon", "afternoon")},
//                                swapDown = {swapActivity("afternoon", "evening")})
//
//                Text("Evening")
//                // display evening activity
//                DisplayActivity(trip.days[day].activities[4],
//                                trip = trip,
//                                day = day,
//                                timeSlot = "evening",
//                                onEditConfirm = ::reloadJson,
//                                swapUp = {swapActivity("afternoon", "evening")},
//                                swapDown = null)

                // Controls at the bottom.
                Row{
                    // New activity button
                    Button(onClick = {showAddActivityMenu = true}){
                        Text("New Activity")
                    }
                    // Only display the prev day button if there's days prior.
                    if(day > 0){
                        Button(onClick = {day -= 1}){
                            Text("Previous day")
                        }
                    }
                    // Only display the next day button if there's another day to display.
                    if(day < tripData.getJSONArray("days").length() - 1){
                        Button(onClick = {day += 1}){
                            Text("Next day")
                        }
                    }
                }

                // Button to exit.
                Button(onClick = exit){
                    Text("Exit")
                }

                // Needed to do the hacky force recompose thing.
                // If it's not here then it just seems to ignore writes to forceRecompose.
                if(forceRecompose){
                }else{
                }


                if(showAddActivityMenu){
                    ActivityEditor(onConfirm = ::confirmAddEditActivity,
                                    onCancel = {showAddActivityMenu = false}, trip, day)
                }
            } // end Column
        } // end Scaffold
    } // end WandererTheme
} // end WCalendar


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

    Row{
        // Display information about the activity; name, type, priority
        Column{
            Text(activity.name)
            Text("type: " + activity.type + ", Priority: " + activity.priority)
            TimeDisplay(activity.hour, activity.minute)
        }

        // Spacer so that the stuff before is left aligned and the stuff after is right aligned.
        // I'm not sure how to have a spacer the size of the Down button so the up buttons are all aligned.
        Spacer(Modifier.weight(1f))

        // Buttons for swapping up and down; only appears if you *can* swap up and down.
        if (swapUp != null){
            Button(onClick = swapUp){
                Text("↑")
            }
        }
        if (swapDown != null) {
            Button(onClick = swapDown) {
                Text("↓")
            }
        }

        // Button to edit the activity.
        Button(onClick = {editActivityOpen = true}){
            Text("Edit")
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
        for(i in 0..<trip.days[day].activities.size){

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

    Text(String.format("%02d:%02d %s", displayHour, minute, ampm))
}