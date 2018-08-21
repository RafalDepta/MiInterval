package pl.depta.rafal.miinterval.di.module

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.arch.persistence.room.Room
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import pl.depta.rafal.miinterval.App
import pl.depta.rafal.miinterval.utils.AppConsts
import pl.depta.rafal.miinterval.ViewModelFactory
import pl.depta.rafal.miinterval.annotation.ApplicationContext
import pl.depta.rafal.miinterval.annotation.DatabaseInfo
import pl.depta.rafal.miinterval.annotation.DefaultPreference
import pl.depta.rafal.miinterval.data.AppDataManager
import pl.depta.rafal.miinterval.data.DataManager
import pl.depta.rafal.miinterval.data.db.AppDatabase
import pl.depta.rafal.miinterval.data.db.AppDbHelper
import pl.depta.rafal.miinterval.data.db.DbHelper
import pl.depta.rafal.miinterval.data.prefs.AppPrefsHelper
import pl.depta.rafal.miinterval.data.prefs.PrefsHelper
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Binds
    internal abstract fun bindApplication(app: App): Application

    @Binds
    @ApplicationContext
    @Singleton
    internal abstract fun provideContext(application: Application): Application

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @Singleton
    internal abstract fun provideAppDbHelper(appDbHelper: AppDbHelper): DbHelper

    @Binds
    @Singleton
    internal abstract fun providePreferenceHelper(appPrefsHelper: AppPrefsHelper): PrefsHelper

    @Binds
    @Singleton
    internal abstract fun provideDataManager(appDataManager: AppDataManager): DataManager

    @Module
    companion object {

        @JvmStatic
        @Provides
        @Singleton
        @DefaultPreference
        fun provideDefaultSharePreference(@ApplicationContext context: Application): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }

        @JvmStatic
        @Provides
        @Singleton
        @DatabaseInfo
        fun provideDbName(): String {
            return AppConsts.DB_NAME
        }

        @JvmStatic
        @Provides
        @Singleton
        fun provideAppDatabase(@DatabaseInfo dbName: String, @ApplicationContext application: Application): AppDatabase {
            return Room.databaseBuilder(application, AppDatabase::class.java, dbName).build()
        }
    }
}