package xyz.bboylin.dailyandroid.presentation.widget

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ItemDecoration
import android.view.View

/**
 * recyclerView分割线
 * Created by lin on 2018/2/6.
 */
class SimpleItemDecoration(margin: Int) : ItemDecoration() {
    private val paint = Paint()
    private var margin = 0

    init {
        this.margin = margin
    }

    override fun onDraw(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.onDraw(c, parent, state)
    }

    override fun onDrawOver(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.onDrawOver(c, parent, state)
        paint.setARGB(100, 20, 20, 20)
        for (i in 0..(parent?.childCount!! - 1)) {
            val child = parent.getChildAt(i)
            c?.drawLine((child.left + margin).toFloat(), child.bottom.toFloat()
                    , (child.right - margin).toFloat(), child.bottom.toFloat(), paint)
        }
    }

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
    }
}