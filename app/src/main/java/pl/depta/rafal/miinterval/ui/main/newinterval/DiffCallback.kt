package pl.depta.rafal.miinterval.ui.main.newinterval

import android.support.v7.util.DiffUtil
import pl.depta.rafal.miinterval.data.db.entity.IntervalPartEntity

class DiffCallback() : DiffUtil.ItemCallback<IntervalPartEntity>() {
    override fun areItemsTheSame(oldItem: IntervalPartEntity, newItem: IntervalPartEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: IntervalPartEntity, newItem: IntervalPartEntity): Boolean {
        return newItem.id == oldItem.id
                && newItem.pause == oldItem.pause
                && newItem.vibrate == oldItem.vibrate
                && newItem.repeat == oldItem.repeat
    }
}