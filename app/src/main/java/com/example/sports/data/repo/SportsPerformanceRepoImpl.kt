package com.example.sports.data.repo

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
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class SportsPerformanceRepoImpl(
    private val dao: SportsPerformanceDao,
    private val api: SportsApi
) : SportsPerformanceRepo {
    override suspend fun getPerformances(filterType: FilterType): List<SportPerformance> {
        return when (filterType) {
            FilterType.LOCAL -> dao.getAll().map { it.toDomain() }
            FilterType.REMOTE -> api.getPerformances().map { it.toDomain() }
            FilterType.ALL -> coroutineScope {
                val remoteDeferred = async { api.getPerformances().map { it.toDomain() } }
                val localDeferred = async { dao.getAll().map { it.toDomain() } }
                localDeferred.await() + remoteDeferred.await()
            }
        }
    }

    override suspend fun storeSportPerformance(
        performance: SportPerformance,
        storageType: StorageType
    ) {
        when (storageType) {
            StorageType.LOCAL -> dao.insert(performance.toEntity())
            StorageType.REMOTE -> api.savePerformance(performance.toDto())
        }
    }
}
