package com.example.expenseease

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.expenseease.ui.theme.Background
import com.example.expenseease.ui.theme.ExpenseEaseTheme
import com.example.expenseease.ui.theme.GreenDark
import com.example.expenseease.ui.theme.HomeView
import com.example.expenseease.ui.theme.MainViewModel
import com.example.expenseease.ui.theme.Navigation
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            ExpenseEaseTheme(darkTheme = false) {
                SetBarColor(color = Background)
                SetNavigationBarColor(color = GreenDark)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Background
                ) {
                    Navigation(viewModel = MainViewModel())
                }
            }
        }
    }
}
@Composable
private fun SetBarColor(color: Color){
    val systemUicontroller = rememberSystemUiController()
    SideEffect {
        systemUicontroller.setSystemBarsColor(
            color = color
        )
    }
}

@Composable
private fun SetNavigationBarColor(color: Color) {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setNavigationBarColor(
            color = color
        )
    }
}

