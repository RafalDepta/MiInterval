package pl.depta.rafal.miinterval.ui.main.scanner

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
class ScannerModule {

    @Provides
    @FragmentScope
    @IntoMap
    @ViewModelKey(ScannerViewModel::class)
    fun provideScannerViewModel(dataManager: DataManager, @ApplicationContext application: Application): ViewModel {
        return ScannerViewModel(dataManager, application)
    }

}