package com.mastercyber.tp1

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "TPKotlinMM",
    ) {
        App()
    }
}