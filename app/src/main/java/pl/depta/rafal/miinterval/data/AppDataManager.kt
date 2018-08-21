package pl.depta.rafal.miinterval.data

import android.arch.lifecycle.LiveData
import io.reactivex.Flowable
import pl.depta.rafal.miinterval.data.db.DbHelper
import pl.depta.rafal.miinterval.data.db.entity.IntervalEntity
import pl.depta.rafal.miinterval.data.db.entity.IntervalPartEntity
import pl.depta.rafal.miinterval.data.db.pojo.FullInterval
import pl.depta.rafal.miinterval.data.prefs.AppPrefsHelper
import javax.inject.Inject


class AppDataManager @Inject constructor(private val dbHelper: DbHelper, private val prefsHelper: AppPrefsHelper) : DataManager {
    override fun insertIntervalsList(intervals: List<IntervalPartEntity>): Flowable<List<Long>> {
        return dbHelper.insertIntervalsList(intervals)
    }

    override fun getIntervalPartById(intervalPartId: Long): Flowable<IntervalPartEntity> {
        return dbHelper.getIntervalPartById(intervalPartId)
    }

    override fun getFullIntervalById(intervalId: Long): Flowable<FullInterval> {
        return dbHelper.getFullIntervalById(intervalId)
    }

    override fun deleteIntervalPartById(partId: Long): Flowable<Int> {
        return dbHelper.deleteIntervalPartById(partId)
    }

    override fun getFullIntervalLiveData(intervalId: Long): LiveData<FullInterval> {
        return dbHelper.getFullIntervalLiveData(intervalId)
    }

    override fun getIntervalPartsLiveData(id: Long): LiveData<List<IntervalPartEntity>> {
        return dbHelper.getIntervalPartsLiveData(id)
    }

    override fun insertIntervalPart(intervalPartEntity: IntervalPartEntity): Flowable<Long> {
        return dbHelper.insertIntervalPart(intervalPartEntity)
    }

    override fun deleteIntervalById(id: Long): Flowable<Int> {
        return dbHelper.deleteIntervalById(id)
    }

    override fun insertInterval(intervalEntity: IntervalEntity): Flowable<Long> {
        return dbHelper.insertInterval(intervalEntity)
    }

    override fun getIntervalsListLiveData(): LiveData<List<IntervalEntity>> {
        return dbHelper.getIntervalsListLiveData()
    }

    override fun getDevice(): String {
        return prefsHelper.getDevice()
    }

    override fun saveDevice(deviceAddress: String) {
        return prefsHelper.saveDevice(deviceAddress)
    }

    override fun setReady(ready: Boolean) {
        return prefsHelper.setReady(ready)
    }

}