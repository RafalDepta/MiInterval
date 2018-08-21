package pl.depta.rafal.miinterval

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import pl.depta.rafal.miinterval.di.component.DaggerAppComponent

class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}