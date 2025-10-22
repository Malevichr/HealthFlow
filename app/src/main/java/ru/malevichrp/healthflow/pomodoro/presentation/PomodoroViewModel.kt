package ru.malevichrp.healthflow.pomodoro.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.malevichrp.healthflow.pomodoro.data.PomodoroRepository
import javax.inject.Inject

@HiltViewModel
class PomodoroViewModel @Inject constructor(
    private val pomodoroRepository: PomodoroRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), PomodoroClickActions {
    val state: StateFlow<PomodoroUiState> =
        savedStateHandle.getStateFlow(KEY, PomodoroUiState.Initial)

    val currentTimes = pomodoroRepository.pomodoroTimes().stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(5000),
        PomodoroTimes(15, 2)
    )

    private var currentTimerJob: Job? = null
    override fun start() {
        savedStateHandle[KEY] = PomodoroUiState.Work
        currentTimerJob = viewModelScope.launch {
            val waitUntilWork: Flow<Unit> = pomodoroRepository.startWork()
            waitUntilWork.collect {
                savedStateHandle[KEY] = PomodoroUiState.PrepareToRelax
            }
        }
    }

    override fun stop() {
        savedStateHandle[KEY] = PomodoroUiState.Initial
        currentTimerJob?.cancel()
    }

    override fun startRelax() {
        savedStateHandle[KEY] = PomodoroUiState.Relax
        viewModelScope.launch {
            val waitUntilRelax: Flow<Unit> = pomodoroRepository.startRelax()
            waitUntilRelax.collect {
                savedStateHandle[KEY] = PomodoroUiState.Initial
            }
        }
    }

    override fun saveTimes(pomodoroTimes: PomodoroTimes) {
        stop()
        viewModelScope.launch {
            pomodoroRepository.saveTimes(
                workTime = pomodoroTimes.workTime,
                relaxTime = pomodoroTimes.relaxTime
            )
        }
    }

    companion object {
        private const val KEY = "PomodoroStateKey"
    }
}