package pl.depta.rafal.miinterval.data

import io.reactivex.Flowable
import pl.depta.rafal.miinterval.data.db.DbHelper
import pl.depta.rafal.miinterval.data.db.entity.DeviceEntity
import pl.depta.rafal.miinterval.data.prefs.PrefsHelper

interface DataManager : DbHelper, PrefsHelper {
}