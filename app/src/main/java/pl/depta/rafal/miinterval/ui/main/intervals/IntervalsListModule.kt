package pl.depta.rafal.miinterval.ui.main.intervals

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.support.v7.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import pl.depta.rafal.miinterval.annotation.ApplicationContext
import pl.depta.rafal.miinterval.annotation.FragmentScope
import pl.depta.rafal.miinterval.annotation.ViewModelKey
import pl.depta.rafal.miinterval.data.DataManager


@Module
class IntervalsListModule {

    @Provides
    @FragmentScope
    @IntoMap
    @ViewModelKey(IntervalsListViewModel::class)
    fun provideIntervalsListViewModel(dataManager: DataManager, @ApplicationContext application: Application): ViewModel {
        return IntervalsListViewModel(dataManager, application)
    }

    @Provides
    @FragmentScope
    fun provideIntervalsListAdapter(): IntervalsListAdapter {
        return IntervalsListAdapter()
    }

}