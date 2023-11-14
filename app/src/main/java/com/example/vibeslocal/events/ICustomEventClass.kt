package com.example.vibeslocal.events

interface ICustomEventClass<T> {
    fun subscribe(action: (T) -> Unit)
    fun unsubscribe(action: (T) -> Unit)
}