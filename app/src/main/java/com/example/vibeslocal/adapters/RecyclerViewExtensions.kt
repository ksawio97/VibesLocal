package com.example.vibeslocal.adapters

import android.content.res.Resources
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

fun RecyclerView.ViewHolder.setStartAnimation(animationId: Int) {
    try {
        val startAnimation = AnimationUtils.loadAnimation(itemView.context, animationId)
        itemView.startAnimation(startAnimation)
    }
    catch (ex: Resources.NotFoundException) {
        Log.e(this.javaClass.simpleName, ex.message ?: "Without message")
    }
}
/** Returns cleanup method */
fun RecyclerView.setupScrollProgress(scrollProgressBar: ProgressBar) : () -> Unit{
    val weakRecyclerView = WeakReference(this)
    val weakScrollProgressBar = WeakReference(scrollProgressBar)

    val scrollChangeListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            weakRecyclerView.get()?.apply {
                    if (dy != 0) {
                        weakScrollProgressBar.get()?.apply {
                            progress += dy
                        }
                    }
            }
        }
    }

    val onLayoutChangeListener : (View, Int, Int, Int, Int, Int, Int, Int, Int) -> Unit = { _, _, top, _, bottom, _, _, _, _ ->
        val height = bottom - top
        weakScrollProgressBar.get()?.apply {
            max = weakRecyclerView.get()?.let{
                computeVerticalScrollRange()
            }?.minus(height) ?: 1
        }
    }

    var cleanupExecuted = false

    val cleanup : () -> Unit = {
        if (!cleanupExecuted) {
            cleanupExecuted = true
            this.clearOnScrollListeners()
            this.removeOnLayoutChangeListener(onLayoutChangeListener)
        }
    }
    this.addOnScrollListener(scrollChangeListener)
    this.addOnLayoutChangeListener(onLayoutChangeListener)

    return cleanup
}