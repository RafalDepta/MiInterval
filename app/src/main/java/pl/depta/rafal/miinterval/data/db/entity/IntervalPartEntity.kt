package pl.depta.rafal.miinterval.data.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(foreignKeys = [
    ForeignKey(
            entity = IntervalEntity::class,
            parentColumns = ["id"],
            childColumns = ["intervalId"],
            onDelete = CASCADE
    )
],
        indices = [
            Index("intervalId")
        ]
)
class IntervalPartEntity(
        @PrimaryKey(autoGenerate = true)
        var id: Long,
        var intervalId: Long,
        var vibrate: Int,
        var pause: Int,
        var repeat: Int
) {
    constructor() : this(0, 0, 500, 500, 2)
    constructor(intervalId: Long) : this(0, intervalId, 500, 500, 2)
}