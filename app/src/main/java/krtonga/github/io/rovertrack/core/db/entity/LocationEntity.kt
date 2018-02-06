package krtonga.github.io.rovertrack.core.db.entity

import android.arch.persistence.room.*
import android.location.Location
import krtonga.github.io.rovertrack.core.kotlin.sha512
import java.util.*

@Entity(
        tableName = "locationEntities",
        indices = [
            Index("uniqueId", unique = true)
        ],
        foreignKeys = [
            ForeignKey(
                    entity = RoverEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["userId"],
                    onDelete = ForeignKey.CASCADE
            )
        ])
data class LocationEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,

        val userId: Long,

        val latitude:Double,

        val longitude:Double,

        val altitude:Double,

        val accuracy:Float,

        val bearing:Float,

        val time: Date,

        val uniqueId: String
) {
    constructor(roverEntity: RoverEntity, location: Location) : this(
            userId = roverEntity.id,
            latitude = location.latitude,
            longitude = location.longitude,
            altitude = location.longitude,
            accuracy = location.accuracy,
            bearing = location.bearing,
            time = Date(location.time),
            uniqueId = computeUniqueId(roverEntity, location)
    )

    @Ignore
    val location = Location("").apply {
        latitude = this@LocationEntity.latitude
        longitude = this@LocationEntity.longitude
        altitude = this@LocationEntity.altitude
        accuracy = this@LocationEntity.accuracy
        bearing = this@LocationEntity.bearing
        time = this@LocationEntity.time.time
    }

    companion object {
        fun computeUniqueId(roverEntity: RoverEntity, location: Location) =
                listOf(roverEntity.phoneNumber, location.time.toString())
                        .joinToString("|")
                        .sha512()
    }
}