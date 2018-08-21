package pl.depta.rafal.miinterval.data.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class DeviceEntity(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        var name: String = "",
        var address: String = ""
) {
    constructor(name: String = "", address: String = "") : this(0, name, address)
}