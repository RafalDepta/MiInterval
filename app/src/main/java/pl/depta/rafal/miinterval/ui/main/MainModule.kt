package pl.depta.rafal.miinterval.ui.main

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.support.v4.app.FragmentManager
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import pl.depta.rafal.miinterval.annotation.ActivityScope
import pl.depta.rafal.miinterval.annotation.ApplicationContext
import pl.depta.rafal.miinterval.annotation.ViewModelKey
import pl.depta.rafal.miinterval.data.DataManager
import pl.depta.rafal.miinterval.ui.main.MainViewModel

@Module
class MainModule {

    @Provides
    @ActivityScope
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun provideQuizViewModel(dataManager: DataManager, @ApplicationContext application: Application): ViewModel {
        return MainViewModel(dataManager, application)
    }

}