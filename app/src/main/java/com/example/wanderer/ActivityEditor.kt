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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Dialog
import androidx.wear.compose.material3.Text
import com.example.wanderer.ui.theme.WandererTheme

@Preview
@Composable
fun ActivityEditorPreview(){
    WandererTheme {
        ActivityEditor(onConfirm = {}, onCancel = {})
    }
}


// An ActivityEditor.
// Opening/closing the dialog should be handled by the caller.
// onConfirm should handle loading JSON, closing the activity, and anything else that happens when the activity is done.
// onCancel should handle closing the editor and probably nothing else.
@Composable
fun ActivityEditor(onConfirm: () -> Unit, onCancel: () -> Unit){
    var name by remember({mutableStateOf("")})
    var addr by remember({mutableStateOf("")})
    var typeExpanded by remember({mutableStateOf(false)})
    var activityType by remember({mutableStateOf("")})
    var timeExpanded by remember({mutableStateOf(false)})
    var timeSlot by remember({mutableStateOf("")})
    var warningText by remember({mutableStateOf("")})

    fun appendToJson(){
        // Save the data as a new activity to the file.
    }

    Dialog(visible = true, onDismissRequest = onCancel){
        Column{
            // Top text
            Text("Create Activity")


            // Name (text field)
            Text ("Name:")
            TextField(value = name, onValueChange = {s -> name = s})


            // Address (text field)
            Text("Address:")
            TextField(value = addr, onValueChange = {s -> addr = s})


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
                        onClick = { activityType = "Flight" }
                    )
                    DropdownMenuItem(
                        text = { Text("Lodging") },
                        onClick = { activityType = "Lodging" }
                    )
                    DropdownMenuItem(
                        text = { Text("Food") },
                        onClick = { activityType = "Food" }
                    )
                    DropdownMenuItem(
                        text = { Text("Event") },
                        onClick = { activityType = "Event" }
                    )
                })
            }


            // Time slot (dropdown menu)
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
                                text = { Text("Flight") },
                                onClick = { timeSlot = "Morning" }
                            )
                            DropdownMenuItem(
                                text = { Text("Lodging") },
                                onClick = { timeSlot = "Mid-morning" }
                            )
                            DropdownMenuItem(
                                text = { Text("Food") },
                                onClick = { timeSlot = "Afternoon" }
                            )
                            DropdownMenuItem(
                                text = { Text("Event") },
                                onClick = { timeSlot = "Evening" }
                            )
                        })
            }


            // Confirm/cancel buttons
            Row{
                // Cancel button
                Button(onClick = {onCancel()}){
                    Text("Cancel")
                }
                // Confirm button
                Button(onClick = {
                    // Check to ensure name, addr, activitytype, and timeslot have been selected.
                    if (name == ""){
                        warningText = "Must select a name"
                        return@Button
                    }
                    if (addr == ""){
                        warningText = "Must input an address"
                        return@Button
                    }
                    if (activityType == ""){
                        warningText = "Must select an activity type"
                        return@Button
                    }
                    if (timeSlot == ""){
                        warningText = "Must select a time slot"
                    }

                    // Save to file, and then do whatever the caller needs once this is confirmed (which is probably loading the JSON).
                    appendToJson()
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