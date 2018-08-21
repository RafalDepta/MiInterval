package pl.depta.rafal.miinterval.ui.main.intervals

import android.databinding.ObservableField
import pl.depta.rafal.miinterval.data.db.entity.IntervalEntity

class IntervalItemViewModel(private val intervalEntity: IntervalEntity, private val itemClickListener: IntervalItemViewHolder.ItemClickListener?) {

    val name = ObservableField<String>()

    init {
        this.name.set(intervalEntity.name)
    }

    fun onClick() {
        itemClickListener?.onClick(intervalEntity.id)
    }

    fun onPlayClick() {
        itemClickListener?.onPlayClick(intervalEntity.id)
    }

    fun onPauseClick() {
        itemClickListener?.onPauseClick(intervalEntity.id)
    }

    fun onStopClick() {
        itemClickListener?.onStopClick(intervalEntity.id)
    }

    fun onDeleteClick() {
        itemClickListener?.onDeleteClick(intervalEntity.id)
    }

    fun onEditClick(){
        itemClickListener?.onEditClick(intervalEntity.id)
    }

}