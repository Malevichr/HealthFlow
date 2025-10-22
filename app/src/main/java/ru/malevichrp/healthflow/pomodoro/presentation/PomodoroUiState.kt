package ru.malevichrp.healthflow.pomodoro.presentation

import android.content.Context
import android.media.RingtoneManager
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.io.Serializable

interface PomodoroUiState : Serializable {
    @Composable
    fun Show(pomodoroClickActions: PomodoroClickActions, scope: ColumnScope)

    data object Initial : PomodoroUiState {
        @Suppress("unused")
        private fun readResolve(): Any = Initial

        @Composable
        override fun Show(pomodoroClickActions: PomodoroClickActions, scope: ColumnScope) {
            with(scope) {
                Spacer(modifier = Modifier.Companion.weight(1F))

                Text("Start work?", fontSize = 32.sp)

                Spacer(modifier = Modifier.Companion.weight(1F))

                Button(pomodoroClickActions::start) {
                    Text("Start work", fontSize = 16.sp)
                }
            }

        }
    }

    data object Work : PomodoroUiState {
        @Suppress("unused")
        private fun readResolve(): Any = Work

        @Composable
        override fun Show(pomodoroClickActions: PomodoroClickActions, scope: ColumnScope) {
            with(scope) {
                Spacer(modifier = Modifier.Companion.weight(1F))

                Text("In work", fontSize = 32.sp)

                Spacer(modifier = Modifier.Companion.weight(1F))

                Button(pomodoroClickActions::stop) {
                    Text("Stop pomodoro", fontSize = 16.sp)
                }
            }
        }
    }

    data object PrepareToRelax : PomodoroUiState {
        @Suppress("unused")
        private fun readResolve(): Any = PrepareToRelax

        @Composable
        override fun Show(pomodoroClickActions: PomodoroClickActions, scope: ColumnScope) {
            with(scope) {
                Spacer(modifier = Modifier.Companion.weight(1F))
                var isVisible by remember { mutableStateOf(true) }
                val context = LocalContext.current
                LaunchedEffect(Unit) {
                    val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    val ringtone = RingtoneManager.getRingtone(context, uri)
                    try {
                        while (true) {
                            isVisible = !isVisible
                            context.vibrate()
                            ringtone.play()
                            delay(500)
                        }
                    } finally {
                        ringtone.stop()
                    }
                }

                Text(
                    "Work ended, start relax",
                    fontSize = 32.sp,
                    color = if (isVisible) Color.Companion.Red else Color.Companion.Green
                )

                Spacer(modifier = Modifier.Companion.weight(1F))

                Button(pomodoroClickActions::startRelax) {
                    Text("Start relax", fontSize = 16.sp)
                }
            }
        }

        private fun Context.vibrate(durationMillis: Long = 200) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager

            val vibrator = vibratorManager.defaultVibrator

            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    durationMillis,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        }
    }

    data object Relax : PomodoroUiState {
        @Suppress("unused")
        private fun readResolve(): Any = Relax

        @Composable
        override fun Show(pomodoroClickActions: PomodoroClickActions, scope: ColumnScope) {
            with(scope) {
                Spacer(modifier = Modifier.Companion.weight(1F))

                Text("Relax your body", fontSize = 32.sp)

                Spacer(modifier = Modifier.Companion.weight(1F))

                Button(pomodoroClickActions::stop) {
                    Text("Stop pomodoro", fontSize = 16.sp)
                }
            }
        }
    }
}