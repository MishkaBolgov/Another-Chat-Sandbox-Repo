package ru.appvelox.chat

import android.content.Context
import android.graphics.Canvas
import android.os.Vibrator
import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_message.view.*
import kotlinx.android.synthetic.main.left_swipe_action_icon.view.*
import kotlin.math.abs

class SwipeToReplyCallback : ItemTouchHelper.Callback() {

    var itemTouchHelper: ItemTouchHelper? = null
    var triggerOffset = 300f

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
//        if(offsetReached)
//            return ACTION_STATE_IDLE

        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        val threshold = super.getSwipeThreshold(viewHolder)
        return threshold
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        val velocity = defaultValue
        return super.getSwipeVelocityThreshold(velocity)
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        val velocity = defaultValue
        return  super.getSwipeEscapeVelocity(velocity)
    }

    var offsetReached = false

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

//        if(dX < -triggerOffset)
//            offsetReached = true

        getDefaultUIUtil().onDraw(
            c,
            recyclerView,
            viewHolder.itemView.contentContainer,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        getDefaultUIUtil().clearView(viewHolder.itemView.contentContainer)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        viewHolder?.let {
            getDefaultUIUtil().onSelected(it.itemView.contentContainer)
        }

    }

    enum class States{
        REST, SWIPE, TRIGGERED
    }
}