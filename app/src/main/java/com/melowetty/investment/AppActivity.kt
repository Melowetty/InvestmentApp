package com.melowetty.investment

import android.app.Application
import androidx.room.Room
import com.melowetty.investment.database.AppDatabase
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig


class AppActivity: Application() {

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

        val config: YandexMetricaConfig = YandexMetricaConfig.newConfigBuilder("b5c7ba7c-4ed3-42a1-a570-187f9a1031d7").build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)
    }
    companion object {
        private var db: AppDatabase? = null
        fun getDatabase(): AppDatabase? {
            return db
        }
    }

}