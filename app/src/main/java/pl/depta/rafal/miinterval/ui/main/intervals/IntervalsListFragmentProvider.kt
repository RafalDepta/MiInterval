package pl.depta.rafal.miinterval.ui.main.intervals

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.depta.rafal.miinterval.annotation.FragmentScope

@Module
abstract class IntervalsListFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [IntervalsListModule::class])
    abstract fun provideIntervalsListFragment(): IntervalsListFragment
}