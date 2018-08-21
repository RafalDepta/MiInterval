package pl.depta.rafal.miinterval.data.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import pl.depta.rafal.miinterval.data.db.entity.IntervalPartEntity

@Dao
interface IntervalPartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIntervalPart(intervalPartEntity: IntervalPartEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllIntervalParts(intervalPartsEntity: List<IntervalPartEntity>): List<Long>

    @Delete
    fun deleteInterval(intervalPartEntity: IntervalPartEntity): Int

    @Query("DELETE FROM IntervalPartEntity WHERE id=:id")
    fun deleteIntervalPartById(id: Long): Int

    @Query("SELECT * FROM IntervalPartEntity WHERE intervalId=:id")
    fun getIntervalPartsLiveData(id: Long): LiveData<List<IntervalPartEntity>>

    @Query("SELECT * FROM IntervalPartEntity WHERE id=:intervalPartId")
    fun getIntervalPartById(intervalPartId: Long): IntervalPartEntity
}