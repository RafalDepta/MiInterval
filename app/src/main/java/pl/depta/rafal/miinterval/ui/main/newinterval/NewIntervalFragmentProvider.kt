package pl.depta.rafal.miinterval.ui.main.newinterval

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.depta.rafal.miinterval.annotation.FragmentScope

@Module
abstract class NewIntervalFragmentProvider {


    @FragmentScope
    @ContributesAndroidInjector(modules = [NewIntervalModule::class])
    abstract fun provideNewIntervalFragment(): NewIntervalFragment

}