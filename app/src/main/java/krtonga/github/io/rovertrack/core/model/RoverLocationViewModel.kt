package krtonga.github.io.rovertrack.core.model

import android.location.Location
import krtonga.github.io.rovertrack.core.db.entity.LocationEntity
import krtonga.github.io.rovertrack.core.db.entity.RoverEntity

data class RoverLocationViewModel(
        val displayName: String,
        val location: Location,
        val uniqueId: String
) {
    companion object {
        fun create(roverEntity: RoverEntity, locationEntity: LocationEntity): RoverLocationViewModel {
            // To format all international numbers correctly: https://github.com/googlei18n/libphonenumber/wiki/Android-Studio-setup
            // FIXME libphone
            val displayName = roverEntity.name ?: roverEntity.phoneNumber
            return RoverLocationViewModel(displayName, locationEntity.location, locationEntity.uniqueId)
        }
    }
}