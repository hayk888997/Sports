package com.example.sports.domain.di

import com.example.sports.domain.usecase.GetSportsPerformancesUseCase
import com.example.sports.domain.usecase.StoreSportPerformanceUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { StoreSportPerformanceUseCase(get()) }
    factory { GetSportsPerformancesUseCase(get()) }
}
