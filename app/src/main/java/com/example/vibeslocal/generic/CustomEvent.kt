package com.example.vibeslocal.generic

open class CustomEvent<T> : ICustomEventClass<T>{
    private val subscribers = mutableListOf<(T) -> Unit>()

    override fun subscribe(action: (T) -> Unit) {
        subscribers.add(action)
    }

    override fun unsubscribe(action: (T) -> Unit) {
        subscribers.remove(action)
    }

    fun notify(param: T) {
        subscribers.forEach {
            it(param)
        }
    }
}