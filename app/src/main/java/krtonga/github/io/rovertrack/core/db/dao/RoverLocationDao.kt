package krtonga.github.io.rovertrack.core.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import io.reactivex.Flowable
import krtonga.github.io.rovertrack.core.db.entity.RoverAndLocations

@Dao
interface RoverLocationDao {
    @Query("SELECT * FROM rovers")
    fun getLatestLocations(): Flowable<List<RoverAndLocations>>
}