package krtonga.github.io.rovertrack.core.location.sms

import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.location.Location
import android.net.Uri
import android.provider.Telephony
import android.telephony.SmsMessage
import com.cantrowitz.rxbroadcast.RxBroadcast
import com.jakewharton.rxrelay2.PublishRelay
import krtonga.github.io.rovertrack.core.location.LocationFromSource
import krtonga.github.io.rovertrack.core.location.RoverLocationSource

class SmsRoverLocationSource(private val context: Context) : RoverLocationSource {

    override val permissions= listOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS)

    // Relay is like a subject, but can never terminate (has no onError or onComplete, only onNext)
    private val relay = PublishRelay.create<LocationFromSource>()

    // Makes Relay read only (returns an Observable)
    override val locations = relay.hide()

    override fun permissionsGranted() {
        val intentFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        intentFilter.priority = 1
        // FIXME: Remember to unregister somewhere...
        RxBroadcast.fromBroadcast(context, intentFilter)
                .filter { intent -> intent.extras != null }
                // FIXME .flatMapIterable { intent -> Telephony.Sms.Intents.getMessagesFromIntent(intent).toList() }
                .flatMapIterable { intent -> (intent.extras.get("pdus") as Array<Any>).toList() }
                .map { msg -> SmsMessage.createFromPdu(msg as ByteArray) }
                .filter { sms -> isRover(sms.displayMessageBody) }
                .map { sms -> LocationFromSource(sms.displayOriginatingAddress, readSms(sms.displayMessageBody)) }
                .subscribe(relay)

        readExistingSms()
    }

    fun readExistingSms() {
        val uriSms = Uri.parse("content://sms/inbox")
        val cursor = context.contentResolver.query(
                uriSms, arrayOf("_id", "address", "date", "body"), null, null, "date asc") ?: return
        cursor.use {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val address = cursor.getString(1)
                val body = cursor.getString(3)
                if (isRover(body)) {
                    relay.accept(LocationFromSource(address, readSms(body)))
                }
            }
        }
    }

    // functionally equivalent to static
    companion object {
        fun isRover(message: String): Boolean {
            return message.matches(".+;.+;.+;.+;\\d+".toRegex())
        }

        fun readSms(message: String): Location {
            val location = Location("sms")

            val parts = message.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            location.latitude = parts[0].toDouble()
            location.longitude = parts[1].toDouble()
            location.altitude = parts[2].toDouble()
            location.accuracy = parts[3].toFloat()
            location.speed = parts[4].toFloat()
            location.bearing = parts[5].toFloat()
            location.time = parts[6].toLong() * 1000

            return location
        }
    }
}
