package ru.appvelox.chat

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import ru.appvelox.chat.model.Message

class ChatView(context: Context, attributeSet: AttributeSet): RecyclerView(context, attributeSet) {
    private val adapter = MessageAdapter()

    fun setLoadMoreListener(listener: LoadMoreListener) {
        adapter.loadMoreListener = listener
    }

    init {
        super.setAdapter(adapter)
        val layoutManager = MessageLayoutManager(context)
        super.setLayoutManager(layoutManager)
        addOnScrollListener(object : OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.d("mytag", "onScrolled dx = $dx, dy = $dy, first visible = ${layoutManager.findFirstVisibleItemPosition()}, last visible = ${layoutManager.findLastVisibleItemPosition()}")
//                if(layoutManager.findFirstVisibleItemPosition() == 0)
//                    adapter.requestPreviousMessagesFromListener()
            }
        })
    }

    fun addMessage(message: Message) {
        adapter.addNewMessage(message)
        layoutManager?.scrollToPosition(adapter.getLastMessageIndex())
    }

    fun setCurrentUserId(id: Long){
        adapter.currentUserId = id
    }

    interface LoadMoreListener {
        fun requestPreviousMessages(count: Int, alreadyLoadedMessagesCount: Int, callback: (List<Message>) -> Unit)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        Log.d("mytag", "onScrollChanged l = $l, t = $t, oldl = $oldl, oldt = $oldt")
    }

    fun addOldMessages(messages: List<Message>) {
        adapter.addOldMessage(messages)
        layoutManager?.scrollToPosition(adapter.getLastMessageIndex())
    }


}
