package krtonga.github.io.rovertrack.core.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import io.reactivex.Single
import krtonga.github.io.rovertrack.core.db.entity.LocationEntity
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import krtonga.github.io.rovertrack.core.db.entity.RoverEntity


@Dao
interface LocationDao {
//    @Query("SELECT * FROM locations GROUP BY userId ORDER BY time ASC")
//    fun getLatestLocations(): Single<List<RoverLocation>>

    @Query("SELECT * FROM locationEntities WHERE userId = :userId ORDER BY time ASC")
    fun getHistory(userId: String): Single<List<LocationEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM locationEntities " +
            "WHERE uniqueId = :uniqueId LIMIT 1)")
    fun existsByUniqueId(uniqueId: String): Boolean

    @Insert
    fun insertAll(vararg locationEntities: LocationEntity)

    @Insert
    fun insert(location: LocationEntity): Long

    @Delete
    fun delete(locationEntity: LocationEntity)
}