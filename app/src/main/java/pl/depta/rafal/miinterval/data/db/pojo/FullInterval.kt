package pl.depta.rafal.miinterval.data.db.pojo

import android.arch.persistence.room.Relation
import pl.depta.rafal.miinterval.data.db.entity.IntervalEntity
import pl.depta.rafal.miinterval.data.db.entity.IntervalPartEntity

data class FullInterval(
        @Relation(parentColumn = "id", entityColumn = "intervalId", entity = IntervalPartEntity::class)
        var intervalParts: List<IntervalPartEntity> = listOf()
) : IntervalEntity()