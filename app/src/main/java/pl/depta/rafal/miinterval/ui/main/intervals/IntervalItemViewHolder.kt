package pl.depta.rafal.miinterval.ui.main.intervals

import pl.depta.rafal.miinterval.data.db.entity.IntervalEntity
import pl.depta.rafal.miinterval.databinding.ItemIntervalViewBinding
import pl.depta.rafal.miinterval.ui.base.BaseViewHolder

class IntervalItemViewHolder(val mBinding: ItemIntervalViewBinding) : BaseViewHolder(mBinding.root) {

    fun onBind(intervalEntity: IntervalEntity, itemClickListener: ItemClickListener?) {
        val intervalItemViewModel = IntervalItemViewModel(intervalEntity, itemClickListener)
        mBinding.viewModel = intervalItemViewModel
    }


    interface ItemClickListener {
        fun onClick(id: Long)
        fun onPlayClick(id: Long)
        fun onPauseClick(id: Long)
        fun onStopClick(id: Long)
        fun onDeleteClick(id: Long)
        fun onEditClick(id: Long)
    }

}