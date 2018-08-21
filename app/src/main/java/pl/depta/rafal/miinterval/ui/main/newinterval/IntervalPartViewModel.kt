package pl.depta.rafal.miinterval.ui.main.newinterval

import android.databinding.ObservableField
import pl.depta.rafal.miinterval.data.db.entity.IntervalPartEntity

class IntervalPartViewModel(private val intervalPartEntity: IntervalPartEntity, private val intervalPartListener: IntervalPartViewHolder.IntervalPartListener?) {

    val vibration = ObservableField<String>()
    val pause = ObservableField<String>()
    val repeat = ObservableField<String>()

    init {
        vibration.set(intervalPartEntity.vibrate.toString())
        pause.set(intervalPartEntity.pause.toString())
        repeat.set(intervalPartEntity.repeat.toString())
    }


    fun onDeletePartClick() {
        intervalPartListener?.onDeletePartClick(intervalPartEntity.id)
    }

    fun onTestClick(){
        intervalPartListener?.onTestClick(intervalPartEntity)
    }
}