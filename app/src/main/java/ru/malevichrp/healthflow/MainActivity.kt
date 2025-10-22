package ru.malevichrp.healthflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.malevichrp.healthflow.pomodoro.presentation.PomodoroScreen
import ru.malevichrp.healthflow.pomodoro.presentation.PomodoroViewModel
import ru.malevichrp.healthflow.ui.theme.HealthFlowTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HealthFlowTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Modifier.padding(innerPadding)
                    PomodoroScreen(hiltViewModel<PomodoroViewModel>())
                }
            }
        }
    }
}


