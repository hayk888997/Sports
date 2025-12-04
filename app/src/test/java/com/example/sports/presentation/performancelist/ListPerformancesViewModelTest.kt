package com.example.sports.presentation.performancelist

import com.example.sports.domain.model.FilterType
import com.example.sports.domain.model.SportPerformance
import com.example.sports.domain.model.StorageType
import com.example.sports.domain.usecase.GetSportsPerformancesUseCase
import com.example.sports.domain.util.DataError
import com.example.sports.domain.util.Result
import com.example.sports.presentation.commmon.AppError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class ListPerformancesViewModelTest {

    private lateinit var viewModel: ListPerformancesViewModel
    private val useCase: GetSportsPerformancesUseCase = mockk()

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun samplePerformances() = listOf(
        SportPerformance("1", "Run", "Park", 10, StorageType.LOCAL),
        SportPerformance("2", "Swim", "Pool", 20, StorageType.REMOTE),
        SportPerformance("3", "Bike", "Gym", 15, StorageType.LOCAL)
    )

    @Test
    fun `init loads data and splits lists`() = runTest {
        coEvery { useCase(FilterType.ALL) } returns Result.Success(samplePerformances())

        // Act
        viewModel = ListPerformancesViewModel(useCase)
        dispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.uiState

        assertFalse(state.isLoading)
        assertEquals(3, state.allPerformances.size)
        assertEquals(2, state.localPerformances.size)
        assertEquals(1, state.remotePerformances.size)
        assertNull(state.error)
    }

    @Test
    fun `init loads data error and sets error in uiState`() = runTest {
        // Arrange
        coEvery { useCase(FilterType.ALL) } returns
                Result.Error(DataError.Network)

        // Act
        viewModel = ListPerformancesViewModel(useCase)
        dispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.uiState

        assertFalse(state.isLoading)
        assertEquals(AppError.NetworkUnavailable, state.error)
    }

    @Test
    fun `filter change updates selectedFilter`() = runTest {
        coEvery { useCase(FilterType.ALL) } returns Result.Success(samplePerformances())

        viewModel = ListPerformancesViewModel(useCase)
        dispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.onEvent(ListPerformancesEvent.FilterChanged(FilterType.LOCAL))

        // Assert
        assertEquals(FilterType.LOCAL, viewModel.uiState.selectedFilter)
    }

    @Test
    fun `scroll position stored per filter`() = runTest {
        coEvery { useCase(FilterType.ALL) } returns Result.Success(samplePerformances())

        viewModel = ListPerformancesViewModel(useCase)
        dispatcher.scheduler.advanceUntilIdle()

        // ALL
        viewModel.onEvent(ListPerformancesEvent.ScrollPositionChanged(5))
        assertEquals(5, viewModel.uiState.scrollState.all)

        // LOCAL
        viewModel.onEvent(ListPerformancesEvent.FilterChanged(FilterType.LOCAL))
        viewModel.onEvent(ListPerformancesEvent.ScrollPositionChanged(12))
        assertEquals(12, viewModel.uiState.scrollState.local)

        // REMOTE
        viewModel.onEvent(ListPerformancesEvent.FilterChanged(FilterType.REMOTE))
        viewModel.onEvent(ListPerformancesEvent.ScrollPositionChanged(3))
        assertEquals(3, viewModel.uiState.scrollState.remote)
    }

    @Test
    fun `refresh triggers loadPerformances again`() = runTest {
        coEvery { useCase(FilterType.ALL) } returns Result.Success(samplePerformances())

        viewModel = ListPerformancesViewModel(useCase)
        dispatcher.scheduler.advanceUntilIdle()

        // Act
        viewModel.onEvent(ListPerformancesEvent.Refresh)
        dispatcher.scheduler.advanceUntilIdle()

        // Assert
        coVerify(exactly = 2) {
            useCase(FilterType.ALL)
        }
    }

    @Test
    fun `refresh keeps previous list`() = runTest {
        val initial = samplePerformances()
        coEvery { useCase(FilterType.ALL) } returns Result.Success(initial)

        viewModel = ListPerformancesViewModel(useCase)
        dispatcher.scheduler.advanceUntilIdle()

        val before = viewModel.uiState.allPerformances

        // Trigger refresh
        viewModel.onEvent(ListPerformancesEvent.Refresh)
        dispatcher.scheduler.advanceUntilIdle()

        val after = viewModel.uiState.allPerformances

        assertEquals(before, after)
        assertFalse(viewModel.uiState.isLoading)
    }

    @Test
    fun `changing filters does not modify list contents`() = runTest {
        val items = samplePerformances()
        coEvery { useCase(FilterType.ALL) } returns Result.Success(items)

        viewModel = ListPerformancesViewModel(useCase)
        dispatcher.scheduler.advanceUntilIdle()

        val beforeAll = viewModel.uiState.allPerformances
        val beforeLocal = viewModel.uiState.localPerformances
        val beforeRemote = viewModel.uiState.remotePerformances

        viewModel.onEvent(ListPerformancesEvent.FilterChanged(FilterType.LOCAL))
        viewModel.onEvent(ListPerformancesEvent.FilterChanged(FilterType.ALL))
        viewModel.onEvent(ListPerformancesEvent.FilterChanged(FilterType.REMOTE))

        assertEquals(beforeAll, viewModel.uiState.allPerformances)
        assertEquals(beforeLocal, viewModel.uiState.localPerformances)
        assertEquals(beforeRemote, viewModel.uiState.remotePerformances)
    }

    @Test
    fun `refresh clears previous error`() = runTest {
        coEvery { useCase(FilterType.ALL) } returnsMany listOf(
            Result.Error(DataError.Network), // first
            Result.Success(samplePerformances()) // second
        )

        viewModel = ListPerformancesViewModel(useCase)
        dispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.uiState.error)

        viewModel.onEvent(ListPerformancesEvent.Refresh)
        dispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.uiState.error)
    }

    @Test
    fun `multiple refresh calls keep state consistent`() = runTest {
        coEvery { useCase(FilterType.ALL) } returns Result.Success(samplePerformances())

        viewModel = ListPerformancesViewModel(useCase)
        dispatcher.scheduler.advanceUntilIdle()

        // user pulls refresh multiple times
        viewModel.onEvent(ListPerformancesEvent.Refresh)
        viewModel.onEvent(ListPerformancesEvent.Refresh)
        dispatcher.scheduler.advanceUntilIdle()

        // Lists still valid
        assertEquals(3, viewModel.uiState.allPerformances.size)
    }

    @Test
    fun `scroll positions stay independent between filters`() = runTest {
        coEvery { useCase(FilterType.ALL) } returns Result.Success(samplePerformances())

        viewModel = ListPerformancesViewModel(useCase)
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.onEvent(ListPerformancesEvent.FilterChanged(FilterType.ALL))
        viewModel.onEvent(ListPerformancesEvent.ScrollPositionChanged(5))

        viewModel.onEvent(ListPerformancesEvent.FilterChanged(FilterType.LOCAL))
        viewModel.onEvent(ListPerformancesEvent.ScrollPositionChanged(12))

        viewModel.onEvent(ListPerformancesEvent.FilterChanged(FilterType.REMOTE))
        viewModel.onEvent(ListPerformancesEvent.ScrollPositionChanged(3))

        assertEquals(5, viewModel.uiState.scrollState.all)
        assertEquals(12, viewModel.uiState.scrollState.local)
        assertEquals(3, viewModel.uiState.scrollState.remote)
    }

    @Test
    fun `empty list handled without errors`() = runTest {
        coEvery { useCase(FilterType.ALL) } returns Result.Success(emptyList())

        viewModel = ListPerformancesViewModel(useCase)
        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.allPerformances.isEmpty())
        assertTrue(viewModel.uiState.localPerformances.isEmpty())
        assertTrue(viewModel.uiState.remotePerformances.isEmpty())
        assertNull(viewModel.uiState.error)
    }
}
