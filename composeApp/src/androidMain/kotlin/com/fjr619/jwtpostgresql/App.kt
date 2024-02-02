package com.fjr619.jwtpostgresql

import android.app.Application
import di.initKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}