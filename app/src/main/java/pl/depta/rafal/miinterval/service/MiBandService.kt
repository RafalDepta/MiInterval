package pl.depta.rafal.miinterval.service

import android.bluetooth.BluetoothGatt
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import dagger.android.DaggerService

class MiBandService : DaggerService() {

    private var bluetoothGatt: BluetoothGatt? = null
    private val mBinder: IBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder = mBinder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    inner class LocalBinder : Binder() {
        fun getService(): MiBandService {
            return this@MiBandService
        }
    }
}