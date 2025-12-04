package com.example.sports.data.di

import androidx.room.Room
import com.example.sports.data.local.db.AppDatabase
import com.example.sports.data.remote.FirebaseRemoteDataSource
import com.example.sports.data.remote.FirebaseService
import com.example.sports.data.repo.SportsPerformanceRepoImpl
import com.example.sports.domain.repo.SportsPerformanceRepo
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val dataModule = module {

    //Remote
    single { FirebaseFirestore.getInstance() }
    single { FirebaseService(get()) }
    single { FirebaseRemoteDataSource(get()) }

    // Local
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "sports_performance_db"
        ).build()
    }
    single { get<AppDatabase>().performanceDao() }

    // Repository
    single<SportsPerformanceRepo> {
        SportsPerformanceRepoImpl(
            dao = get(),
            remote = get()
        )
    }
}
