package pl.depta.rafal.miinterval.ui.main.intervals

import android.app.Application
import android.arch.lifecycle.LiveData
import io.reactivex.schedulers.Schedulers
import pl.depta.rafal.miinterval.data.DataManager
import pl.depta.rafal.miinterval.data.db.entity.IntervalEntity
import pl.depta.rafal.miinterval.ui.base.BaseViewModel
import javax.inject.Inject

class IntervalsListViewModel @Inject constructor(dataManager: DataManager, application: Application) : BaseViewModel(dataManager, application) {
    var intervalLiveData: LiveData<List<IntervalEntity>>? = null

    init {
        intervalLiveData = dataManager.getIntervalsListLiveData()
    }

    fun deleteInterval(id: Long) {
        compositeDisposable.add(dataManager.deleteIntervalById(id)
                .subscribeOn(Schedulers.io())
                .subscribe())
    }
}