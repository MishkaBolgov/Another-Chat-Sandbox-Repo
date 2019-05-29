package ru.appvelox.myapplication

import android.graphics.Bitmap
import android.util.Log
import ru.appvelox.chat.model.Author
import ru.appvelox.chat.model.Message
import java.lang.StringBuilder
import java.util.*
import kotlin.random.Random

object MessageGenerator {

    val user1 = object : Author {
        override fun getName() = "Golfred"

        override fun getId() = 0L

        override fun getAvatar(): String {
            return "https://images-na.ssl-images-amazon.com/images/I/719SroNweJL._SL1500_.jpg"
        }
    }

    val user2 = object : Author {
        override fun getName() = "Gretta Colossal"

        override fun getId() = 1L

        override fun getAvatar(): String {
            return "https://media.pitchfork.com/photos/5929bbb45e6ef95969322c92/1:1/w_320/c91b3bfd.jpg"
        }
    }

    val user3 = object : Author {
        override fun getName() = "Gogich"

        override fun getId() = 2L

        override fun getAvatar(): String {
            return "https://upload.wikimedia.org/wikipedia/en/thumb/9/99/Process_-_Sampha_album.jpg/220px-Process_-_Sampha_album.jpg"
        }
    }

    var nextId = 0L
        get() = field++


    var previousDate = Date()
        get() {
            val currentDate = field
            field = Date(field.time - Random.nextLong(44_000_000))
            return currentDate
        }

    var nextDate = Date()
        get() {
            val currentDate = field
            field = Date(field.time + Random.nextLong(44_000_000))
            return currentDate
        }

    val messagesList = mutableListOf<Message>()

    fun generateMessage(oldMessages: Boolean): Message {
        return object : Message {

            private val mId = nextId
            private val mUser = when (Random.nextInt(3)) {
                0 -> user1
                1 -> user2
                2 -> user3
                else -> user3
            }
            private val mMessageText = MessageGenerator.generateMessageText()
            private val mDate = if (oldMessages) previousDate else nextDate
            private val repliedOn = if (Random.nextInt(4) != 0)
                null
            else {
                if (messagesList.isEmpty())
                    null
                else
                    messagesList[Random.nextInt(messagesList.size)]
            }

            override fun getId(): Long {
                return mId
            }

            override fun getText(): String {
                return mMessageText
            }

            override fun getAuthor(): Author {
                return mUser
            }

            override fun getDate(): Date {
                return mDate
            }

            override fun getRepliedMessage(): Message? {
                return repliedOn
            }
        }.also { messagesList.add(it) }
    }


    fun generateMessageText(words: Int = 20): String {
        val wordNumber = wordsArray.size
        val message = StringBuilder()
        repeat(words + 1) {
            message.append(wordsArray[Random.nextInt(wordNumber)])
            message.append(" ")

        }
        return message.toString().toLowerCase().capitalize()
    }

    val wordsArray =
        "One evening in January 1842, Virginia showed the first signs of consumption, now known as tuberculosis, while singing and playing the piano, which Poe described as breaking a blood vessel in her throat. She only partially recovered, and Poe began to drink more heavily under the stress of her illness. He left Graham's and attempted to find a new position, for a time angling for a government post. He returned to New York where he worked briefly at the Evening Mirror before becoming editor of the Broadway Journal, and later its owner. There he alienated himself from other writers by publicly accusing Henry Wadsworth Longfellow of plagiarism, though Longfellow never responded. On January 29, 1845, his poem \"The Raven\" appeared in the Evening Mirror and became a popular sensation. It made Poe a household name almost instantly, though he was paid only \$9 for its publication. It was concurrently published in The American Review: A Whig Journal under the pseudonym \"Quarles\".".split(
            ' '
        )
}