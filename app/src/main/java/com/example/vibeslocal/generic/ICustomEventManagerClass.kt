package com.example.vibeslocal.generic

interface ICustomEventManagerClass<EventType> {
    fun <T> subscribeToEvent(event: EventType, action: (T) -> Unit)
    fun <T> unsubscribeToEvent(event: EventType, action: (T) -> Unit)
}