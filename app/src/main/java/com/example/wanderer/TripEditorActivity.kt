@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.wanderer
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.wanderer.ui.theme.WandererTheme
import java.util.Date
import java.util.Locale
import kotlin.math.sqrt

class TripEditorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TripEditor({ oldName, name, arrivalDate, departureDate ->
                                    finalize(oldName, name, arrivalDate, departureDate) },
                    {cancel()})
        }
    }

    fun finalize(oldName: String, name: String, arrivalDate: Date, departureDate: Date){

    }

    fun cancel(){

    }
}

@Preview
@Composable
fun TripEditorPreview(){
    fun finalize_fn(oldName: String, name: String, arrivalDate: Date, departureDate: Date){

    }

    fun cancel(){

    }

    TripEditor(::finalize_fn, ::cancel)
}

// An entire trip editor.
// The finalize function should receive a name, arrival date, and departure date,
//   write the new data to JSON, and return an exit code of 1.
// The cancel function should do nothing except return an exit code of 0.
@Composable
fun TripEditor(finalize: (oldName: String, name: String, arrivalDate: Date, departureDate: Date) -> Unit, cancel: () -> Unit){
    val nameFieldVal = remember<MutableState<String>>({mutableStateOf("a")})
    var name by nameFieldVal

    var dialogueVal = remember{mutableStateOf(false)}
    var openDialogue by dialogueVal

    fun onValueChange(s: String){
        openDialogue = true
        name = s
    }

    val arrivalDate = rememberDatePickerState()
    val departureDate = rememberDatePickerState()

    WandererTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column{
                Text(text = "Add/edit trip:",
                    modifier = Modifier.padding(innerPadding)
                )
                Text(text = "Name:")
                TextField(
                    value = name,
                    onValueChange = ::onValueChange
                )
                // Arrival Date picker
                DatePopupPicker(arrivalDate)
                // Departure Date picker
                DatePopupPicker(departureDate)
                // Confirm button
                Button(onClick = {}){
                    Text("Ok")
                }
                // Cancel button
                Button(onClick = {}){
                    Text("Cancel")
                }
            }
        }
    }
}

@Composable
fun DatePopupPicker(datePickerState: DatePickerState){
    var showDatePicker by remember { mutableStateOf(false) }
//    val datePickerState = rememberDatePickerState()

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
        Text("Select Date")
    }
}