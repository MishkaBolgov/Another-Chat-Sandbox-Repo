package ru.appvelox.chat

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.appvelox.chat.model.Message

class ChatView(context: Context, attributeSet: AttributeSet) : RecyclerView(context, attributeSet) {
    private val appearance = Appearance()
    private val adapter = MessageAdapter(appearance)

    var onItemClickListener: OnItemClickListener?
        set(value) {
            adapter.onItemClickListener = value
        }
        get() = adapter.onItemClickListener

    var onItemLongClickListener: OnItemLongClickListener?
        set(value) {
            adapter.onItemLongClickListener = value
        }
        get() = adapter.onItemLongClickListener

    fun setLoadMoreListener(listener: LoadMoreListener) {
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
        setSelectOnClick(true)

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ChatView)
        Log.d("mytag", "typedArray = ${typedArray.getColor(R.styleable.ChatView_some_custom_attr, Color.RED)}")

    }

    fun setSelectOnClick(b: Boolean){
        if (b)
            adapter.onItemClickListener = object: OnItemClickListener{
                override fun onClick(message: Message) {
                    adapter.addSelectedMessage(message)
                }
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

    interface LoadMoreCallback {
        fun onResult(messages: List<Message>)
    }


}

interface OnItemClickListener {
    fun onClick(message: Message)
}

interface OnItemLongClickListener {
    fun onClick(message: Message) {

    }
}