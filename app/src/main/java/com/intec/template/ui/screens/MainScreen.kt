package com.intec.template.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.intec.template.chatGPT.ChatGPTManager
import com.intec.template.ui.viewmodels.RobotViewModel
import kotlinx.coroutines.delay

@Composable
fun MainScreen(navController: NavController, robotViewModel: RobotViewModel) {
    val focusManager = LocalFocusManager.current
    // Observa los cambios en el estado de escucha y el texto reconocido
    val isListening by robotViewModel.isListening.observeAsState(false)
    var text by remember { mutableStateOf("") }
    val speechText by robotViewModel.speechText.collectAsState()
    val chatGPTManager = ChatGPTManager("")
    val context = LocalContext.current

    LaunchedEffect(key1 = speechText) {
        Log.d("LaunchedEffect", "Ejecutando con speechText: $speechText")

        if (speechText.contains("oye paco", ignoreCase = true) ||
            speechText.contains("彼得", ignoreCase = true)||
            speechText.contains("你得", ignoreCase = true)
            ) {
            //robotViewModel.speak("DIME")
            delay(1000)
            var prompt = speechText.replace("oye Paco","")
            Log.d("LaunchedEffect", "HA ENTRADO EN EL IF DE PACO")
            //chatGPTManager.fetchGPT3Response(prompt){response ->
            //    robotViewModel.speak(response)
            //    Log.d("RESPUESTA DE PACO", response)


                }

            }

    Column {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    onClick = { focusManager.clearFocus() }
                )
        ){
            Column {
                TitleComponent(title = "MANEJO")
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Button(
                        modifier = Modifier
                            .width(100.dp)
                            .height(35.dp),
                        onClick = {robotViewModel.irAdelante()}) {
                        Text("Avanzar", fontSize = 12.sp)
                    }
                    Button(
                        modifier = Modifier
                            .width(100.dp)
                            .height(35.dp),
                        onClick = {robotViewModel.parar()}) {
                        Text("Parar", fontSize = 12.sp)
                    }
                    Button(
                        modifier = Modifier
                            .width(100.dp)
                            .height(35.dp),
                        onClick = {robotViewModel.irAtras()}) {
                        Text("Atrás", fontSize = 12.sp)
                    }
                    Button(
                        modifier = Modifier
                            .width(100.dp)
                            .height(35.dp),
                        onClick = {robotViewModel.mirarArriba()}) {
                        Text("Arriba", fontSize = 12.sp)
                    }
                    Button(
                        modifier = Modifier
                            .width(100.dp)
                            .height(35.dp),
                        onClick = {robotViewModel.mirarAbajo()}) {
                        Text("Abajo", fontSize = 12.sp)
                    }
                }
                TitleComponent(title = "TEXT-CHAT")
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        modifier = Modifier
                            .width(140.dp)
                            .height(35.dp),
                        onClick = {
                            //robotViewModel.startCamera()
                            navController.navigate("text_chat_screen")
                        }
                    ) {
                        Text("Chat", fontSize = 12.sp)
                    }
                }


                TitleComponent(title = "FOCUS")
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Button(
                        modifier = Modifier
                            .width(150.dp)
                            .height(35.dp),
                        onClick = {robotViewModel.comenzarSeguimiento()}) {
                        Text("Seguimiento", fontSize = 12.sp)
                    }
                    Button(
                        modifier = Modifier
                            .width(150.dp)
                            .height(35.dp),
                        onClick = {robotViewModel.pararSeguimiento()}) {
                        Text("Detener", fontSize = 12.sp)
                    }
                }
                TitleComponent(title = "TTS/STT")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround // Espacia los elementos dentro del Row
                ){
                    //val speechText by robotViewModel.speechText.collectAsState()
                    Text(text = "Listening: $speechText")
                }
                Spacer(modifier = Modifier.weight(1f)) // Empuja todo lo que sigue hacia abajo
                Row(
                    modifier = Modifier.fillMaxWidth() ,
                    verticalAlignment = Alignment.CenterVertically, // Alinea verticalmente los elementos del Row
                    horizontalArrangement = Arrangement.SpaceAround // Espacia los elementos dentro del Row
                ) {
                    Button(
                        modifier = Modifier
                            .width(150.dp)
                            .height(35.dp)
                            .padding(start = 8.dp),
                        onClick = {
                            robotViewModel.speak(text)
                            // Opcional: Ocultar el teclado
                            focusManager.clearFocus()
                        }
                    ) {
                        Text("PLAY", fontSize = 12.sp)
                    }
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Mensaje TTS") },
                        modifier = Modifier
                            .width(190.dp) // Ajusta el ancho del TextField según sea necesario
                            .height(30.dp) // Ajusta la altura si es necesario
                    )

                }
                Spacer(modifier = Modifier.weight(1f)) // Empuja todo lo que sigue hacia abajo
                LazyRowUbicaciones(robotViewModel = robotViewModel, modifier = Modifier.fillMaxWidth())
            }
        }
    }

}

@Composable
fun TitleComponent(title: String){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth() // Asegura que el Box ocupe todo el ancho disponible
        // Puedes añadir .fillMaxHeight() si también quieres que ocupe todo el alto disponible
    ) {
        Text(
            text = title,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun LazyRowUbicaciones(
    robotViewModel: RobotViewModel,
    modifier: Modifier = Modifier,
) {

    // Observa los cambios en el LiveData desde el ViewModel
    val destinations by robotViewModel.destinationsList.observeAsState(initial = emptyList())

    LazyRow(
        modifier = modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        items(items = destinations) { destinationName ->
            Button(
                modifier = Modifier
                    .height(30.dp) // Ajusta la altura del botón
                    .width(100.dp), // Ajusta el ancho del botón,
                onClick = { robotViewModel.irA(destinationName) },
                contentPadding =
                PaddingValues( all = 4.dp )
            ) {
                Text(
                    text = destinationName,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}
