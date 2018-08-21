package pl.depta.rafal.miinterval.ui.main.newinterval

import android.app.Application
import android.arch.lifecycle.LiveData
import android.util.Log
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import pl.depta.rafal.miinterval.SingleLiveEvent
import pl.depta.rafal.miinterval.data.DataManager
import pl.depta.rafal.miinterval.data.db.entity.IntervalPartEntity
import pl.depta.rafal.miinterval.data.db.pojo.FullInterval
import pl.depta.rafal.miinterval.ui.base.BaseViewModel
import pl.depta.rafal.miinterval.ui.main.MainActivity
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NewIntervalViewModel @Inject constructor(dataManager: DataManager, application: Application) : BaseViewModel(dataManager, application) {
    var intervalLiveData: LiveData<FullInterval>? = null
    val startObserve = SingleLiveEvent<Any>()
    private var mIntervalId = -1L

    fun loadIntervalData(intervalId: Long) {
        mIntervalId = intervalId
        intervalLiveData = dataManager.getFullIntervalLiveData(intervalId)
        startObserve.call()
    }

    fun addNewInterval() {
        compositeDisposable.add(
                dataManager.insertIntervalPart(IntervalPartEntity(mIntervalId))
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        )
    }

    fun deleteIntervalPart(partId: Long) {
        compositeDisposable.add(
                dataManager.deleteIntervalPartById(partId)
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        )
    }

    fun testInterval(intervalPart: IntervalPartEntity) {
        compositeDisposable.add(
                Flowable.just(intervalPart)
                        .flatMap { part ->
                            var i = part.repeat
                            if (i == 0) {
                                i++
                            }
                            Flowable.just(part)
                                    .flatMap {
                                        test(i, part.vibrate, part.pause)
                                        i--
                                        Flowable.just(i)
                                    }
                                    .delay(part.vibrate + part.pause.toLong(), TimeUnit.MILLISECONDS, Schedulers.computation())
                                    .repeatUntil { i == 0 }
                        }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        )
    }

    private fun test(i: Int, vibrate: Int, pause: Int) {
        Log.d("ASDASD", "Test: $i")
        MainActivity.startVibrate(vibrate, pause)
    }

    fun saveAllIntervals(intervals: List<IntervalPartEntity>) {
        compositeDisposable.add(
                dataManager.insertIntervalsList(intervals)
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        )
    }

    fun updateIntervalPart(intervalPartEntity: IntervalPartEntity) {
        compositeDisposable.add(
                dataManager.insertIntervalPart(intervalPartEntity)
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        )
    }

    fun isContentChanged() {

    }

}
