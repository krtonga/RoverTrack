package krtonga.github.io.rovertrack.feature

import android.app.Application
import android.arch.persistence.room.Room
import com.mapbox.mapboxsdk.Mapbox
import krtonga.github.io.rovertrack.BuildConfig
import krtonga.github.io.rovertrack.R
import krtonga.github.io.rovertrack.core.db.AppDatabase
import krtonga.github.io.rovertrack.core.location.RoverLocationSource
import krtonga.github.io.rovertrack.core.location.RoverLocationStore
import krtonga.github.io.rovertrack.core.location.sms.SmsRoverLocationSource
import timber.log.Timber

class RoverTrackApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Mapbox.getInstance(applicationContext, getString(R.string.mapbox_token))
    }

    val locationStore by lazy {
        RoverLocationStore(database,
                listOf<RoverLocationSource>(SmsRoverLocationSource(this)))
    }

    val database by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "roverEntity-db")
                .build()
    }
}