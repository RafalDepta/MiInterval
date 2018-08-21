package pl.depta.rafal.miinterval.ui.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import pl.depta.rafal.miinterval.data.DataManager


abstract class BaseViewModel(val dataManager: DataManager, application: Application) :
        AndroidViewModel(application) {

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
