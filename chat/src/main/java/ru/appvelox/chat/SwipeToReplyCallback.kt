package ru.appvelox.chat

import android.graphics.Canvas
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.avatar.view.*
import kotlinx.android.synthetic.main.item_incoming_message.view.*

class SwipeToReplyCallback : ItemTouchHelper.Callback() {

    var itemTouchHelper: ItemTouchHelper? = null
    private var blockSwipeDirection = true
    private var actionOffset = 500

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
//        if (isItemDraggedToAction)
//            return 0

        if (blockSwipeDirection) {
            blockSwipeDirection = false
            isItemDraggedToAction = false
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
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            setTouchListener(recyclerView, dX)
        }
        getDefaultUIUtil().onDraw(
            c,
            recyclerView,
            viewHolder.itemView.messageContainer,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        viewHolder?.itemView?.messageContainer?.let {
            getDefaultUIUtil().onDrawOver(
                c,
                recyclerView,
                it,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
    }


    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        getDefaultUIUtil().clearView(viewHolder.itemView.messageContainer)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        viewHolder?.let {
            getDefaultUIUtil().onSelected(it.itemView.messageContainer)
        }

    }

    fun setTouchListener(recyclerView: RecyclerView, dX: Float) {
        recyclerView.setOnTouchListener { v, event ->
            blockSwipeDirection = event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP

            if(dX < -actionOffset) {
                Toast.makeText(recyclerView.context, "Action", Toast.LENGTH_SHORT).show()
                isItemDraggedToAction = true
//                itemTouchHelper?.attachToRecyclerView(null)
            }


            if (blockSwipeDirection)
                if (dX < -actionOffset){}

            false
        }
    }

    var isItemDraggedToAction = false
}