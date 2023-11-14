package com.example.vibeslocal.events

import android.content.ComponentName
import android.content.ServiceConnection

abstract class ServiceConnectionWithEventManager : ServiceConnection {
    protected val eventManager = EventManager()

    override fun onServiceDisconnected(name: ComponentName?) {
        eventManager.unsubscribeToAll()
    }

    fun unsubscribeToAllEvents() {
        eventManager.unsubscribeToAll()
    }
}