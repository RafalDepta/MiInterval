package pl.depta.rafal.miinterval.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import pl.depta.rafal.miinterval.data.db.dao.DeviceDao
import pl.depta.rafal.miinterval.data.db.dao.IntervalDao
import pl.depta.rafal.miinterval.data.db.dao.IntervalPartDao
import pl.depta.rafal.miinterval.data.db.entity.DeviceEntity
import pl.depta.rafal.miinterval.data.db.entity.IntervalEntity
import pl.depta.rafal.miinterval.data.db.entity.IntervalPartEntity

@Database(entities =
[IntervalEntity::class, DeviceEntity::class, IntervalPartEntity::class],
        version = 1,
        exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun deviceDao(): DeviceDao

    abstract fun intervalDao(): IntervalDao

    abstract fun intervalPartDao(): IntervalPartDao

}