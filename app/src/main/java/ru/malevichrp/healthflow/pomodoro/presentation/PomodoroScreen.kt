package ru.malevichrp.healthflow.pomodoro.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun PomodoroScreen(viewModel: PomodoroViewModel) {
    val pomodoroUiState = viewModel.state.collectAsStateWithLifecycle()
    val currentTimes = viewModel.currentTimes.collectAsStateWithLifecycle()
    PomodoroScreenUi(
        viewModel,
        pomodoroUiState.value,
        currentTimes.value
    )
}

@Composable
fun PomodoroScreenUi(
    pomodoroClickActions: PomodoroClickActions,
    pomodoroUiState: PomodoroUiState,
    currentTimes: PomodoroTimes
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        pomodoroUiState.Show(pomodoroClickActions, this)

        var workTime by rememberSaveable { mutableStateOf("") }
        var relaxTime by rememberSaveable { mutableStateOf("") }
        LaunchedEffect(currentTimes.workTime) {
            workTime = currentTimes.workTime.toString()
            relaxTime = currentTimes.relaxTime.toString()
        }
        Spacer(modifier = Modifier.weight(0.5F))
        OutlinedTextField(
            value = workTime,
            onValueChange = { workTime = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            textStyle = MaterialTheme.typography.bodyLarge,
            singleLine = true,
            label = {
                Text("work time in minutes")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )

        )
        OutlinedTextField(
            value = relaxTime,
            onValueChange = { relaxTime = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            textStyle = MaterialTheme.typography.bodyLarge,
            singleLine = true,
            label = {
                Text("relax time in minutes")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        Button(
            {
                pomodoroClickActions.saveTimes(
                    PomodoroTimes(
                        workTime.toIntOrNull() ?: 15,
                        relaxTime.toIntOrNull() ?: 2
                    )
                )
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Save times", fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInitial() {
    PomodoroScreenUi(
        PomodoroClickActions.Empty,
        PomodoroUiState.Initial,
        PomodoroTimes(15, 2)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewWork() {
    PomodoroScreenUi(
        PomodoroClickActions.Empty,
        PomodoroUiState.Work,
        PomodoroTimes(15, 2)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPrepareToRelax() {
    PomodoroScreenUi(
        PomodoroClickActions.Empty,
        PomodoroUiState.PrepareToRelax,
        PomodoroTimes(15, 2)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewRelax() {
    PomodoroScreenUi(
        PomodoroClickActions.Empty,
        PomodoroUiState.Relax,
        PomodoroTimes(15, 2)
    )
}