package pl.depta.rafal.miinterval.ui.main

import android.app.Application
import android.util.Log
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.depta.rafal.miinterval.SingleLiveEvent
import pl.depta.rafal.miinterval.data.DataManager
import pl.depta.rafal.miinterval.data.db.entity.IntervalEntity
import pl.depta.rafal.miinterval.data.db.entity.IntervalPartEntity
import pl.depta.rafal.miinterval.ui.base.BaseViewModel
import javax.inject.Inject

class MainViewModel
@Inject constructor(dataManager: DataManager, application: Application) : BaseViewModel(dataManager, application) {

    val intervalCreated = SingleLiveEvent<Long>()

    fun getLastDevice(): String {
        return dataManager.getDevice()
    }

    fun setDeviceReady(isReady: Boolean) {
        dataManager.setReady(isReady)
    }

    fun createNewInterval() {

        compositeDisposable.add(
                dataManager.insertInterval(IntervalEntity(0, "My interval"))
                        .map {
                            dataManager.insertIntervalPart(IntervalPartEntity(intervalId = it))
                                    .subscribe()
                        it
                        }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            intervalCreated.value = it
                        }
        )
    }


}