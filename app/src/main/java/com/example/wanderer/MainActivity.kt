package com.example.wanderer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
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
import com.example.wanderer.ui.theme.WandererTheme


class MainActivity : ComponentActivity() {
    val text = mutableStateOf("test1")

    override fun onCreate(savedInstanceState: Bundle?) {
        val someText by text

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WandererTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column{
                        Greeting(
                            name = someText,
                            modifier = Modifier.padding(innerPadding)
                        )
                        Button { click() }
                    }
                }
            }
        }
    }

    fun click(){
        text.value = "test2"
    }
}




@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun Button(onClick: () -> Unit){
    Button(onClick){
        Text("button")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WandererTheme {
        Greeting("Android")
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

    // Function for button; this will change the top text to "test2" and will add new text ("4").
    fun click(){
        text.value = "test2"
        textList.add("4")
    }
    // Main display thing.
    // Don't know what the theme means yet.
    WandererTheme {
        // Don't know what scaffold means yet.
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            // Arrange elements in a column.
            Column{
                // See Greeting function above; adds some text saying "Hello <name>!".
                // We use someText here so we can modify it by the button.
                // Don't know what the modifier means yet.
                Greeting(
                    name = someText,
                    modifier = Modifier.padding(innerPadding)
                )
                // Add a button that calls click() when you click it.
                Button { click() }
                // "LazyColumn" allows for a dynamically-updating column of things.
                // I'm thinking we'll use a LazyColumn for the trip view list.
                LazyColumn{
                    // This will read textList and create corresponding Text UI elements.
                    items(textList){ text ->
                        Text(text)
                    }
                }
            }
        }
    }
}

// This doesn't seem to work
// I'm thinking we put the bulk of the activity thing in a separate function,
// that way we can use the same thing in the activity and preview without changes.
@Preview(showBackground = true)
@Composable
fun MainPreview2(){
    MainActivity()  
}
