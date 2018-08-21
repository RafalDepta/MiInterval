package pl.depta.rafal.miinterval.ui.main

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.bluetooth.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import pl.depta.rafal.miinterval.R
import pl.depta.rafal.miinterval.BR
import pl.depta.rafal.miinterval.CustomBluetoothProfile
import pl.depta.rafal.miinterval.databinding.ActivityMainBinding
import pl.depta.rafal.miinterval.events.ControlDeviceEvent
import pl.depta.rafal.miinterval.events.DeviceFoundEvent
import pl.depta.rafal.miinterval.events.NavigationEvent
import pl.depta.rafal.miinterval.ui.base.BaseActivity
import pl.depta.rafal.miinterval.ui.main.intervals.IntervalsListFragment
import pl.depta.rafal.miinterval.ui.main.newinterval.NewIntervalFragment
import pl.depta.rafal.miinterval.ui.main.scanner.ScannerFragment
import pl.depta.rafal.miinterval.utils.AppUtils.Companion.fromUint8
import pl.depta.rafal.miinterval.utils.AppUtils.Companion.fromUint8s
import java.io.ByteArrayOutputStream
import java.util.*
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    lateinit var mViewModel: MainViewModel
    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory
    private var mBinding: ActivityMainBinding? = null
    override val layoutId = R.layout.activity_main

    private val mBluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()


    companion object {
        const val REQUEST_ENABLE_BT = 1
        var mGatt: BluetoothGatt? = null

        fun startVibrate(vibration:Int, pause:Int) {
            try {
                val repeat = 1
                val service = mGatt?.getService(CustomBluetoothProfile.AlertNotification.service)
                val characteristic = service?.getCharacteristic(CustomBluetoothProfile.AlertNotification.alertCharacteristic)
                characteristic?.value = byteArrayOf(-1, (vibration and 255).toByte(), ((vibration shr 8 and 255).toByte()), (pause and 255).toByte(), (pause shr 8 and 255).toByte(), repeat.toByte())
                mGatt?.setCharacteristicNotification(characteristic, true)
                if (!mGatt?.writeCharacteristic(characteristic)!!) {
                   // Toast.makeText(this, "Failed start vibrate", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = viewDataBinding
        setSupportActionBar(toolbar)


        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE not supported", Toast.LENGTH_SHORT).show()
            finish()
        }

        if (savedInstanceState == null) {
            if (mViewModel.getLastDevice().isEmpty()) {
                loadFragment(ScannerFragment.newInstance())
            } else {
                loadFragment(IntervalsListFragment.newInstance(mViewModel.getLastDevice()))
                startConnecting(mViewModel.getLastDevice())
            }
        }
        subscribeLiveData()

        /*   val intent = Intent(this, DeviceControlActivity::class.java)
           intent.putExtra(DeviceScanActivity.EXTRA_DEVICE_NAME, "Band 2")
           intent.putExtra(DeviceScanActivity.EXTRA_DEVICE_ADDRESS, "")
           //intent.putExtra("device", it)
           startActivity(intent)*/
    }

    private fun subscribeLiveData() {
        mViewModel.intervalCreated.observe(this, android.arch.lifecycle.Observer {
            it?.let {
                openAddIntervalFragment(it)
            }

        })
    }

    override fun onResume() {
        super.onResume()

        if (!mBluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            return
        }

        if (mGatt == null)
            startConnecting(mViewModel.getLastDevice())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                finish()
            } else {
                startConnecting(mViewModel.getLastDevice())
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        mGatt?.close()
        super.onDestroy()
    }

    private fun startConnecting(lastDeviceAddress: String) {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val mBluetoothDevice = bluetoothAdapter.getRemoteDevice(lastDeviceAddress)

        Log.v("test", "Connecting to $lastDeviceAddress")
        Log.v("test", "Device name " + mBluetoothDevice?.name)

        mGatt = mBluetoothDevice?.connectGatt(this, true, bluetoothGattCallback)
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
                    mGatt?.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTING -> {
                    Log.d("BT", "DEVICE DISCONNECTING")
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    Log.d("BT", "DEVICE DISCONNECTED")
                    mViewModel.setDeviceReady(false)
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            Log.v("test", "onServicesDiscovered")
            //gatt.device.createBond()
            //authoriseMiBand()
            mViewModel.setDeviceReady(true)
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            Log.v("test", "onCharacteristicRead")
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
            Log.v("test", "onCharacteristicWrite")
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            Log.v("test", "onCharacteristicChanged")

            /*  when (characteristic.uuid.toString()) {
                  "00000009-0000-3512-2118-0009af100700" -> executeAuthorisationSequence(characteristic)
              }*/
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

    private fun authoriseMiBand() {
        val service = mGatt?.getService(CustomBluetoothProfile.AlertNotification.CUSTOM_SERVICE_FEE1)

        val characteristic = service?.getCharacteristic(CustomBluetoothProfile.AlertNotification.CUSTOM_SERVICE_AUTH_CHARACTERISTIC)
        mGatt?.setCharacteristicNotification(characteristic, true)
        for (descriptor in characteristic!!.descriptors) {
            if (descriptor.uuid == CustomBluetoothProfile.AlertNotification.CUSTOM_SERVICE_AUTH_DESCRIPTOR) {
                Log.d("INFO", "Found NOTIFICATION BluetoothGattDescriptor: " + descriptor.uuid.toString())
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                mGatt?.writeDescriptor(descriptor)
            }
        }

        characteristic.value = byteArrayOf(0x01, 0x8, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x40, 0x41, 0x42, 0x43, 0x44, 0x45)
        //characteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT

        mGatt?.writeCharacteristic(characteristic)
    }


    fun startVibrate() {
        try {
            val vibration = 200
            val pause = 500
            val repeat = 1
            val service = mGatt?.getService(CustomBluetoothProfile.AlertNotification.service)
            val characteristic = service?.getCharacteristic(CustomBluetoothProfile.AlertNotification.alertCharacteristic)
            characteristic?.value = byteArrayOf(-1, (vibration and 255).toByte(), ((vibration shr 8 and 255).toByte()), (pause and 255).toByte(), (pause shr 8 and 255).toByte(), repeat.toByte())
            mGatt?.setCharacteristicNotification(characteristic, true)
            if (!mGatt?.writeCharacteristic(characteristic)!!) {
                Toast.makeText(this, "Failed start vibrate", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopVibrate() {
        val service = mGatt?.getService(CustomBluetoothProfile.AlertNotification.service)
        val characteristic = service?.getCharacteristic(CustomBluetoothProfile.AlertNotification.alertCharacteristic)
        mGatt?.setCharacteristicNotification(characteristic, true)
        characteristic?.value = byteArrayOf(0)
        if (!mGatt?.writeCharacteristic(characteristic)!!) {
            Toast.makeText(this, "Failed stop vibrate", Toast.LENGTH_SHORT).show()
        }
    }

    fun test() {
        try {
            val bchar = mGatt?.getService(UUID.fromString((String.format("0000%s-0000-1000-8000-00805f9b34fb", "1811"))))
                    ?.getCharacteristic(UUID.fromString((String.format("0000%s-0000-1000-8000-00805f9b34fb", "2A46"))))
            val stream = ByteArrayOutputStream(100)
            stream.write(fromUint8(3).toInt())
            stream.write(fromUint8(1).toInt())
            stream.write(fromUint8s("Rafal_123456789012"))

            bchar?.value = stream.toByteArray()
            if (!mGatt?.writeCharacteristic(bchar)!!) {
                Toast.makeText(this, "Failed start test", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Suppress("Unused")
    @Subscribe(sticky = true)
    fun onEvent(deviceFoundEvent: DeviceFoundEvent) {
        loadFragment(IntervalsListFragment.newInstance(deviceFoundEvent.deviceAddress))
        EventBus.getDefault().removeStickyEvent(deviceFoundEvent)
    }

    @Suppress("Unused")
    @Subscribe(sticky = true)
    fun onEvent(controlDeviceEvent: ControlDeviceEvent) {
        when (controlDeviceEvent.deviceEventType) {
            ControlDeviceEvent.DeviceEventType.START_INTERVAL -> {
                startVibrate()
            }
            ControlDeviceEvent.DeviceEventType.STOP_INTERVAL -> {
                stopVibrate()
            }
            ControlDeviceEvent.DeviceEventType.PAUSE_INTERVAL -> {
                test()
            }
        }

        EventBus.getDefault().removeStickyEvent(controlDeviceEvent)
    }

    @Suppress("Unused")
    @Subscribe(sticky = true)
    fun onEvent(navigationEvent: NavigationEvent) {
        when (navigationEvent.intervalId) {
            NewIntervalFragment.CREATE_INTERVAL -> {
                mViewModel.createNewInterval()
            }
            else -> openAddIntervalFragment(navigationEvent.intervalId)
        }

        EventBus.getDefault().removeStickyEvent(navigationEvent)
    }

    private fun openAddIntervalFragment(intervalId: Long) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(mBinding?.frameContainer?.id!!, NewIntervalFragment.newInstance(intervalId))
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(mBinding?.frameContainer?.id!!, fragment)
        transaction.commit()
    }

    override fun onStart() {
        super.onStart()
        mGatt?.connect()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onStop() {
        mGatt?.disconnect()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }

        super.onStop()
    }


    override fun getViewModel(): MainViewModel {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(MainViewModel::class.java)
        return mViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

}
