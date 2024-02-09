package com.intec.template

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ainirobot.coreservice.client.actionbean.Pose
import com.intec.template.robot.RobotConnectionService
import com.intec.template.robot.RobotManager
import com.intec.template.robot.SkillApiService
import com.intec.template.robot.listeners.NavigationListener
import com.intec.template.ui.theme.MyApplicationTheme
import com.intec.template.ui.viewmodels.RobotViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), NavigationListener {

    @Inject
    lateinit var robotManager: RobotManager

    @Inject
    lateinit var robotConnectionService: RobotConnectionService

    @Inject
    lateinit var skillApiService: SkillApiService

    private val robotViewModel : RobotViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        robotManager.addNavigationListener(this)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val recognizedText by robotViewModel.recognizedText.observeAsState("")
                    Greeting(robotViewModel = robotViewModel, recognizedText = recognizedText)
                }
            }
        }
    }

    override fun onRouteBlocked() {
        TODO("Not yet implemented")
        Log.d("NavListener Main", "Route blocked")
    }

    override fun onObstacleDisappeared() {
        TODO("Not yet implemented")
        Log.d("NavListener Main", "Obstacle dissapeared")

    }

    override fun onNavigationStarted() {
        TODO("Not yet implemented")
        Log.d("NavListener Main", "Navigation Started")
    }

    override fun onDestroy() {
        super.onDestroy()
        robotManager.removeNavigationListener(this)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(robotViewModel: RobotViewModel, recognizedText: String) {
    val focusManager = LocalFocusManager.current
    // Observa los cambios en el estado de escucha y el texto reconocido
    val isListening by robotViewModel.isListening.observeAsState(false)
    var text by remember { mutableStateOf("") }

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
                        modifier = Modifier.width(100.dp).height(35.dp),
                        onClick = {robotViewModel.irAdelante()}) {
                        Text("Avanzar", fontSize = 12.sp)
                    }
                    Button(
                        modifier = Modifier.width(100.dp).height(35.dp),
                        onClick = {robotViewModel.parar()}) {
                        Text("Parar", fontSize = 12.sp)
                    }
                    Button(
                        modifier = Modifier.width(100.dp).height(35.dp),
                        onClick = {robotViewModel.irAtras()}) {
                        Text("Atrás", fontSize = 12.sp)
                    }
                    Button(
                        modifier = Modifier.width(100.dp).height(35.dp),
                        onClick = {robotViewModel.mirarArriba()}) {
                        Text("Arriba", fontSize = 12.sp)
                    }
                    Button(
                        modifier = Modifier.width(100.dp).height(35.dp),
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
                        modifier = Modifier.width(150.dp).height(35.dp),
                        onClick = {robotViewModel.comenzarSeguimiento()}) {
                        Text("Seguimiento0000", fontSize = 12.sp)
                    }
                    Button(
                        modifier = Modifier.width(150.dp).height(35.dp),
                        onClick = {robotViewModel.pararSeguimiento()}) {
                        Text("Detener", fontSize = 12.sp)
                    }
                }
                TitleComponent(title = "TTS/STT")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround // Espacia los elementos dentro del Row
                ){
                    Button(
                        modifier = Modifier
                            .width(150.dp).height(35.dp)
                            .padding(start = 8.dp),
                        onClick = {
                            robotViewModel.toggleListening()
                        }) {
                        Text(if (isListening) "PARAR" else "ESCUCHA", fontSize = 12.sp)
                    }
                    // Asegúrate de limitar el número de líneas y el desbordamiento del texto
                    Text(
                        text = if(isListening){if (recognizedText == "") "Escuchando..." else recognizedText} else {""},
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f)) // Empuja todo lo que sigue hacia abajo
                Row(
                    modifier = Modifier.fillMaxWidth() ,
                    verticalAlignment = Alignment.CenterVertically, // Alinea verticalmente los elementos del Row
                    horizontalArrangement = Arrangement.SpaceAround // Espacia los elementos dentro del Row
                ) {
                    Button(
                        modifier = Modifier
                            .width(150.dp).height(35.dp)
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
                onClick = { /* Define tu acción aquí */ },
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
