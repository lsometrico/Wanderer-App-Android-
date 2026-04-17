@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.wanderer
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.window.Dialog
import com.example.wanderer.JsonStorage.deleteTrip
import com.example.wanderer.JsonStorage.saveTrip
import com.example.wanderer.ui.theme.WandererTheme
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.util.Date


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
// The onConfirm function should reload the JSON and cause a recompose, and close this menu.
// The onCancel functinon should just close this menu.
@Composable
fun TripEditor(onConfirm: () -> Unit, onCancel: () -> Unit, trip: Trip? = null){
    // Trip name.
    // If the passed trip is non-null, we use its name as initial value;
    // otherwise we init to empty string.
    var name by remember<MutableState<String>>{
        if(trip != null){
            mutableStateOf(trip.tripName)
        }else{
            mutableStateOf("")
        }
    }

    // Context, needed for appendToJson function.
    // Can't directly access LocalContext.current inside the function because it's not composable.
    val context = LocalContext.current

    // Arrival date & departure date.
    // If the passed trip is non-null, we use its dates as initial values;
    // otherwise we pass null in so it initializes as default.
    val arrivalDate = rememberDatePickerState(trip?.arrivalDate)
    val departureDate = rememberDatePickerState(trip?.departureDate)

    // Warning text in case the user messes up input (forgets to select date, empty name,
    // issues of causality by trying to leave before they've arrived, etc.).
    var warningText by remember{mutableStateOf("")}

    // Run when a _new_ trip is created.
    fun appendToJson(){
        // Save the trip to JSON.
        // If a trip was passed, we're editing an existing one.
        // Otherwise, we're making a new one.
        if(trip != null){
            // Replace the old trip with the new one.
            // Here, we use deleteTrip to delete the old data, in case the user changed the trip name in the edit.
            deleteTrip(context, trip.tripName)
            // Reassign fields, then save modified data
            trip.tripName = name;
            trip.arrivalDate = arrivalDate.selectedDateMillis!!
            trip.departureDate = departureDate.selectedDateMillis!!
            saveTrip(context, JSONObject(Json.encodeToString(trip)))
        }else{
            // Create a new trip and save it.
            val trip = Trip.new(
                name,
                arrivalDate.selectedDateMillis!!,
                departureDate.selectedDateMillis!!
            )
            saveTrip(context, JSONObject(Json.encodeToString(trip)))
        }
    } // end appendToJson


    //back to jetpack compose
    //this is the styling for the entire add/edit trip dialog
    //because of colors already being set in color.kt, the program
    //uses those as their default and need no styling

    Dialog (onDismissRequest = onCancel){
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // top of menu text saying "add/edit"
                Text(
                    text = "Add/edit trip:",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = MaterialTheme.typography.headlineLarge.fontFamily
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // field to enter trip name
                OutlinedTextField(
                    value = name,
                    onValueChange = { s -> name = s },
                    label = { Text("Trip Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Date pickers in a row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        DatePopupPicker(arrivalDate, name = "Arrival")
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        DatePopupPicker(departureDate, name = "Departure")
                    }
                }

                if (warningText.isNotEmpty()) {
                    Text(
                        text = warningText,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // buttons at the bottom; if dialogs are empty they will prompt the message
                // according to what is missing
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (trip != null) {
                        TextButton(
                            onClick = {
                                deleteTrip(context, trip.tripName)
                                onConfirm()
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Delete")
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextButton(onClick = onCancel) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (name == "") {
                                    warningText = "Must enter a name."
                                    return@Button
                                }
                                if (arrivalDate.selectedDateMillis == null) {
                                    warningText = "Must select an arrival date."
                                    return@Button
                                }
                                if (departureDate.selectedDateMillis == null) {
                                    warningText = "Must select a departure date."
                                    return@Button
                                }
                                if (departureDate.selectedDateMillis!! < arrivalDate.selectedDateMillis!!) {
                                    warningText = "Departure date must be later than arrival date."
                                    return@Button
                                }
                                appendToJson()
                                onConfirm()
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }
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
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    OutlinedButton(
        onClick = { showDatePicker = true },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(name)
    }
}
