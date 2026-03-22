@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.wanderer
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.wanderer.JsonStorage.saveTrip
import com.example.wanderer.JsonStorage.saveTripByName
import com.example.wanderer.ui.theme.WandererTheme
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
import kotlin.math.sqrt

//class TripEditorActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?){
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            TripEditor({ oldName, name, arrivalDate, departureDate ->
//                                    finalize(oldName, name, arrivalDate, departureDate) },
//                    {cancel()})
//        }
//    }
//
//    fun finalize(oldName: String, name: String, arrivalDate: Date, departureDate: Date){
//
//    }
//
//    fun cancel(){
//
//    }
//}

@Preview
@Composable
fun TripEditorPreview(){
    fun finalize_fn(oldName: String, name: String, arrivalDate: Date, departureDate: Date){

    }

    fun cancel(){

    }

    WandererTheme {
        TripEditor(::cancel, ::cancel)
    }
}

// An entire trip editor.
// The finalize function should receive a name, arrival date, and departure date,
//   write the new data to JSON, and return an exit code of 1.
// The cancel function should do nothing except return an exit code of 0.
@Composable
fun TripEditor(onConfirm: () -> Unit, onCancel: () -> Unit){
    // Trip name.
    var name by remember<MutableState<String>>({mutableStateOf("")})
    val context = LocalContext.current

    val arrivalDate = rememberDatePickerState()
    val departureDate = rememberDatePickerState()

    var warningText by remember{mutableStateOf("")}

    // Run when a _new_ trip is created.
    fun appendToJson(){
        // Save the data of the trip to the JSON.
        // We use !! here to say "I am absolutely positive this is not null".
        // This is checked in the confirm button code.
        val trip = Trip.new(
            name,
            arrivalDate.selectedDateMillis!!,
            departureDate.selectedDateMillis!!
        )
        saveTrip(context, JSONObject(Json.encodeToString(trip)))
    }

    Dialog (onDismissRequest = onCancel){
        Column {
            // Top-of-menu text
            // Color is there temporarily since for some reason it renders as black-on-black in the preview.
            Text(text = "Add/edit trip:", color = androidx.compose.ui.graphics.Color.White)


            // Trip name
            // See above for color note.
            Text(text = "Name:", color = androidx.compose.ui.graphics.Color.White)
            TextField(
                value = name,
                onValueChange = {s -> name = s}
            )


            // Arrival Date picker
            DatePopupPicker(arrivalDate, name = "Arrival Date")
            // Departure Date picker
            DatePopupPicker(departureDate, name = "Departure Date")


            // Confirm button
            Button(onClick = {
                // Check to ensure that name, arrival, and departure are selected.
                // Arrival and departure check MUST be present, or
                //   we'll have a NullException when it's asserted to be non-null in appendToJson.
                // If we want it optional we'll need to modify the Trip class.
                if (name == ""){
                    warningText = "Must enter a name."
                    return@Button
                }
                if (arrivalDate.selectedDateMillis == null){
                    warningText = "Must select an arrival date."
                    return@Button
                }
                if (departureDate.selectedDateMillis == null){
                    warningText = "Must select a departure date."
                    return@Button
                }

                // Then append to JSON and inform the caller of completion.
                // Caller will probably respond by hiding this menu and reloading their JSON.
                appendToJson()
                onConfirm()
            }) {
                Text("Confirm")
            }


            // Cancel button
            Button(onClick = onCancel) {
                Text("Cancel")
            }

            // Warning text, for invalid name and whatnot.
            // See top-of-menu text for color note.
            Text(warningText, color = androidx.compose.ui.graphics.Color.White)
        }
    }
}

@Composable
fun DatePopupPicker(datePickerState: DatePickerState, name: String){
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Button(onClick = { showDatePicker = true }) {
        Text(name)
    }
}