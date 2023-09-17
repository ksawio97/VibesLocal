package com.example.vibeslocal.generic

class CustomEvent<T> {
    private val subscribers = mutableListOf<(T) -> Unit>()

    fun subscribe(action: (T) -> Unit) {
        subscribers.add(action)
    }

    fun notify(param: T) {
        subscribers.forEach {
            it(param)
        }
    }
}