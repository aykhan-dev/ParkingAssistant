package ev.aykhan.parkingasistant

import android.app.Application
import ev.aykhan.parkingasistant.local.RoomManager
import ev.aykhan.parkingasistant.manager.PreferenceManager

class ParkingAssistantGlobalApp : Application() {

    override fun onCreate() {
        super.onCreate()
        PreferenceManager.init(applicationContext)
        RoomManager.init(applicationContext)
    }

}