package pl.depta.rafal.miinterval.data.db.dao

import android.arch.persistence.room.*
import pl.depta.rafal.miinterval.data.db.entity.DeviceEntity

@Dao
interface DeviceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDevice(device: DeviceEntity): Long

    @Query("SELECT * FROM DeviceEntity WHERE name=:deviceName")
    fun getDeviceByName(deviceName: String): Int

    @Delete
    fun deleteDevice(device: DeviceEntity): Int
}