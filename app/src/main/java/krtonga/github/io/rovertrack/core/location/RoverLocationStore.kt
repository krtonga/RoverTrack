package krtonga.github.io.rovertrack.core.location

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import krtonga.github.io.rovertrack.core.db.AppDatabase
import krtonga.github.io.rovertrack.core.db.entity.LocationEntity
import krtonga.github.io.rovertrack.core.db.entity.RoverEntity
import krtonga.github.io.rovertrack.core.model.RoverLocationViewModel

class RoverLocationStore(private val database: AppDatabase,
                         private val sources: List<RoverLocationSource>) {

    // Merges Observables that admit save type vs. combineLatest which needs converter
    //val locations = Observable.merge(sources.map { it.locations })

    val locations = database.roverLocationDoa().getLatestLocations()
            .map { list -> list.map { roverAndLocations ->
                val mostRecentLocation = roverAndLocations.locationEntities!!.first()
                RoverLocationViewModel.create(
                        roverAndLocations.roverEntity!!,
                        mostRecentLocation)
            } }

    val permissions = sources.map { it.permissions }.flatten().toList()

    init {
        val locations = Observable.merge(sources.map { it.locations })
        locations
                .observeOn(Schedulers.io())
                .subscribe { ping ->
                    val roverEntity = getOrCreateRover(ping.phoneNumber)
                    val locationEntity = LocationEntity(roverEntity, ping.location)

                    // Check if location exists..
                    val existingLocation = database.locationDoa().existsByUniqueId(
                            LocationEntity.computeUniqueId(roverEntity, locationEntity.location))
                    if (existingLocation) {
                        return@subscribe
                    }
                    database.locationDoa().insert(locationEntity)
                }
    }

    fun permissionsGranted() {
        for (source in sources) {
            source.permissionsGranted()
        }
    }

    private fun getOrCreateRover(phoneNumber: String): RoverEntity {
        val existingRover = database.roverDoa().getRoverByPhone(phoneNumber)
        if (existingRover != null) {
            return existingRover
        }
        val newRover = RoverEntity(phoneNumber = phoneNumber)
        val newUserId = database.roverDoa().insert(newRover)
        return newRover.copy(id = newUserId)
    }
}