package com.rdisoftware.chronobeat

import android.app.Application
import android.content.Context
import com.rdisoftware.chronobeat.di.initKoin
import org.koin.dsl.module

class ChronoBeatApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        val androidAppModule = module {
            single<Context> { this@ChronoBeatApplication }
            single<Application> { this@ChronoBeatApplication }
        }

        initKoin {
            printLogger()
            modules(androidAppModule)
        }
    }
}