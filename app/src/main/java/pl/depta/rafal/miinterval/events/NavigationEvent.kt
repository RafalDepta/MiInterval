package pl.depta.rafal.miinterval.events

class NavigationEvent(val intervalId: Long) {
    enum class NavigationType {
        ADD_INTERVAL,
        EDIT_INTERVAL
    }
}

