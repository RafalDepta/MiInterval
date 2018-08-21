package pl.depta.rafal.miinterval.di.component

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import pl.depta.rafal.miinterval.App
import pl.depta.rafal.miinterval.di.builder.ActivityBuilder
import pl.depta.rafal.miinterval.di.module.AppModule
import javax.inject.Singleton

@Singleton
@Component(modules =
[
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityBuilder::class
])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}