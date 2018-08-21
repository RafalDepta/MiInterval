package pl.depta.rafal.miinterval.data.db

import android.arch.lifecycle.LiveData
import io.reactivex.Flowable
import pl.depta.rafal.miinterval.data.db.entity.IntervalEntity
import pl.depta.rafal.miinterval.data.db.entity.IntervalPartEntity
import pl.depta.rafal.miinterval.data.db.pojo.FullInterval

interface DbHelper {
    fun getIntervalsListLiveData(): LiveData<List<IntervalEntity>>
    fun insertInterval(intervalEntity: IntervalEntity) :Flowable<Long>
    fun deleteIntervalById(id: Long): Flowable<Int>
    fun getIntervalPartsLiveData(id: Long): LiveData<List<IntervalPartEntity>>
    fun insertIntervalPart(intervalPartEntity: IntervalPartEntity): Flowable<Long>
    fun getFullIntervalLiveData(intervalId: Long): LiveData<FullInterval>
    fun deleteIntervalPartById(partId: Long): Flowable<Int>
    fun getFullIntervalById(intervalId: Long): Flowable<FullInterval>
    fun getIntervalPartById(intervalPartId: Long): Flowable<IntervalPartEntity>
    fun insertIntervalsList(intervals: List<IntervalPartEntity>): Flowable<List<Long>>
}