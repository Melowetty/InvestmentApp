package com.melowetty.investment

import android.app.Application
import androidx.room.Room
import com.melowetty.investment.database.AppDatabase


class App: Application() {

    var instance: App? = null

    private var database: AppDatabase? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(this, AppDatabase::class.java, "database")
            .build()
    }

}