package com.example.wanderer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
    val text = remember<MutableState<String>>({mutableStateOf("test1")})
    val someText by text

    fun click(){
        text.value = "test2"
    }
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

@Preview(showBackground = true)
@Composable
fun MainPreview2(){
    MainActivity()
}
