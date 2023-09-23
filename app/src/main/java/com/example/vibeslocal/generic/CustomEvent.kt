package com.example.vibeslocal.generic

open class CustomEvent<T> {
    private val subscribers = mutableListOf<(T) -> Unit>()

    fun subscribe(action: (T) -> Unit) {
        subscribers.add(action)
    }

    fun unsubscribe(action: (T) -> Unit) {
        subscribers.remove(action)
    }

    fun notify(param: T) {
        subscribers.forEach {
            it(param)
        }
    }
}