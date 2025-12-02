package com.example.sports.data.repo

import com.example.sports.data.local.dao.SportsPerformanceDao
import com.example.sports.data.local.mapper.toDomain
import com.example.sports.data.local.mapper.toEntity
import com.example.sports.data.remote.FirebaseRemoteDataSource
import com.example.sports.data.remote.mapper.toDomain
import com.example.sports.data.remote.mapper.toDto
import com.example.sports.data.util.safeCall
import com.example.sports.data.util.safeFlow
import com.example.sports.domain.model.FilterType
import com.example.sports.domain.model.SportPerformance
import com.example.sports.domain.model.StorageType
import com.example.sports.domain.repo.SportsPerformanceRepo
import com.example.sports.domain.util.DataError
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import com.example.sports.domain.util.Result

class SportsPerformanceRepoImpl(
    private val dao: SportsPerformanceDao,
    private val remote: FirebaseRemoteDataSource
) : SportsPerformanceRepo {

//    override fun observePerformances(): Flow<Result<List<SportPerformance>>> =
//        safeFlow {
//            remote.observePerformances()
//                .map { list -> list.map { it.toDomain() } }
//        }

    override suspend fun getPerformances(filter: FilterType): Result<List<SportPerformance>> =
        when (filter) {
            FilterType.LOCAL -> getLocal()
            FilterType.REMOTE -> getRemote()
            FilterType.ALL -> coroutineScope {
                val remoteDeferred = async { getRemote() }
                val localDeferred = async { getLocal() }

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
            StorageType.LOCAL -> storeLocal(performance)
            StorageType.REMOTE -> storeRemote(performance)
        }
    }

    private suspend fun getLocal(): Result<List<SportPerformance>> =
        safeCall {
            dao.getAll().map { it.toDomain() }
        }

    private suspend fun getRemote(): Result<List<SportPerformance>> =
        safeCall {
            remote.getPerformances().map { it.toDomain() }
        }

    private suspend fun storeLocal(performance: SportPerformance): Result<Unit> =
        safeCall {
            dao.insert(performance.toEntity())
        }

    private suspend fun storeRemote(performance: SportPerformance): Result<Unit> =
        safeCall {
            remote.savePerformance(performance.toDto())
        }
}
