package com.example.sports.data.repo

import android.database.sqlite.SQLiteException
import com.example.sports.data.local.dao.SportsPerformanceDao
import com.example.sports.data.local.mapper.toDomain
import com.example.sports.data.local.mapper.toEntity
import com.example.sports.data.remote.api.SportsApi
import com.example.sports.data.remote.mapper.toDomain
import com.example.sports.data.remote.mapper.toDto
import com.example.sports.domain.model.FilterType
import com.example.sports.domain.model.SportPerformance
import com.example.sports.domain.model.StorageType
import com.example.sports.domain.repo.SportsPerformanceRepo
import com.example.sports.domain.util.DataError
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import com.example.sports.domain.util.Result
import retrofit2.HttpException
import java.io.IOException

class SportsPerformanceRepoImpl(
    private val dao: SportsPerformanceDao,
    private val api: SportsApi
) : SportsPerformanceRepo {
    override suspend fun getPerformances(filter: FilterType): Result<List<SportPerformance>> =
        when (filter) {
            FilterType.LOCAL -> safeGetLocal()
            FilterType.REMOTE -> safeGetRemote()
            FilterType.ALL -> coroutineScope {
                val remoteDeferred = async { safeGetRemote() }
                val localDeferred = async { safeGetLocal() }

                val remote = remoteDeferred.await()
                val local = localDeferred.await()

                when {
                    local is Result.Error && remote is Result.Error -> {
                        Result.Error(
                            DataError.Unknown(
                                Exception("Both sources failed")
                            )
                        )
                    }

                    local is Result.Success && remote is Result.Success -> {
                        Result.Success(local.data + remote.data)
                    }

                    local is Result.Success -> local
                    remote is Result.Success -> remote
                    else -> Result.Error(DataError.Unknown(Exception("Unknown state")))
                }
            }
        }

    override suspend fun storeSportPerformance(
        performance: SportPerformance,
        storageType: StorageType
    ): Result<Unit> {
        return when (storageType) {
            StorageType.LOCAL -> safeStoreLocal(performance)
            StorageType.REMOTE -> safeStoreRemote(performance)
        }
    }

    private suspend fun safeGetLocal(): Result<List<SportPerformance>> = try {
        val data = dao.getAll().map { it.toDomain() }
        Result.Success(data)
    } catch (e: Exception) {
        Result.Error(mapException(e))
    }

    private suspend fun safeGetRemote(): Result<List<SportPerformance>> = try {
        val data = api.getPerformances().map { it.toDomain() }
        Result.Success(data)
    } catch (e: Exception) {
        Result.Error(mapException(e))
    }

    private suspend fun safeStoreLocal(performance: SportPerformance): Result<Unit> = try {
        dao.insert(performance.toEntity())
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(mapException(e))
    }

    private suspend fun safeStoreRemote(performance: SportPerformance): Result<Unit> = try {
        api.savePerformance(performance.toDto())
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(mapException(e))
    }


    private fun mapException(e: Exception): DataError = when (e) {
        is IOException -> DataError.Network
        is HttpException -> if (e.code() == 404) DataError.NotFound else DataError.Unknown(e)
        is SQLiteException -> DataError.Database
        else -> DataError.Unknown(e)
    }
}
