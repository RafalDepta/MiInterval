package pl.depta.rafal.miinterval.data.prefs

interface PrefsHelper {
    fun saveDevice(deviceAddress: String)
    fun getDevice(): String
    fun setReady(ready: Boolean)
}