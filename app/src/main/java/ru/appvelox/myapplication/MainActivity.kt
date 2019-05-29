package ru.appvelox.myapplication

import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import ru.appvelox.chat.*
import ru.appvelox.chat.model.Author
import ru.appvelox.chat.model.Message
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    val listener = object : ChatView.LoadMoreListener {
        override fun requestPreviousMessages(
            count: Int,
            alreadyLoadedMessagesCount: Int,
            callback: ChatView.LoadMoreCallback
        ) {
            val messages = mutableListOf<Message>()
            repeat(count) {
                messages.add(MessageGenerator.generateMessage(true))
            }
            AsyncTask.execute {
                Thread.sleep(1000)
                callback.onResult(messages)
            }
        }
    }

    var counter = 0
    get() {
        return field++
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chatView.setLoadMoreListener(listener)

        chatView.setCurrentUserId(MessageGenerator.user1.getId())

        chatView.addMessage(MessageGenerator.generateMessage(false))
        chatView.addMessage(MessageGenerator.generateMessage(false))
        chatView.addMessage(MessageGenerator.generateMessage(false))

        button1.setOnClickListener {
            chatView.addMessage(MessageGenerator.generateMessage(false))
        }

        button2.setOnClickListener {
            when(counter){
                0 -> {
                    chatView.setSelectOnClick(true)
                }

                1 -> {
                    chatView.setSelectOnClick(false)
                }

                2 -> {
                    chatView.setOnItemClickListener(object: ChatView.OnItemClickListener{
                        override fun onClick(message: Message) {
                            Toast.makeText(this@MainActivity, "Click on message #${message.getId()}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }

                3 -> {
                    chatView.setOnItemLongClickListener(object: ChatView.OnItemLongClickListener{
                        override fun onLongClick(message: Message) {
                            Toast.makeText(this@MainActivity, "Long click on message #${message.getId()}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }

                4 -> {
                    chatView.setMessageBackgroundCornerRadius(50f)
                    chatView.setIncomingMessageBackgroundColor(Color.BLUE)
                }

                5 -> {
                    chatView.setMessageBackgroundCornerRadius(0f)
                    chatView.setIncomingMessageBackgroundColor(Color.BLACK)
                }

                6 -> {
                    chatView.setMessageBackgroundCornerRadius(100f)
                    chatView.setIncomingMessageBackgroundColor(Color.CYAN)
                }
            }
        }

    }
}