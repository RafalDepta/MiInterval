package pl.depta.rafal.miinterval.data.prefs

import android.content.SharedPreferences
import pl.depta.rafal.miinterval.annotation.DefaultPreference
import javax.inject.Inject

class AppPrefsHelper @Inject constructor(@DefaultPreference val sp: SharedPreferences) : PrefsHelper {
    companion object {

        const val DEVICE_ADDRESS = "device_address"
        const val IS_DEVICE_RADY = "is_device_ready"

    }
    override fun getDevice(): String {
      return sp.getString(DEVICE_ADDRESS, "")
    }

    override fun saveDevice(deviceAddress: String) {
        sp.edit().putString(DEVICE_ADDRESS, deviceAddress).apply()
    }

    override fun setReady(ready: Boolean) {
        sp.edit().putBoolean(IS_DEVICE_RADY, ready).apply()
    }
}