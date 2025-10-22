package ru.malevichrp.healthflow.pomodoro

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.malevichrp.healthflow.pomodoro.data.PomodoroRepository


@Suppress("unused")
@Module
@InstallIn(ViewModelComponent::class)
abstract class PomodoroBindsModule {
    @Binds
    abstract fun bindPomodoroRepository(pomodoroRepository: PomodoroRepository.Base): PomodoroRepository
}