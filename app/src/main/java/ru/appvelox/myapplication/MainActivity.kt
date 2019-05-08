package ru.appvelox.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import ru.appvelox.chat.model.Author
import ru.appvelox.chat.model.Message
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    val user1 = object : Author {
        override fun getName() = "Golfred"

        override fun getId() = 0L
    }

    val user2 = object : Author {
        override fun getName() = "Gretta Colossal"

        override fun getId() = 1L
    }

    var nextId = 0L
        get() = field++

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chatView.setCurrentUserId(user1.getId())

        button.setOnClickListener {
            chatView.addMessage(object : Message {

                private val mId = nextId
                private val mUser = if (Random.nextBoolean()) user1 else user2

                override fun getId(): Long {
                    println("mId ${mId.hashCode()}")
                    return mId
                }

                override fun getText(): String {
                    return "Some message"
                }

                override fun getAuthor(): Author {
                    return mUser
                }
            })
        }
    }
}

