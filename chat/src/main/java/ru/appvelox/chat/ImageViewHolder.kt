package ru.appvelox.chat

import android.view.View
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_image_message.view.*
import ru.appvelox.chat.model.Message

class ImageViewHolder(view: View) : MessageViewHolder(view) {

    override fun bind(
        message: Message,
        showMessageDate: Boolean,
        dateFormatter: ChatView.DateFormatter,
        messageType: MessageType
    ) {
        val transformation = when (messageType.type) {
            MessageType.INCOMING_IMAGE.type -> ImageTransformation(20f)
            else -> ImageTransformation(20f)
        }
        Picasso.get()
            .load(message.getImageUrl())
            .transform(transformation)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
//                .fit()
//                .centerInside()
            .into(itemView.image)

    }
}