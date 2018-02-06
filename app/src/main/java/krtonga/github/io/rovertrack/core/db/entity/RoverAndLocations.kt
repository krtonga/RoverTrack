package krtonga.github.io.rovertrack.core.db.entity

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

//data class RoverAndLocations(
//        @Embedded
//        val rover: Rover,
//
//        @Relation(parentColumn = "id", entityColumn = "userId")
//        val locations: List<RoverLocation>
//)

//TODO disaster
data class RoverAndLocations(
        @Embedded
        var roverEntity: RoverEntity? = null,

        @Relation(parentColumn = "id", entityColumn = "userId")
        var locationEntities: List<LocationEntity>? = null

        // TODO: One to one relationship for lastLocation
)