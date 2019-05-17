package ru.appvelox.chat

import android.graphics.Canvas
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.avatar.view.*

class SwipeToReplyCallback:ItemTouchHelper.Callback() {

    private var blockSwipeDirection = true
    private var actionOffset = 200

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if(blockSwipeDirection){
            blockSwipeDirection = false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            setTouchListener(recyclerView, dX)
        }
        getDefaultUIUtil().onDraw(c, recyclerView, viewHolder.itemView.avatar, dX, dY, actionState, isCurrentlyActive)
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    fun setTouchListener(recyclerView: RecyclerView, dX: Float){
        recyclerView.setOnTouchListener { v, event ->
            blockSwipeDirection = event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP

            if(blockSwipeDirection)
                if(dX < -actionOffset)
                    Toast.makeText(recyclerView.context, "Action", Toast.LENGTH_SHORT).show()

            false
        }
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        Log.d("mytag", "onSelectedChanged $actionState")
    }
}