package com.example.sports.data.di

import androidx.room.Room
import com.example.sports.data.local.db.AppDatabase
import com.example.sports.data.remote.api.SportsApi
import com.example.sports.data.repo.SportsPerformanceRepoImpl
import com.example.sports.domain.repo.SportsPerformanceRepo
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    // Database
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "sports_performance_db"
        ).build()
    }

    single { get<AppDatabase>().performanceDao() }

    // Retrofit API
    single<SportsApi> {
        Retrofit.Builder()
            .baseUrl("https://your-api/") // Replace with actual base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SportsApi::class.java)
    }

    // Repository
    single<SportsPerformanceRepo> {
        SportsPerformanceRepoImpl(
            dao = get(),
            api = get()
        )
    }
}
