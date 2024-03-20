package com.intec.template.ui.screens

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.intec.template.navigation.AppScreens
import com.intec.template.ui.viewmodels.RobotViewModel
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MainScreen(navController: NavController, robotViewModel: RobotViewModel) {
    val focusManager = LocalFocusManager.current
    // Observa los cambios en el estado de escucha y el texto reconocido
    val isListening by robotViewModel.isListening.observeAsState(false)
    var text by remember { mutableStateOf("") }


    val speechText by robotViewModel.speechText.collectAsState()


    LaunchedEffect(speechText) {


        if(speechText.contains("Peter", ignoreCase = true)){
            if (speechText.contains("hola",ignoreCase = true))
                robotViewModel.speak("Hola")

            if (speechText.equals("avanzar", ignoreCase = true)) robotViewModel.irAdelante()

            if(speechText.equals("parar", ignoreCase = true))robotViewModel.parar()

            if(speechText.contains("Qué hora es",ignoreCase=true)){
                val horaActual = LocalDateTime.now()

                val formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss")
                val horaFormateada = horaActual.format(formatoHora)

                robotViewModel.speak("La hora es $horaActual")
            }

            if (speechText.contains("seguimiento",ignoreCase = true))robotViewModel.comenzarSeguimiento()

            if(speechText.contains("mira arriba", ignoreCase = true))robotViewModel.mirarArriba()

            if(speechText.contains("mira abajo", ignoreCase = true))robotViewModel.mirarAbajo()

            if(speechText.contains("Cuéntame un chiste", ignoreCase = true)){
                robotViewModel.speak("Vale")
                delay(3000)
                robotViewModel.speak("¿Qué le dice una roca a tra roca ? ")
                delay(3000)
                robotViewModel.speak("Que  dura es la vida")
            }
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
                        onClick = {navController.navigate(AppScreens.VideoCallScreen.route)}) {
                        Text("Ir a otra pantalla", fontSize = 12.sp)
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
                    val speechText by robotViewModel.speechText.collectAsState()


                    Text(text = "Escucha $speechText")
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
                            .width(300.dp) // Ajusta el ancho del TextField
                            .height(56.dp) // Ajusta la altura si es necesario
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

    val speechText by robotViewModel.speechText.collectAsState()

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
    LaunchedEffect(speechText) {
        destinations.forEach { destination ->
            if (speechText.contains("Peter")){
            if (speechText.contains(destination, ignoreCase = true)) {

                robotViewModel.irA(destination)
                return@LaunchedEffect
            }}
        }
    }
}
