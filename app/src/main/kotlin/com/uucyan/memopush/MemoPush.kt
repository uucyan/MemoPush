package com.uucyan.memopush

/**
 * Created by Uucyan on 2017/09/09.
 */
import android.app.Application;
import io.realm.Realm;
import io.realm.RealmConfiguration;

class MemoPush : Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val config = RealmConfiguration.Builder().build()
        Realm.setDefaultConfiguration(config)
    }
}