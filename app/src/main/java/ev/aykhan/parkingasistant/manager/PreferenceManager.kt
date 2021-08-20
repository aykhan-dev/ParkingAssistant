package ev.aykhan.parkingasistant.manager

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object PreferenceManager {

    private const val PREFERENCES_KEY = "Parking Assistant SharedPref"

    private const val IS_FIRST_LAUNCH_KEY = "is first launch of the app"

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        if (!::sharedPreferences.isInitialized)
            sharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
    }

    var isFirstLaunch: Boolean
        get() = sharedPreferences.getBoolean(IS_FIRST_LAUNCH_KEY, true)
        set(value) {
            sharedPreferences.edit {
                putBoolean(IS_FIRST_LAUNCH_KEY, value)
            }
        }

}