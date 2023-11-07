package com.example.vibeslocal.adapters

import android.content.res.Resources
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.ViewHolder.setStartAnimation(animationId: Int) {
    try {
        val startAnimation = AnimationUtils.loadAnimation(itemView.context, animationId)
        itemView.startAnimation(startAnimation)
    }
    catch (ex: Resources.NotFoundException) {
        Log.e(this.javaClass.simpleName, ex.message ?: "Without message")
    }
}