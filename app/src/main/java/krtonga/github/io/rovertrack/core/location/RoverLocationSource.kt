package krtonga.github.io.rovertrack.core.location

import io.reactivex.Observable

interface RoverLocationSource {

    val permissions: List<String>

    val locations: Observable<LocationFromSource>

    fun permissionsGranted()
}