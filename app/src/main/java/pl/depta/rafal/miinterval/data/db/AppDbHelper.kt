package pl.depta.rafal.miinterval.data.db

import android.arch.lifecycle.LiveData
import io.reactivex.Flowable
import pl.depta.rafal.miinterval.data.db.entity.DeviceEntity
import pl.depta.rafal.miinterval.data.db.entity.IntervalEntity
import pl.depta.rafal.miinterval.data.db.entity.IntervalPartEntity
import pl.depta.rafal.miinterval.data.db.pojo.FullInterval
import javax.inject.Inject

class AppDbHelper @Inject constructor(private val appDatabase: AppDatabase) : DbHelper {
    override fun insertIntervalsList(intervals: List<IntervalPartEntity>): Flowable<List<Long>> {
        return Flowable.fromCallable{appDatabase.intervalPartDao().insertAllIntervalParts(intervals)}
    }

    override fun getIntervalPartById(intervalPartId: Long): Flowable<IntervalPartEntity> {
        return Flowable.fromCallable { appDatabase.intervalPartDao().getIntervalPartById(intervalPartId) }
    }

    override fun getFullIntervalById(intervalId: Long): Flowable<FullInterval> {
        return Flowable.fromCallable { appDatabase.intervalDao().getFullIntervalById(intervalId) }
    }

    override fun deleteIntervalPartById(partId: Long): Flowable<Int> {
        return Flowable.fromCallable { appDatabase.intervalPartDao().deleteIntervalPartById(partId) }
    }

    override fun getFullIntervalLiveData(intervalId: Long): LiveData<FullInterval> {
        return appDatabase.intervalDao().getFullIntervalLiveData(intervalId)
    }

    override fun insertIntervalPart(intervalPartEntity: IntervalPartEntity): Flowable<Long> {
        return Flowable.fromCallable { appDatabase.intervalPartDao().insertIntervalPart(intervalPartEntity) }
    }

    override fun deleteIntervalById(id: Long): Flowable<Int> {
        return Flowable.fromCallable { appDatabase.intervalDao().deleteIntervalById(id) }
    }

    override fun insertInterval(intervalEntity: IntervalEntity): Flowable<Long> {
        return Flowable.fromCallable { appDatabase.intervalDao().insertInterval(intervalEntity) }
    }

    override fun getIntervalsListLiveData(): LiveData<List<IntervalEntity>> {
        return appDatabase.intervalDao().getIntervalsListLiveData()
    }

    override fun getIntervalPartsLiveData(id: Long): LiveData<List<IntervalPartEntity>> {
        return appDatabase.intervalPartDao().getIntervalPartsLiveData(id)
    }

}