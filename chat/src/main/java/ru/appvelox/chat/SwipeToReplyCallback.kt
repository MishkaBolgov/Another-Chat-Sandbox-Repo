package ru.appvelox.chat

import android.content.Context
import android.graphics.Canvas
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_message.view.*
import kotlinx.android.synthetic.main.left_swipe_action_icon.view.*
import kotlin.math.abs

class SwipeToReplyCallback : ItemTouchHelper.Callback() {

    var itemTouchHelper: ItemTouchHelper? = null
    private var blockSwipeDirection = true
    private var actionOffset = 160
    private var resetOffset = 80
    private var actionIconAppearOffset = 80

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT)
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
        setTouchListener(recyclerView)

        if(dX < -actionOffset && !isItemDraggedToAction) {
            (recyclerView.context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(20)
            isItemDraggedToAction = true
//                itemTouchHelper?.attachToRecyclerView(null)
        }

        if (dX < -actionOffset) return

        if(dX > -resetOffset)
            isItemDraggedToAction = false

        if(dX == 0f)
            viewHolder.itemView.imageViewLeftSwipeActionIcon.imageAlpha = 0

        if(dX < -actionIconAppearOffset) {
            val progress = (dX + actionIconAppearOffset) / (actionOffset - actionIconAppearOffset)
            val alpha = abs((255 * progress).toInt())
            viewHolder.itemView.imageViewLeftSwipeActionIcon.imageAlpha = alpha
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

            if(isItemDraggedToAction)
                return

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

    fun setTouchListener(recyclerView: RecyclerView) {
        recyclerView.setOnTouchListener { v, event ->
           blockSwipeDirection = event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
            if(blockSwipeDirection){
                onSwipeEnd()
            }
            false
        }
    }

    fun onSwipeEnd(){
        isItemDraggedToAction = false

    }

    var isItemDraggedToAction = false

}