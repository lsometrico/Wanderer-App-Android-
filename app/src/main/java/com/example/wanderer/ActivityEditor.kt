package com.example.wanderer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.wear.compose.material3.Text
import com.example.wanderer.JsonStorage.saveTripByName
import com.example.wanderer.ui.theme.WandererTheme
import kotlinx.serialization.json.Json
import org.json.JSONObject

@Preview
@Composable
fun ActivityEditorPreview(){
    WandererTheme {
        ActivityEditor(onConfirm = {}, onCancel = {},
                       Trip.new("a", 0, 0),
                       0)
    }
}


// An ActivityEditor.
// Opening/closing the dialog should be handled by the caller.
// onConfirm should handle loading JSON, closing the activity, and anything else that happens when the activity is done.
// onCancel should handle closing the editor and probably nothing else.
// trip should be the trip we're adding an activity to.
// day is same as above.
// activity should be non-null if we're editing; if non-null, fields will be prefilled.
// timeslot should be non-null if we're editing,
// and must be the timeslot that the activity is slotted into on the given day.
@Composable
fun ActivityEditor(onConfirm: () -> Unit, onCancel: () -> Unit, trip: Trip, day: Int,
                   activity: Activity? = null, timeslot: String? = null){
    // Initialize name field to either the passed activity name, or "" if no passed activity.
    // The "?:" operator - named the Elvis operator - has a nullable on the left and value on the right.
    // If the left operand is non-null, it evaluates to the left; otherwise it evaluates to the right.
    var name by remember{mutableStateOf(activity?.name ?: "")}

    // Initialize address field to either passed activity address, or "" if no passed activity.
    var address by remember{mutableStateOf(activity?.address ?: "")}

    // typeExpanded is whether the type dropdown is open.
    // activityType stores the activity type field.
    var typeExpanded by remember{mutableStateOf(false)}
    var activityType by remember{mutableStateOf(activity?.type ?: "")}

    // Same as above but with the priority dropdown & field.
    var priorityExpanded by remember{mutableStateOf(false)}
    var priority by remember{mutableIntStateOf(activity?.priority ?: -1)}

    // timeExpanded is whether the time slot dropdown is open.
    // timeslot is initialized to either the timeslot given, or "" if no timeslot given.
    var timeExpanded by remember{mutableStateOf(false)}
    var timeSlot by remember{mutableStateOf(timeslot ?: "")}

    // Warning text, in case the user doesn't fill out a given field.
    var warningText by remember{mutableStateOf("")}

    // Context for the saveToJson function to use when saving.
    val context = LocalContext.current

    // Save to the JSON. Run when the confirm button is pressed.
    fun saveToJson(){
        // Create the new activity.
        val activity = Activity(
            name = name,
            type = activityType,
            priority = priority,
            address = address
        )
        // Save the data as a new activity to the file.
        when(timeSlot){
            "morning" -> {
                trip.days[day].morning = activity
            }
            "midmorning" -> {
                trip.days[day].midmorning = activity
            }
            "noon" -> {
                trip.days[day].noon = activity
            }
            "afternoon" -> {
                trip.days[day].afternoon = activity
            }
            "evening" -> {
                trip.days[day].evening = activity
            }
        }

        saveTripByName(context, JSONObject(Json.encodeToString(trip)))
    }

    Dialog(onDismissRequest = onCancel){
        Column{
            // Top text
            Text("Create Activity")


            // Name (text field)
            Text ("Name:")
            TextField(value = name, onValueChange = {s -> name = s})


            // Address (text field)
            Text("Address:")
            TextField(value = address, onValueChange = {s -> address = s})


            // Type (dropdown menu)
            Box(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Button(onClick = { typeExpanded = !typeExpanded }) {
                    Text("Select type")
                }
                DropdownMenu(
                    expanded = typeExpanded,
                    onDismissRequest = { typeExpanded = false },
                    content =
                {
                    DropdownMenuItem(
                        text = { Text("Flight") },
                        onClick = { activityType = "flight"; typeExpanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Lodging") },
                        onClick = { activityType = "lodging"; typeExpanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Food") },
                        onClick = { activityType = "food"; typeExpanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Event") },
                        onClick = { activityType = "event"; typeExpanded = false }
                    )
                })
            } // end type dropdown



            // Time slot (dropdown menu)
            // We only allow this to open if we're not editing.
            if(activity == null) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Button(onClick = { timeExpanded = !timeExpanded }) {
                        Text("Select time")
                    }
                    DropdownMenu(
                        expanded = timeExpanded,
                        onDismissRequest = { timeExpanded = false },
                        content =
                            {
                                DropdownMenuItem(
                                    text = { Text("Morning") },
                                    onClick = { timeSlot = "morning"; timeExpanded = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Mid-morning") },
                                    onClick = { timeSlot = "midmorning"; timeExpanded = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Noon") },
                                    onClick = { timeSlot = "noon"; timeExpanded = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Afternoon") },
                                    onClick = { timeSlot = "afternoon"; timeExpanded = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Evening") },
                                    onClick = { timeSlot = "evening"; timeExpanded = false }
                                )
                            })
                }
            } // end time select dropdown



            // Priority (dropdown menu)
            Box(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Button(onClick = { priorityExpanded = !priorityExpanded }) {
                    Text("Select priority")
                }
                DropdownMenu(
                    expanded = priorityExpanded,
                    onDismissRequest = { priorityExpanded = false },
                    content =
                        {
                            DropdownMenuItem(
                                text = { Text("Low") },
                                onClick = { priority = 0; priorityExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Medium") },
                                onClick = { priority = 1; priorityExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("High") },
                                onClick = { priority = 2; priorityExpanded = false }
                            )
                        })
            } // end priority dropdown


            // Confirm/cancel buttons
            Row{
                // Cancel button
                Button(onClick = {onCancel()}){
                    Text("Cancel")
                }
                // Confirm button
                Button(onClick = {
                    // Check to ensure name, address, type, timeslot, and priority have been selected.
                    if (name == ""){
                        warningText = "Must select a name"
                        return@Button
                    }
                    if (address == ""){
                        warningText = "Must input an address"
                        return@Button
                    }
                    if (activityType == ""){
                        warningText = "Must select an activity type"
                        return@Button
                    }
                    if (timeSlot == ""){
                        warningText = "Must select a time slot"
                        return@Button
                    }
                    if (priority == -1){
                        warningText = "Must select a priority"
                        return@Button
                    }

                    // Save to file, and then do whatever the caller needs once this is confirmed (which is probably loading the JSON).
                    saveToJson()
                    onConfirm()
                }){
                    Text("Confirm")
                }
            }

            // Warning if invalid input; this is set by Confirm button
            Text(warningText)
        }

    }
}