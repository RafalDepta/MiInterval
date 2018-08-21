package pl.depta.rafal.miinterval.ui.main.scanner

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.depta.rafal.miinterval.annotation.FragmentScope

@Module
abstract class ScannerFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [ScannerModule::class])
    abstract fun provideScannerFragment(): ScannerFragment
}