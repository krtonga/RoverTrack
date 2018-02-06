package krtonga.github.io.rovertrack.core.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import krtonga.github.io.rovertrack.core.db.converter.DateConverter
import krtonga.github.io.rovertrack.core.db.dao.LocationDao
import krtonga.github.io.rovertrack.core.db.dao.RoverDao
import krtonga.github.io.rovertrack.core.db.dao.RoverLocationDao
import krtonga.github.io.rovertrack.core.db.entity.RoverEntity
import krtonga.github.io.rovertrack.core.db.entity.LocationEntity

@Database(entities = [RoverEntity::class, LocationEntity::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun roverDoa(): RoverDao

    abstract fun locationDoa(): LocationDao

    abstract fun roverLocationDoa(): RoverLocationDao
}