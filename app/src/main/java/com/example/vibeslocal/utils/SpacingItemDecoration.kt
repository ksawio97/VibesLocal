package com.example.vibeslocal.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecoration(context: Context, private val space: Spacing) : RecyclerView.ItemDecoration() {

    private val density = context.resources.displayMetrics.density

    data class Spacing(val top: Int, val right: Int, val bottom: Int, val left: Int)
    private fun toDp(space: Int) : Int {
        return (space * density).toInt()
    }
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        outRect.left = toDp(space.left)
        outRect.right = toDp(space.right)
        outRect.bottom = toDp(space.bottom)
        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = toDp(space.top)
        }
    }
}