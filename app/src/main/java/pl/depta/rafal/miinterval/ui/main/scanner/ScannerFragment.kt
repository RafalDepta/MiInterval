package pl.depta.rafal.miinterval.ui.main.scanner

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat.getSystemService
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import org.greenrobot.eventbus.EventBus
import pl.depta.rafal.miinterval.utils.AppConsts
import pl.depta.rafal.miinterval.R
import pl.depta.rafal.miinterval.BR
import pl.depta.rafal.miinterval.databinding.FragmentScannerBinding
import pl.depta.rafal.miinterval.events.DeviceFoundEvent
import pl.depta.rafal.miinterval.ui.base.BaseFragment
import javax.inject.Inject

class ScannerFragment : BaseFragment<FragmentScannerBinding, ScannerViewModel>() {

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory
    private lateinit var mViewModel: ScannerViewModel

    override val layoutId = R.layout.fragment_scanner

    private var mBinding: FragmentScannerBinding? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mScanning: Boolean = false
    private var mHandler: Handler? = null
    private var mLeScanner: BluetoothLeScanner? = null
    private var mSettings: ScanSettings? = null
    private var mFilters: MutableList<ScanFilter>? = null
    private var mDeviceList: MutableList<BluetoothDevice> = ArrayList()
    private var mListAdapter: ArrayAdapter<BluetoothDevice>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        mHandler = Handler()

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        val bluetoothManager = getSystemService(context!!, BluetoothManager::class.java)
        mBluetoothAdapter = bluetoothManager?.adapter

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(baseActivity, "Bluetooth not supported", Toast.LENGTH_SHORT).show()
            activity?.finish()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = viewDataBinding
        setUp()
    }

    private fun setUp() {
        mListAdapter = ArrayAdapter(baseActivity, android.R.layout.simple_list_item_1, mDeviceList)
        mBinding?.lvScanList?.adapter = mListAdapter
    }

    override fun onResume() {
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (mBluetoothAdapter == null || !mBluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        } else {
            var deviceFound = false
            mBluetoothAdapter?.bondedDevices?.forEach {
                if (it.name == "MI Band 2") {
                    Log.d("SCANNN", "Found device: ${it.name}")
                    /*  mDeviceList.add(it)
                      mListAdapter?.notifyDataSetChanged()*/
                    deviceFound = true
                    return@forEach
                }
            }

            if (!deviceFound) {
                mLeScanner = mBluetoothAdapter?.bluetoothLeScanner
                mSettings = ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                        .setNumOfMatches(1)
                        .build()
                val filter = ScanFilter.Builder()
                        .setDeviceName(AppConsts.MI_BAND)
                        .build()
                mFilters = ArrayList()
                mFilters?.add(filter)
                // Initializes list view adapter.
                scanLeDevice(true)
            } else {
                baseActivity?.finish()
            }

        }

        super.onResume()
    }

    private fun scanLeDevice(enable: Boolean) {
        mScanning = enable
        if (enable) {
            mHandler?.postDelayed({
                mLeScanner?.stopScan(mScanCallback)
                mScanning = false
            }, SCAN_PERIOD)

            mLeScanner?.startScan(mFilters, mSettings, mScanCallback)
        } else {
            mLeScanner?.stopScan(mScanCallback)
        }
        baseActivity?.invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)
        if (!mScanning) {
            menu?.findItem(R.id.menu_stop)?.isVisible = false
            menu?.findItem(R.id.menu_scan)?.isVisible = true
            menu?.findItem(R.id.menu_refresh)?.actionView = null
        } else {
            menu?.findItem(R.id.menu_stop)?.isVisible = true
            menu?.findItem(R.id.menu_scan)?.isVisible = false
            menu?.findItem(R.id.menu_refresh)?.setActionView(
                    R.layout.actionbar_progress)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_scan -> scanLeDevice(true)
            R.id.menu_stop -> scanLeDevice(false)
        }
        baseActivity?.invalidateOptionsMenu()
        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                baseActivity?.finish()
            } else {
                scanLeDevice(true)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
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
                mViewModel.saveDevice(it.address)
                EventBus.getDefault().post(DeviceFoundEvent(it.address))
                /*  val intent = Intent(baseActivity, DeviceControlActivity::class.java)
                  intent.putExtra(DeviceScanActivity.EXTRA_DEVICE_NAME, it.name)
                  intent.putExtra(DeviceScanActivity.EXTRA_DEVICE_ADDRESS, it.address)
                  intent.putExtra("device", it)
                  startActivity(intent)*/
            }

        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            Log.i("callbackType", "Scan batch result: $results")
        }
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 1
        // Stops scanning after 10 seconds.
        private const val SCAN_PERIOD: Long = 10000

        @JvmStatic
        fun newInstance() = ScannerFragment()
    }

    override fun getViewModel(): ScannerViewModel {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ScannerViewModel::class.java)
        return mViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }


}
