package by.bashlikovvv.pokemon.presentation

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        app = this
    }

    companion object {
        private lateinit var app: App
        val instance: App get() = app
    }
}