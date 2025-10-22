package ru.malevichrp.healthflow.pomodoro.presentation

interface PomodoroClickActions {
    fun start()
    fun stop()
    fun startRelax()
    fun saveTimes(pomodoroTimes: PomodoroTimes)

    object Empty : PomodoroClickActions {
        override fun start() = Unit
        override fun stop() = Unit
        override fun startRelax() = Unit
        override fun saveTimes(pomodoroTimes: PomodoroTimes) = Unit
    }
}