package com.example.vibeslocal.events

class EventManager {
    private val unsubscribes = mutableListOf<() -> Unit>()
    fun <T> subscribeTo(event: ICustomEventClass<T>, action: (T) -> Unit) {
        event.subscribe(action)
        unsubscribes.add {
            event.unsubscribe(action)
        }
    }

    fun unsubscribeToAll() {
        unsubscribes.forEach { unsubscribe ->
            unsubscribe()
        }
    }
}