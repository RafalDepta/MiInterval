package pl.depta.rafal.miinterval.events

class ControlDeviceEvent (val deviceEventType: DeviceEventType){
    enum class DeviceEventType {
        START_INTERVAL,
        STOP_INTERVAL,
        PAUSE_INTERVAL
    }
}

