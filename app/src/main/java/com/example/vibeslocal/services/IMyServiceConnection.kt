package com.example.vibeslocal.services

//TODO use this to connect with services
interface IMyServiceConnection<T> {
    fun onServiceConnected(service: T)
    fun onServiceDisconnected()
}