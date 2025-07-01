package com.globalwallet.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GlobalWalletApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Initialize any application-level components here
    }
} 