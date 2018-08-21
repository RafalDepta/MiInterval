package pl.depta.rafal.miinterval.ui.main.newinterval

import android.databinding.DataBindingUtil
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import pl.depta.rafal.miinterval.R
import pl.depta.rafal.miinterval.data.db.entity.IntervalPartEntity
import pl.depta.rafal.miinterval.databinding.ItemPartIntervalViewBinding

class IntervalPartListAdapter : RecyclerView.Adapter<IntervalPartViewHolder>() {

    private val mDiffer = AsyncListDiffer(this, DiffCallback())
    var itemClickListener: IntervalPartViewHolder.IntervalPartListener? = null

    fun setIntervals(intervalParts: List<IntervalPartEntity>) {
        mDiffer.submitList(intervalParts)
    }

    fun getIntervals(): List<IntervalPartEntity> {
        return mDiffer.currentList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntervalPartViewHolder {
        val itemPartIntervalViewBinding = DataBindingUtil.inflate<ItemPartIntervalViewBinding>(LayoutInflater.from(parent.context), R.layout.item_part_interval_view, parent, false)
        return IntervalPartViewHolder(itemPartIntervalViewBinding)
    }

    override fun onBindViewHolder(holder: IntervalPartViewHolder, position: Int) {
        holder.onBind(mDiffer.currentList[position], itemClickListener)
    }

    override fun getItemCount() = mDiffer.currentList.size
}