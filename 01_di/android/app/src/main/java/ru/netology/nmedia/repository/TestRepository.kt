package ru.netology.nmedia.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

// Для примера
interface TestRepository {
    val test: Flow<String>
}

class TestRepositoryImpl @Inject constructor(
    private val arg: String
) : TestRepository {

    override val test: Flow<String>
        get() = flowOf(arg)
}