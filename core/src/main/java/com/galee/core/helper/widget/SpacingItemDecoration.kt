package com.galee.core.helper.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView item decoration - give equal margin around grid item
 */
class SpacingItemDecoration(
    private val spanCount: Int,
    private val spacingPx: Int,
    private val includeEdge: Boolean
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        if (includeEdge) {
            outRect.left = spacingPx - column * spacingPx / spanCount
            outRect.right = (column + 1) * spacingPx / spanCount

            if (position < spanCount) { // top edge
                outRect.top = spacingPx
            }
            outRect.bottom = spacingPx // item bottom
        } else {
            outRect.left = column * spacingPx / spanCount
            outRect.right = spacingPx - (column + 1) * spacingPx / spanCount
            if (position >= spanCount) {
                outRect.top = spacingPx // item top
            }
        }
    }
}