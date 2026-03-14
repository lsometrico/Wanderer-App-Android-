package com.example.wanderer

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.wanderer.JsonStorage.loadTripByName
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

        // temporary JSON object
        val allTripsData = JSONArray("""[{
            "tripName":"b", 
            days = [
                {"morning":""},
                {"midmorning":""}
            ]
            }]""".trimMargin())

        val tripData = allTripsData.getJSONObject(0)


//        val tripData = JSONObject("[{\"tripName\":\"test\"}]")

        //load Intent first so setContent has proper variables to pass
        val tripIntent = intent
//        val tripName = tripIntent.getJSONObject("tripJSON")
//        val tripDay = tripIntent.getInt(day=1)
        setContent {
                WCalendarPreview ()
        }
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
fun WCalendarPreview(){
    //if anyone wants to try to make our JSON in Kotlin, knock yourself out. ¯\_ (ツ)_/¯ 
    WandererTheme {
        WCalendar(day=1)
    }
}

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



//WCalendar main function, called with JSON data for a single day
@Composable
fun WCalendar (day: Int) {
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


    WandererTheme{
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Text("test")
            }
        }
    }
}


//@Composable
//fun ButtonExit(onClick: () -> Unit) {
//    ElevatedButton(
//        onClick = { onClick() },
//        modifier = TODO(),
//        enabled = TODO(),
//        shape = TODO(),
//        colors = TODO(),
//        elevation = TODO(),
//        border = TODO(),
//        contentPadding = TODO(),
//        interactionSource = TODO(),
//        content = TODO()
//    )
//    Text("X")
//}
//
//@Composable
//fun ButtonNext(onClick: () -> Unit) {
//    ElevatedButton(
//        onClick = { onClick() },
//        modifier = TODO(),
//        enabled = TODO(),
//        shape = TODO(),
//        colors = TODO(),
//        elevation = TODO(),
//        border = TODO(),
//        contentPadding = TODO(),
//        interactionSource = TODO(),
//        content = TODO()
//    )
//}
//
//@Composable
//fun ButtonPrev(onClick: () -> Unit) {
//    ElevatedButton(
//        onClick = { onClick() },
//        modifier = TODO(),
//        enabled = TODO(),
//        shape = TODO(),
//        colors = TODO(),
//        elevation = TODO(),
//        border = TODO(),
//        contentPadding = TODO(),
//        interactionSource = TODO(),
//        content = TODO()
//    )
//}

