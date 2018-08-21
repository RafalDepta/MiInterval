package pl.depta.rafal.miinterval.data.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import pl.depta.rafal.miinterval.data.db.entity.DeviceEntity
import pl.depta.rafal.miinterval.data.db.entity.IntervalEntity
import pl.depta.rafal.miinterval.data.db.pojo.FullInterval

@Dao
interface IntervalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInterval(intervalEntity: IntervalEntity): Long

    @Delete
    fun deleteInterval(intervalEntity: IntervalEntity): Int

    @Query("SELECT * FROM IntervalEntity")
    fun getIntervalsListLiveData(): LiveData<List<IntervalEntity>>

    @Query("DELETE FROM IntervalEntity WHERE id=:id")
    fun deleteIntervalById(id: Long): Int

    @Query("SELECT * FROM IntervalEntity WHERE id=:intervalId")
    fun getFullIntervalLiveData(intervalId: Long): LiveData<FullInterval>

    @Query("SELECT * FROM IntervalEntity WHERE id=:intervalId")
    fun getFullIntervalById(intervalId: Long): FullInterval
}