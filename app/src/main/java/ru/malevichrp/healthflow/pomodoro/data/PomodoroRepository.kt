package ru.malevichrp.healthflow.pomodoro.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.malevichrp.healthflow.pomodoro.presentation.PomodoroTimes
import javax.inject.Inject

interface PomodoroRepository {
    fun startWork(): Flow<Unit>
    fun startRelax(): Flow<Unit>
    suspend fun saveTimes(workTime: Int, relaxTime: Int)
    fun pomodoroTimes(): Flow<PomodoroTimes>

    class Base @Inject constructor(private val dataStore: DataStore<Preferences>) :
        PomodoroRepository {
        override fun startWork(): Flow<Unit> = flow {
            val workTime = dataStore.data
                .map { it[WORK_TIME_KEY] ?: 15 }
                .first()

            delay(60_000L * workTime)
            emit(Unit)
        }

        override fun startRelax(): Flow<Unit> = flow {
            val relaxTime = dataStore.data
                .map { it[RELAX_TIME_KEY] ?: 2 }
                .first()

            delay(60_000L * relaxTime)
            emit(Unit)
        }

        override suspend fun saveTimes(workTime: Int, relaxTime: Int) {
            dataStore.edit { preferences ->
                preferences[WORK_TIME_KEY] = workTime
                preferences[RELAX_TIME_KEY] = relaxTime
            }
        }

        override fun pomodoroTimes(): Flow<PomodoroTimes> {
            return dataStore.data
                .map {
                    PomodoroTimes(
                        it[WORK_TIME_KEY] ?: 15,
                        it[RELAX_TIME_KEY] ?: 2
                    )
                }
        }

        companion object {
            private val WORK_TIME_KEY = intPreferencesKey("workTime")
            private val RELAX_TIME_KEY = intPreferencesKey("relaxTime")
        }
    }
}