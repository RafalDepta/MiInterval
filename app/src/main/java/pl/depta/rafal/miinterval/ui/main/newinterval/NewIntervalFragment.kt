package pl.depta.rafal.miinterval.ui.main.newinterval


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.View
import pl.depta.rafal.miinterval.BR
import pl.depta.rafal.miinterval.R
import pl.depta.rafal.miinterval.data.db.entity.IntervalPartEntity
import pl.depta.rafal.miinterval.databinding.FragmentNewIntervalBinding
import pl.depta.rafal.miinterval.ui.base.BaseFragment
import javax.inject.Inject


class NewIntervalFragment : BaseFragment<FragmentNewIntervalBinding, NewIntervalViewModel>(), IntervalPartViewHolder.IntervalPartListener {

    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    private lateinit var mViewModel: NewIntervalViewModel
    @Inject
    lateinit var mAdapter: IntervalPartListAdapter
    private var mBinding: FragmentNewIntervalBinding? = null
    override val layoutId = R.layout.fragment_new_interval

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = viewDataBinding
        setUp()
        subscribeLiveData()
    }

    private fun subscribeLiveData() {
        mViewModel.startObserve.observe(this, Observer { _ ->
            mViewModel.intervalLiveData?.observe(this, Observer { it ->
                it?.let {
                    mAdapter.setIntervals(it.intervalParts)
                }
            })
        })
    }

    override fun onDetach() {
        mViewModel.isContentChanged()
        mViewModel.saveAllIntervals(mAdapter.getIntervals())
        super.onDetach()
    }

    private fun setUp() {
        mAdapter.itemClickListener = this
        mBinding?.rvPartInterval?.adapter = mAdapter
        arguments?.let {
            val intervalId = it.getLong(INTERVAL_ID)
            mViewModel.loadIntervalData(intervalId)
        }
    }

    override fun onDeletePartClick(id: Long) {
        Log.d("DeletePart", "Clicked !!!")
        mViewModel.deleteIntervalPart(id)
    }

    override fun onTestClick(intervalPart: IntervalPartEntity) {
       mViewModel.testInterval(intervalPart)
    }

    override fun updateIntervalPart(intervalPartEntity: IntervalPartEntity) {
       mViewModel.updateIntervalPart(intervalPartEntity)
    }



    override fun getViewModel(): NewIntervalViewModel {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(NewIntervalViewModel::class.java)
        return mViewModel
    }


    override fun getBindingVariable() = BR.viewModel


    companion object {

        private const val INTERVAL_ID = "interval_id"
        const val CREATE_INTERVAL = -1L

        @JvmStatic
        fun newInstance(intervalId: Long) =
                NewIntervalFragment().apply {
                    arguments = Bundle().apply {
                        putLong(INTERVAL_ID, intervalId)
                    }
                }
    }
}
