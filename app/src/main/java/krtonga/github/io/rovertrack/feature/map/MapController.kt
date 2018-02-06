package krtonga.github.io.rovertrack.feature.map

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.archlifecycle.LifecycleRestoreViewOnCreateController
import com.jakewharton.rxrelay2.BehaviorRelay
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.tbruyelle.rxpermissions2.RxPermissions
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.kotlin.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import krtonga.github.io.rovertrack.R
import krtonga.github.io.rovertrack.feature.RoverTrackApp
import krtonga.github.io.rovertrack.core.location.RoverLocationStore
import timber.log.Timber

class MapController : LifecycleRestoreViewOnCreateController() {

    lateinit private var locationStore: RoverLocationStore
    lateinit private var mapView: MapView

    private val markerFactory = MarkerFactory()

//    private var map: MapboxMap? = null
    private val mapRelay = BehaviorRelay.create<MapboxMap>()

    private val markers = mutableMapOf<String, Marker>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        val view = inflater.inflate(R.layout.controller_map, container, false)

        mapView = view.findViewById(R.id.mapview)
        mapView.onCreate(savedViewState)
        mapView.getMapAsync { map ->
            mapRelay.accept(map)
        }

        return view
    }

    override fun onContextAvailable(context: Context) {
        super.onContextAvailable(context)
        locationStore = (context.applicationContext as RoverTrackApp).locationStore
    }

    override fun onAttach(view: View) {
        super.onAttach(view)

        mapRelay
                .autoDisposable(scope())
                .subscribe { map ->

                    locationStore.locations
                            .observeOn(AndroidSchedulers.mainThread())
                            .autoDisposable(scope()) // Automatically disposes on corresponding event (onDetach)
                            .subscribe { locations ->
                                for (viewModel in locations) {
                                    Timber.d("Got location! $viewModel")
//                                    val rover = roverAndLocations.roverEntity!!
//                                    val location = roverAndLocations.locationEntities!!.first()
                                    if (markers.containsKey(viewModel.uniqueId)) {
                                        val marker = markers[viewModel.uniqueId]!!
                                        map?.removeMarker(marker)
                                    }
                                    val marker = map.addMarker(markerFactory.markerOptionsForLocation(viewModel))
                                    markers[viewModel.uniqueId] = marker
                                }
                            }

                    val rxPermissions = RxPermissions(activity!!)
                    rxPermissions.request(*locationStore.permissions.toTypedArray())
                            .autoDisposable(scope())
                            .subscribe({granted ->
                                if (granted) {
                                    locationStore.permissionsGranted()
                                }
                            })
                }

    }

    override fun onActivityStarted(activity: Activity) {
        super.onActivityStarted(activity)
        mapView.onStart()
    }

    override fun onActivityPaused(activity: Activity) {
        super.onActivityPaused(activity)
        mapView.onPause()
    }

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        mapView.onResume()
    }

    override fun onActivityStopped(activity: Activity) {
        super.onActivityStopped(activity)
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}

