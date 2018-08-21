package pl.depta.rafal.miinterval.ui.main.intervals

import android.support.v7.util.DiffUtil
import pl.depta.rafal.miinterval.data.db.entity.IntervalEntity

class DiffCallback() : DiffUtil.ItemCallback<IntervalEntity>() {
    override fun areItemsTheSame(oldItem: IntervalEntity, newItem: IntervalEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: IntervalEntity, newItem: IntervalEntity): Boolean {
        return newItem.id == oldItem.id
                && newItem.name == oldItem.name
    }

}