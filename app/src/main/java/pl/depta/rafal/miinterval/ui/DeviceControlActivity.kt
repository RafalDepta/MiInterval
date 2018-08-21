package pl.depta.rafal.miinterval.ui

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.Toast
import pl.depta.rafal.miinterval.ui.DeviceScanActivity.Companion.EXTRA_DEVICE_ADDRESS
import pl.depta.rafal.miinterval.ui.DeviceScanActivity.Companion.EXTRA_DEVICE_NAME
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothGattCallback
import pl.depta.rafal.miinterval.CustomBluetoothProfile
import pl.depta.rafal.miinterval.R
import android.bluetooth.BluetoothGattService
import android.media.AudioManager
import com.google.android.gms.common.util.ArrayUtils
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.ArrayList


class DeviceControlActivity : AppCompatActivity() {

    var bluetoothAdapter: BluetoothAdapter? = null
    var bluetoothGatt: BluetoothGatt? = null
    var bluetoothDevice: BluetoothDevice? = null
    var deviceVibration: Button? = null
    var deviceStopVibration: Button? = null
    private var mDeviceName = ""
    private var mDeviceAddress = ""
    private var mDevicePhysicalAddress = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_control)

        initializeObjects()
        initilaizeComponents()

        getBoundedDevice()

        startConnecting()
    }

    private fun initilaizeComponents() {
        deviceVibration = findViewById(R.id.device_vibration)
        deviceVibration?.setOnClickListener {
            startVibrate()
        }

        deviceStopVibration = findViewById(R.id.device_vibration_stop)
        deviceStopVibration?.setOnClickListener {
            stopVibrate()
        }

        findViewById<Button>(R.id.device_test).setOnClickListener {
            test()
        }
        findViewById<Button>(R.id.device_test2).setOnClickListener {
            test2()
        }
    }

    fun getBoundedDevice() {

        mDeviceName = intent.getStringExtra(EXTRA_DEVICE_NAME)
        mDeviceAddress = intent.getStringExtra(EXTRA_DEVICE_ADDRESS)
        mDevicePhysicalAddress = mDeviceAddress
    }

    fun initializeObjects() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    fun startVibrate() {
        try {
            val service = bluetoothGatt?.getService(CustomBluetoothProfile.AlertNotification.service)
            val characteristic = service?.getCharacteristic(CustomBluetoothProfile.AlertNotification.alertCharacteristic)
            bluetoothGatt?.setCharacteristicNotification(characteristic, true)
            characteristic?.value = byteArrayOf(3)
            if (!bluetoothGatt?.writeCharacteristic(characteristic)!!) {
                Toast.makeText(this, "Failed start vibrate", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun test() {
        try {
            val bchar = bluetoothGatt?.getService(UUID.fromString((String.format("0000%s-0000-1000-8000-00805f9b34fb", "1811"))))
                    ?.getCharacteristic(UUID.fromString((String.format("0000%s-0000-1000-8000-00805f9b34fb", "2A46"))))
            val stream = ByteArrayOutputStream(20)
            stream.write(fromUint8(8).toInt())
            stream.write(fromUint8(1).toInt())
            stream.write(fromUint8s("Ra"))

            bchar?.value = stream.toByteArray()
            if (!bluetoothGatt?.writeCharacteristic(bchar)!!) {
                Toast.makeText(this, "Failed start test", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun test2() {
        try {
            var timer = CountDownLatch(1)
            val bchar = bluetoothGatt?.getService(CustomBluetoothProfile.AlertNotification.service)
                    ?.getCharacteristic(CustomBluetoothProfile.AlertNotification.alertCharacteristic)
            bchar?.value = byteArrayOf(3)

            Log.d("test", "1 vibration")
            if (!bluetoothGatt?.writeCharacteristic(bchar)!!) {
                Toast.makeText(this, "Failed start vibrate", Toast.LENGTH_SHORT).show()
            }

            timer.await(3, TimeUnit.SECONDS)
            timer.countDown()
            Log.d("test", "2 vibration")
            bchar?.value = byteArrayOf(0x2)
            if (!bluetoothGatt?.writeCharacteristic(bchar)!!) {
                Toast.makeText(this, "Failed start test", Toast.LENGTH_SHORT).show()
            }
            timer = CountDownLatch(1)
            timer.await(4, TimeUnit.SECONDS)
            timer.countDown()

            Log.d("test", "stop vibration")
            bchar?.value = byteArrayOf(0x0)
            if (!bluetoothGatt?.writeCharacteristic(bchar)!!) {
                Toast.makeText(this, "Failed start test", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun fromUint8(value: Int): Byte {
        return (value and 0xff).toByte()

    }

    fun fromUint8s(message: String): ByteArray {
        return message.toByteArray(StandardCharsets.UTF_8)

    }

    fun stopVibrate() {
        val service = bluetoothGatt?.getService(CustomBluetoothProfile.AlertNotification.service)
        val characteristic = service?.getCharacteristic(CustomBluetoothProfile.AlertNotification.alertCharacteristic)
        bluetoothGatt?.setCharacteristicNotification(characteristic, true)
        characteristic?.value = byteArrayOf(0)
        if (!bluetoothGatt?.writeCharacteristic(characteristic)!!) {
            Toast.makeText(this, "Failed stop vibrate", Toast.LENGTH_SHORT).show()
        }
    }

    fun startConnecting() {
        bluetoothDevice = bluetoothAdapter?.getRemoteDevice(mDevicePhysicalAddress)

        Log.v("test", "Connecting to $mDevicePhysicalAddress")
        Log.v("test", "Device name " + bluetoothDevice?.name)

        bluetoothGatt = bluetoothDevice?.connectGatt(this, true, bluetoothGattCallback)
    }

    private fun authoriseMiBand() {
        val service = bluetoothGatt?.getService(CustomBluetoothProfile.AlertNotification.CUSTOM_SERVICE_FEE1)

        val characteristic = service?.getCharacteristic(CustomBluetoothProfile.AlertNotification.CUSTOM_SERVICE_AUTH_CHARACTERISTIC)
        bluetoothGatt?.setCharacteristicNotification(characteristic, true)
        for (descriptor in characteristic!!.descriptors) {
            if (descriptor.uuid == CustomBluetoothProfile.AlertNotification.CUSTOM_SERVICE_AUTH_DESCRIPTOR) {
                Log.d("INFO", "Found NOTIFICATION BluetoothGattDescriptor: " + descriptor.uuid.toString())
                descriptor.value = byteArrayOf(4)//BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                bluetoothGatt?.writeDescriptor(descriptor)
            }
        }

        characteristic.value = byteArrayOf(0x01, 0x8, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x40, 0x41, 0x42, 0x43, 0x44, 0x45)
        //characteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT

        bluetoothGatt?.writeCharacteristic(characteristic)
    }


    private fun executeAuthorisationSequence(characteristic: BluetoothGattCharacteristic) {
        val value = characteristic.value
        if (value[0].toInt() == 0x10 && value[1].toInt() == 0x01 && value[2].toInt() == 0x01) {
            characteristic.value = byteArrayOf(0x02, 0x8)
            bluetoothGatt?.writeCharacteristic(characteristic)
        } else if (value[0].toInt() == 0x10 && value[1].toInt() == 0x02 && value[2].toInt() == 0x01) {
            try {
                val tmpValue = Arrays.copyOfRange(value, 3, 19)
                val cipher = Cipher.getInstance("AES/ECB/NoPadding")

                val key = SecretKeySpec(byteArrayOf(0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x40, 0x41, 0x42, 0x43, 0x44, 0x45), "AES")

                cipher.init(Cipher.ENCRYPT_MODE, key)
                val bytes = cipher.doFinal(tmpValue)

                val list = ArrayList<Byte>()
                list.addAll(listOf(0x03, 0x8))
                list.addAll(bytes.toList())

                characteristic.value = list.toByteArray()
                bluetoothGatt?.writeCharacteristic(characteristic)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private val bluetoothGattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            Log.v("test", "onConnectionStateChange")

            when (newState) {
                BluetoothProfile.STATE_CONNECTING -> {
                    Log.d("BT", "DEVICE CONNECTING")
                }
                BluetoothProfile.STATE_CONNECTED -> {
                    Log.d("BT", "DEVICE CONNECTED")
                    bluetoothGatt?.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTING -> {
                    Log.d("BT", "DEVICE DISCONNECTING")
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    Log.d("BT", "DEVICE DISCONNECTED")
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            Log.v("test", "onServicesDiscovered")
            authoriseMiBand()
            //gatt.device.createBond()
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            Log.v("test", "onCharacteristicRead")
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            Log.v("test", "onCharacteristicWrite")
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            Log.v("test", "onCharacteristicChanged")

            when (characteristic.uuid.toString()) {
                "00000009-0000-3512-2118-0009af100700" -> executeAuthorisationSequence(characteristic)
            }
        }

        override fun onDescriptorRead(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
            Log.v("test", "onDescriptorRead")
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
            Log.v("test", "onDescriptorWrite")
        }

        override fun onReliableWriteCompleted(gatt: BluetoothGatt, status: Int) {
            Log.v("test", "onReliableWriteCompleted")
        }

        override fun onReadRemoteRssi(gatt: BluetoothGatt, rssi: Int, status: Int) {
            Log.v("test", "onReadRemoteRssi")
        }

        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
            Log.v("test", "onMtuChanged")
        }

    }

}
