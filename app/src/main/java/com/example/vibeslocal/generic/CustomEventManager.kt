package com.example.vibeslocal.generic

class CustomEventManager<EventType : Enum<EventType>>(private val events: Map<EventType, CustomEvent<*>>) {

    fun <T> subscribeToEvent(eventType: EventType, action: (T) -> Unit) {
        val customEvent = events[eventType] as? CustomEvent<T>
        customEvent?.subscribe(action)
    }

    fun <T> unsubscribeToEvent(event: EventType, action: (T) -> Unit) {
        val customEvent = events[event] as? CustomEvent<T>
        customEvent?.unsubscribe(action)
    }

    fun <T> notifyEvent(event: EventType, param: T) {
        val customEvent = events[event] as? CustomEvent<T>
        customEvent?.notify(param)
    }
}


