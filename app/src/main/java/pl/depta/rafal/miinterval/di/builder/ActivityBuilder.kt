package pl.depta.rafal.miinterval.di.builder

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.depta.rafal.miinterval.annotation.ActivityScope
import pl.depta.rafal.miinterval.ui.main.MainActivity
import pl.depta.rafal.miinterval.ui.main.MainModule
import pl.depta.rafal.miinterval.ui.main.intervals.IntervalsListFragmentProvider
import pl.depta.rafal.miinterval.ui.main.newinterval.NewIntervalFragmentProvider
import pl.depta.rafal.miinterval.ui.main.scanner.ScannerFragmentProvider

@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = [
        MainModule::class,
        ScannerFragmentProvider::class,
        IntervalsListFragmentProvider::class,
        NewIntervalFragmentProvider::class
    ])
    abstract fun bindMainActivity(): MainActivity

}