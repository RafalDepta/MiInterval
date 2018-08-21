package pl.depta.rafal.miinterval.ui.main.newinterval

import android.text.Editable
import android.text.TextWatcher
import pl.depta.rafal.miinterval.data.db.entity.IntervalPartEntity
import pl.depta.rafal.miinterval.databinding.ItemPartIntervalViewBinding
import pl.depta.rafal.miinterval.ui.base.BaseViewHolder

class IntervalPartViewHolder(private val mBinding: ItemPartIntervalViewBinding) : BaseViewHolder(mBinding.root) {

    fun onBind(intervalPartEntity: IntervalPartEntity, intervalPartListener: IntervalPartListener?) {
        val intervalPartViewModel = IntervalPartViewModel(intervalPartEntity, intervalPartListener)
        mBinding.viewModel = intervalPartViewModel

        mBinding.partIntervalRepeat.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    var repeat = 0
                    if (!it.toString().isEmpty()) {
                        repeat = it.toString().toInt()
                    }

                    if (repeat != intervalPartEntity.repeat) {
                        intervalPartEntity.repeat = repeat
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        mBinding.partIntervalVibration.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    var vibration = 0
                    if (!it.toString().isEmpty()) {
                        vibration = it.toString().toInt()
                    }

                    if (vibration != intervalPartEntity.vibrate) {
                        intervalPartEntity.vibrate = vibration
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        mBinding.partIntervalPause.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    var pause = 0
                    if (!it.toString().isEmpty()) {
                        pause = it.toString().toInt()
                    }

                    if (pause != intervalPartEntity.pause) {
                        intervalPartEntity.pause = pause
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

    interface IntervalPartListener {
        fun onDeletePartClick(id: Long)
        fun onTestClick(intervalPart: IntervalPartEntity)
        fun updateIntervalPart(intervalPartEntity: IntervalPartEntity)
    }


}