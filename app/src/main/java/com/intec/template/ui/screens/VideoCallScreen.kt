package com.intec.template.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.intec.template.navigation.AppScreens

@Composable
fun VideoCallScreen(navController: NavController) {


    Row {
        Text("Video Call Screen")
        Button(
            modifier = Modifier
                .width(150.dp)
                .height(35.dp),
            onClick = {navController.navigate(AppScreens.MainScreen.route)}) {
            Text("Ir a otra pantalla", fontSize = 12.sp)
        }
    }
}