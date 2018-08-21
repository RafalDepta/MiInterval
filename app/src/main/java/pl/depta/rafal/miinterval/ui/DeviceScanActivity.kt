package pl.depta.rafal.miinterval.ui

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.*
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import pl.depta.rafal.miinterval.R


class DeviceScanActivity : AppCompatActivity() {
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mScanning: Boolean = false
    private var mHandler: Handler? = null
    private var mLeScanner: BluetoothLeScanner? = null
    private var mSettings: ScanSettings? = null
    private var mFilters: MutableList<ScanFilter>? = null

    private val REQUEST_ENABLE_BT = 1
    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000


    companion object {
        val EXTRA_DEVICE_NAME = "device_name"
        val EXTRA_DEVICE_ADDRESS = "device_address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_device)

        mHandler = Handler()

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE not supported", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager?
        mBluetoothAdapter = bluetoothManager?.adapter

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        if (!mScanning) {
            menu.findItem(R.id.menu_stop).isVisible = false
            menu.findItem(R.id.menu_scan).isVisible = true
            menu.findItem(R.id.menu_refresh).actionView = null
        } else {
            menu.findItem(R.id.menu_stop).isVisible = true
            menu.findItem(R.id.menu_scan).isVisible = false
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_progress)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_scan -> scanLeDevice(true)
            R.id.menu_stop -> scanLeDevice(false)
        }
        invalidateOptionsMenu()
        return true
    }

    override fun onResume() {
        super.onResume()

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (mBluetoothAdapter == null || !mBluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        } else {
            mBluetoothAdapter?.bondedDevices?.forEach {
                if (it.address == "MI Band 2") {
                    Log.d("SCANNN", "Found device: ${it.name}")
                }
            }

            mLeScanner = mBluetoothAdapter?.bluetoothLeScanner
            mSettings = ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                    .setNumOfMatches(1)
                    .build()
            val filter = ScanFilter.Builder()
                    .setDeviceName("MI Band 2")
                    .build()
            mFilters = ArrayList()
            mFilters?.add(filter)
            // Initializes list view adapter.
            scanLeDevice(true)
        }
    }

    private fun scanLeDevice(enable: Boolean) {
        mScanning = enable
        if (enable) {
            mHandler?.postDelayed({
                mLeScanner?.stopScan(mScanCallback)
            }, SCAN_PERIOD)

            mLeScanner?.startScan(mFilters, mSettings, mScanCallback)
        } else {
            mLeScanner?.stopScan(mScanCallback)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                finish()
            } else {
                scanLeDevice(true)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPause() {
        scanLeDevice(false)
        super.onPause()
    }


    private val mScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanFailed(errorCode: Int) {
            Log.i("callbackType", "Scan failed")
        }

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            Log.i("callbackType", callbackType.toString())
            Log.i("result", result.toString())
            val btDevice = result?.device

            btDevice?.let {
                scanLeDevice(false)
                val intent = Intent(this@DeviceScanActivity, DeviceControlActivity::class.java)
                intent.putExtra(EXTRA_DEVICE_NAME, it.name)
                intent.putExtra(EXTRA_DEVICE_ADDRESS, it.address)
                intent.putExtra("device", it)
                startActivity(intent)
                finish()
            }

        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            Log.i("callbackType", "Scan batch result: $results")
        }
    }
}