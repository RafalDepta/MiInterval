package pl.depta.rafal.miinterval.data.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
open class IntervalEntity(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var name: String = ""
) {
    constructor() : this(0,"")
}