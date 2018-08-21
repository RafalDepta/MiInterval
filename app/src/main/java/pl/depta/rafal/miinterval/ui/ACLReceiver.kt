package pl.depta.rafal.miinterval.ui

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ACLReceiver : BroadcastReceiver() {
    companion object {
        const val TAG = "ACLReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action

        when (action) {
            BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED -> {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR)
                when (state) {
                    BluetoothAdapter.STATE_OFF -> {
                        Log.d(TAG, "State off")
                    }
                    BluetoothAdapter.STATE_TURNING_OFF -> {
                        Log.d(TAG, "State turning off")
                    }
                    BluetoothAdapter.STATE_ON -> {
                        Log.d(TAG, "State on")
                    }
                    BluetoothAdapter.STATE_TURNING_ON -> {
                        Log.d(TAG, "State turning on")
                    }

                }
            }
            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                Log.d(TAG, "ACL connected")
            }
            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                Log.d(TAG, "ACL disconnected")
            }
        }

    }
}