package com.example.chatequipefrance

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class ChatApplication : Application() {
    override fun onCreate() {
        super.onCreate()
//        Bloqué le mode sombre
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}