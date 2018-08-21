package pl.depta.rafal.miinterval

import java.util.*

class CustomBluetoothProfile {
    object Basic {
        var service = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb")
        var batteryCharacteristic = UUID.fromString("00000006-0000-3512-2118-0009af100700")
    }

    object AlertNotification {
        var service = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb")
        var alertCharacteristic = UUID.fromString("00002a06-0000-1000-8000-00805f9b34fb")

        //Custom service 3 components
        var CUSTOM_SERVICE_FEE1 = UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb")
        var CUSTOM_SERVICE_AUTH_CHARACTERISTIC = UUID.fromString("00000009-0000-3512-2118-0009af100700")
        var CUSTOM_SERVICE_AUTH_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }

    object HeartRate {
        var service = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb")
        var measurementCharacteristic = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb")
        var descriptor = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
        var controlCharacteristic = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb")
    }


}