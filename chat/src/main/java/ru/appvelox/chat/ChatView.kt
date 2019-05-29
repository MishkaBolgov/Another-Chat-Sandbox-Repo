package ru.appvelox.chat

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.appvelox.chat.model.Message

class ChatView(context: Context, attributeSet: AttributeSet) : RecyclerView(context, attributeSet) {
    private val adapter = MessageAdapter()

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        adapter.onItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
            adapter.onItemLongClickListener = listener
        }

    fun setLoadMoreListener(listener: LoadMoreListener?) {
        adapter.loadMoreListener = listener
    }

    init {
        super.setAdapter(adapter)
        val layoutManager = MessageLayoutManager(context)
        super.setLayoutManager(layoutManager)
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager.findFirstVisibleItemPosition() <= 5 && !adapter.oldDataLoading) {
                    adapter.requestPreviousMessagesFromListener()
                }
            }
        })
        val swipeToReplyCallback = SwipeToReplyCallback()
        val itemTouchHelper = ItemTouchHelper(swipeToReplyCallback)
        swipeToReplyCallback.itemTouchHelper = itemTouchHelper
        itemTouchHelper.attachToRecyclerView(this)

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ChatView)
    }

    fun setSelectOnClick(b: Boolean) {
        if (b)
            adapter.onItemClickListener = object : OnItemClickListener {
                override fun onClick(message: Message) {
                    adapter.addSelectedMessage(message)
                }
            }
        else {
            adapter.onItemClickListener = null
            adapter.eraseSelectedMessages()
        }
    }

    fun addMessage(message: Message) {
        adapter.addNewMessage(message)
        layoutManager?.scrollToPosition(adapter.getLastMessageIndex())
    }

    fun setCurrentUserId(id: Long) {
        adapter.currentUserId = id
    }

    interface LoadMoreListener {
        fun requestPreviousMessages(count: Int, alreadyLoadedMessagesCount: Int, callback: LoadMoreCallback)
    }

    fun addOldMessages(messages: List<Message>) {
        adapter.addOldMessage(messages)
    }


    fun setMessageBackgroundCornerRadius(radius: Float){
        adapter.appearance.messageBackgroundCornerRadius = radius
        adapter.notifyAppearanceChanged()
    }

    fun setIncomingMessageBackgroundColor(color: Int){
        adapter.appearance.incomingMessageBackgroundColor = color
        adapter.notifyAppearanceChanged()
    }

    fun setOutgoingMessageBackgroundColor(color: Color){

    }

    fun setIncomingSelectedMessageBackgroundColor(color: Color){

    }

    fun setOutgoingSelectedMessageBackgroundColor(color: Color){

    }





    interface LoadMoreCallback {
        fun onResult(messages: List<Message>)
    }

    interface OnItemClickListener {
        fun onClick(message: Message)
    }

    interface OnItemLongClickListener {
        fun onLongClick(message: Message)
    }

}
