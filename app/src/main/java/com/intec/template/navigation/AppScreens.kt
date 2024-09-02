package com.intec.template.navigation

sealed class AppScreens (val route: String){
    object MainScreen: AppScreens("main_screen")
    object TextChat: AppScreens("text_chat_screen")
}