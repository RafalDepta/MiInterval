package pl.depta.rafal.miinterval.ui.main.newinterval

import android.app.Application
import android.arch.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import pl.depta.rafal.miinterval.annotation.ApplicationContext
import pl.depta.rafal.miinterval.annotation.FragmentScope
import pl.depta.rafal.miinterval.annotation.ViewModelKey
import pl.depta.rafal.miinterval.data.DataManager

@Module
class NewIntervalModule {

    @Provides
    @FragmentScope
    @IntoMap
    @ViewModelKey(NewIntervalViewModel::class)
    fun provideNewIntervalViewModel(dataManager: DataManager, @ApplicationContext application: Application): ViewModel {
        return NewIntervalViewModel(dataManager, application)
    }

    @Provides
    @FragmentScope
    fun provideIntervalPartsListAdapter(): IntervalPartListAdapter {
        return IntervalPartListAdapter()
    }


}