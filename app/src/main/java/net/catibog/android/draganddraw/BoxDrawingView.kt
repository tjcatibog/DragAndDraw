package net.catibog.android.draganddraw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

private const val TAG = "BoxDrawingView"

class BoxDrawingView(
    context: Context,
    attrs: AttributeSet? = null
): View(context, attrs) {

    private var currentBox: Box? = null
    private val boxes = mutableListOf<Box>()
    private val boxPaint = Paint().apply {
        color = 0x22ff0000
    }
    private val backgroundPaint = Paint().apply {
        color = 0xfff8efe0.toInt()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPaint(backgroundPaint)
        boxes.forEach { box ->
            canvas.drawRect(box.left, box.top, box.right, box.bottom, boxPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val current = PointF(event.x, event.y)
        val action: String = when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentBox = Box(current).also {
                    boxes.add(it)
                }
                "ACTION_DOWN"
            }
            MotionEvent.ACTION_MOVE -> {
                updateCurrentBox(current)
                "ACTION_MOVE"
            }
            MotionEvent.ACTION_UP -> {
                updateCurrentBox(current)
                currentBox = null
                "ACTION_UP"
            }
            MotionEvent.ACTION_CANCEL -> {
                currentBox = null
                "ACTION_CANCEL"
            }
            else -> MotionEvent.actionToString(event.action)
        }
        Log.i(TAG, "$action at x=${current.x}, y=${current.y}")
        return true
    }

    private fun updateCurrentBox(current: PointF) {
        currentBox?.let {
            it.end = current
            invalidate()
        }
    }
}