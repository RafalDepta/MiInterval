package pl.depta.rafal.miinterval.ui.main.scanner

import android.app.Application
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.depta.rafal.miinterval.SingleLiveEvent
import pl.depta.rafal.miinterval.data.DataManager
import pl.depta.rafal.miinterval.ui.base.BaseViewModel
import javax.inject.Inject

class ScannerViewModel @Inject constructor(dataManager: DataManager, application: Application) : BaseViewModel(dataManager, application) {

    val newDeviceLiveData = SingleLiveEvent<Long>()

    fun saveDevice(deviceAddress: String) {
        dataManager.saveDevice(deviceAddress)
    }


}