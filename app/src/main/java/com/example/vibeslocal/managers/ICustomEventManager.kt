package com.example.vibeslocal.managers

interface ICustomEventManager<EventType> {
    fun <T> subscribeToEvent(event: EventType, action: (T) -> Unit)
    fun <T> unsubscribeToEvent(event: EventType, action: (T) -> Unit)
}