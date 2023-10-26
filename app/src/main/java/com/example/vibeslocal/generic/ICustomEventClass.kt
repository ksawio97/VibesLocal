package com.example.vibeslocal.generic

interface ICustomEventClass<T> {
    fun subscribe(action: (T) -> Unit)
    fun unsubscribe(action: (T) -> Unit)
}