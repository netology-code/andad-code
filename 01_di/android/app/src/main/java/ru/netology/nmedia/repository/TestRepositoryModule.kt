package ru.netology.nmedia.repository

import androidx.lifecycle.SavedStateHandle
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ru.netology.nmedia.viewmodel.PostViewModel

// Если нужно, чтобы у каждой вьюмодели был свой репозиторий, привязываем модуль к скоупу вьюмоделей
@InstallIn(ViewModelComponent::class)
@Module
interface TestRepositoryModule {

    companion object {
        @Provides
        @ViewModelScoped
        // SavedStateHandle
        // 1. хранит arguments из фрагмента
        // 2. переживает смерть процесса
        // 3. автоматически предоставляется dagger hilt
        fun provideArg(savedStateHandle: SavedStateHandle): String =
            requireNotNull(savedStateHandle["test"])
    }

    @Binds
    @ViewModelScoped
    fun bindTestRepository(impl: TestRepositoryImpl): TestRepository
}
