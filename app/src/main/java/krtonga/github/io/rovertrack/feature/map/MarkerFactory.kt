package krtonga.github.io.rovertrack.feature.map

import android.telephony.PhoneNumberUtils
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import krtonga.github.io.rovertrack.core.model.RoverLocationViewModel
import java.text.DateFormat

class MarkerFactory {

    fun markerOptionsForLocation(locationViewModel: RoverLocationViewModel): MarkerOptions {
        return MarkerOptions()
                .position(LatLng(locationViewModel.location))
                .title(PhoneNumberUtils.formatNumber(locationViewModel.displayName))
                .snippet(DATE_FORMAT.format(locationViewModel.location.time))
    }

    companion object {
        private val DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
    }
}