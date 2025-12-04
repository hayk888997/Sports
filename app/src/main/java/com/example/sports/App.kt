package com.example.sports

import android.app.Application
import com.example.sports.data.di.dataModule
import com.example.sports.domain.di.domainModule
import com.example.sports.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                dataModule,
                domainModule,
                presentationModule
            )
        }
    }
}
