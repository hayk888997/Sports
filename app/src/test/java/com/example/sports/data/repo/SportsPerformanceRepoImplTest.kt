package com.example.sports.data.repo

import android.database.sqlite.SQLiteException
import com.example.sports.data.local.dao.SportsPerformanceDao
import com.example.sports.data.remote.FirebaseRemoteDataSource
import com.example.sports.domain.model.FilterType
import com.example.sports.domain.model.SportPerformance
import com.example.sports.domain.model.StorageType
import com.example.sports.domain.util.DataError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import com.example.sports.domain.util.Result
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

class SportsPerformanceRepoImplTest {

    private lateinit var dao: SportsPerformanceDao
    private lateinit var remote: FirebaseRemoteDataSource
    private lateinit var repo: SportsPerformanceRepoImpl

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        remote = mockk(relaxed = true)
        repo = spyk(SportsPerformanceRepoImpl(dao, remote), recordPrivateCalls = true)
    }


    @Test
    fun `getPerformances LOCAL returns success when dao returns empty list`() = runTest {
        coEvery { dao.getAll() } returns emptyList()

        val result = repo.getPerformances(FilterType.LOCAL)

        assertTrue(result is Result.Success)
        assertEquals(0, (result as Result.Success).data.size)
    }

    @Test
    fun `getPerformances LOCAL returns LocalFailed when dao throws`() = runTest {
        coEvery { dao.getAll() } throws SQLiteException("db error")

        val result = repo.getPerformances(FilterType.LOCAL)

        assertTrue(result is Result.Error)
        assertEquals(DataError.LocalFailed, (result as Result.Error).error)
    }

    @Test
    fun `getPerformances REMOTE returns success when remote returns empty list`() = runTest {
        coEvery { remote.getPerformances() } returns emptyList()

        val result = repo.getPerformances(FilterType.REMOTE)

        assertTrue(result is Result.Success)
        assertEquals(0, (result as Result.Success).data.size)
    }

    @Test
    fun `getPerformances REMOTE returns Network when remote throws IOException`() = runTest {
        coEvery { remote.getPerformances() } throws IOException("network")

        val result = repo.getPerformances(FilterType.REMOTE)

        assertTrue(result is Result.Error)
        assertEquals(DataError.Network, (result as Result.Error).error)
    }

    @Test
    fun `getPerformances ALL returns merged list when both local and remote succeed`() = runTest {
        val localList = listOf(mockk<SportPerformance>(), mockk())
        val remoteList = listOf(mockk<SportPerformance>())

        coEvery { repo["getLocal"]() } returns Result.Success(localList)
        coEvery { repo["getRemote"]() } returns Result.Success(remoteList)

        val result = repo.getPerformances(FilterType.ALL)

        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals(localList + remoteList, data)
    }

    @Test
    fun `getPerformances ALL returns local when local success and remote error`() = runTest {
        val localList = listOf(mockk<SportPerformance>())

        coEvery { repo["getLocal"]() } returns Result.Success(localList)
        coEvery { repo["getRemote"]() } returns Result.Error(DataError.Network)

        val result = repo.getPerformances(FilterType.ALL)

        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals(localList, data)
    }

    @Test
    fun `getPerformances ALL returns remote when remote success and local error`() = runTest {
        val remoteList = listOf(mockk<SportPerformance>())

        coEvery { repo["getLocal"]() } returns Result.Error(DataError.LocalFailed)
        coEvery { repo["getRemote"]() } returns Result.Success(remoteList)

        val result = repo.getPerformances(FilterType.ALL)

        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals(remoteList, data)
    }

    @Test
    fun `getPerformances ALL returns Unknown when both sources fail`() = runTest {
        coEvery { repo["getLocal"]() } returns Result.Error(DataError.LocalFailed)
        coEvery { repo["getRemote"]() } returns Result.Error(DataError.Network)

        val result = repo.getPerformances(FilterType.ALL)

        assertTrue(result is Result.Error)
        val error = (result as Result.Error).error
        assertTrue(error is DataError.Unknown)
        assertEquals("Both sources failed", (error as DataError.Unknown).exception.message)
    }

    @Test
    fun `storeSportPerformance LOCAL calls dao and returns success`() = runTest {
        val performance = mockk<SportPerformance>(relaxed = true)

        val result = repo.storeSportPerformance(performance, StorageType.LOCAL)

        assertTrue(result is Result.Success)
        coVerify(exactly = 1) { dao.insert(any()) }
    }

    @Test
    fun `storeSportPerformance LOCAL returns LocalFailed when dao throws`() = runTest {
        val performance = mockk<SportPerformance>(relaxed = true)
        coEvery { dao.insert(any()) } throws SQLiteException("db error")

        val result = repo.storeSportPerformance(performance, StorageType.LOCAL)

        assertTrue(result is Result.Error)
        assertEquals(DataError.LocalFailed, (result as Result.Error).error)
    }

    @Test
    fun `storeSportPerformance REMOTE calls remote and returns success`() = runTest {
        val performance = mockk<SportPerformance>(relaxed = true)

        val result = repo.storeSportPerformance(performance, StorageType.REMOTE)

        assertTrue(result is Result.Success)
        coVerify(exactly = 1) { remote.savePerformance(any()) }
    }

    @Test
    fun `storeSportPerformance REMOTE returns Network when remote throws IOException`() = runTest {
        val performance = mockk<SportPerformance>(relaxed = true)
        coEvery { remote.savePerformance(any()) } throws IOException("network")

        val result = repo.storeSportPerformance(performance, StorageType.REMOTE)

        assertTrue(result is Result.Error)
        assertEquals(DataError.Network, (result as Result.Error).error)
    }
}
