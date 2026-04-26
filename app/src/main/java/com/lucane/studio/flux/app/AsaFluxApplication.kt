package com.lucane.studio.flux.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class AsaFluxApplication : Application() {

    @Inject
//    lateinit var testDataSeeder: TestDataSeeder

    override fun onCreate() {
        super.onCreate()

//        TODO REMOVE before production
//        kotlinx.coroutines.MainScope().launch {
//            testDataSeeder.seed()
//        }
    }
}