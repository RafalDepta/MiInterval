package pl.depta.rafal.miinterval.ui.main.intervals

import android.databinding.DataBindingUtil
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import pl.depta.rafal.miinterval.R
import pl.depta.rafal.miinterval.data.db.entity.IntervalEntity
import pl.depta.rafal.miinterval.databinding.ItemIntervalViewBinding

class IntervalsListAdapter : RecyclerView.Adapter<IntervalItemViewHolder>() {

    private val mDiffer = AsyncListDiffer(this, DiffCallback())
    var itemClickListener: IntervalItemViewHolder.ItemClickListener? = null

    fun setIntervals(intervalsList: List<IntervalEntity>) {
        mDiffer.submitList(intervalsList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntervalItemViewHolder {
        val itemIntervalViewBinding = DataBindingUtil.inflate<ItemIntervalViewBinding>(LayoutInflater.from(parent.context), R.layout.item_interval_view, parent, false)
        return IntervalItemViewHolder(itemIntervalViewBinding)
    }

    override fun onBindViewHolder(holder: IntervalItemViewHolder, position: Int) {
        holder.onBind(mDiffer.currentList[position], itemClickListener)
    }

    override fun getItemCount() = mDiffer.currentList.size

}