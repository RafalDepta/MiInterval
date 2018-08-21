package pl.depta.rafal.miinterval.ui.main.intervals


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.View
import org.greenrobot.eventbus.EventBus
import pl.depta.rafal.miinterval.BR
import pl.depta.rafal.miinterval.R
import pl.depta.rafal.miinterval.databinding.FragmentIntervalsListBinding
import pl.depta.rafal.miinterval.events.ControlDeviceEvent
import pl.depta.rafal.miinterval.events.NavigationEvent
import pl.depta.rafal.miinterval.ui.base.BaseFragment
import pl.depta.rafal.miinterval.ui.main.newinterval.NewIntervalFragment
import javax.inject.Inject


class IntervalsListFragment : BaseFragment<FragmentIntervalsListBinding, IntervalsListViewModel>(), IntervalItemViewHolder.ItemClickListener {
    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    private lateinit var mViewModel: IntervalsListViewModel

    @Inject
    lateinit var mAdapter: IntervalsListAdapter
    private var mBinding: FragmentIntervalsListBinding? = null
    override val layoutId = R.layout.fragment_intervals_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = viewDataBinding
        setUp()
        subscribeLiveData()
    }

    private fun setUp() {
        mAdapter.itemClickListener = this
        mBinding?.rvIntervalsList?.adapter = mAdapter

        arguments?.let {
            Log.d(TAG, it.getString(DEVICE_ADDRESS))
        }

        mBinding?.fab?.setOnClickListener {
            EventBus.getDefault().post(NavigationEvent(NewIntervalFragment.CREATE_INTERVAL))
        }
    }

    private fun subscribeLiveData() {
        mViewModel.intervalLiveData?.observe(this, Observer {
            it?.let {
                mAdapter.setIntervals(it)
            }
        })
    }


    override fun onClick(id: Long) {
        Log.d("Interval item", "Clicked !!!")
    }

    override fun onPlayClick(id: Long) {
        Log.d("Play", "Clicked !!!")
        EventBus.getDefault().post(ControlDeviceEvent(ControlDeviceEvent.DeviceEventType.START_INTERVAL))
    }

    override fun onPauseClick(id: Long) {
        Log.d("Pause", "Clicked !!!")
        EventBus.getDefault().post(ControlDeviceEvent(ControlDeviceEvent.DeviceEventType.PAUSE_INTERVAL))
    }

    override fun onStopClick(id: Long) {
        Log.d("Stop", "Clicked !!!")
        EventBus.getDefault().post(ControlDeviceEvent(ControlDeviceEvent.DeviceEventType.STOP_INTERVAL))
    }

    override fun onDeleteClick(id: Long) {
        Log.d("Delete", "Clicked !!!")
        mViewModel.deleteInterval(id)
    }

    override fun onEditClick(id: Long) {
        Log.d("Edit", "Clicked !!!")
        EventBus.getDefault().post(NavigationEvent(id))
    }

    override fun getViewModel(): IntervalsListViewModel {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(IntervalsListViewModel::class.java)
        return mViewModel
    }

    override fun getBindingVariable() = BR.viewModel

    companion object {

        private const val DEVICE_ADDRESS = "device_address"
        private const val TAG = "IntervalsListFragment"

        @JvmStatic
        fun newInstance(deviceAddress: String) =
                IntervalsListFragment().apply {
                    arguments = Bundle().apply {
                        putString(DEVICE_ADDRESS, deviceAddress)
                    }
                }
    }
}
