package ru.netology.nmedia.dto

import ru.netology.nmedia.enumeration.AttachmentType
import java.time.LocalDateTime

sealed class FeedItem {
    abstract val id: Long
}

data class Ad(
    override val id: Long,
    val url: String,
    val image: String,
) : FeedItem()

data class Post(
    override val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: LocalDateTime,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val attachment: Attachment? = null,
    val ownedByMe: Boolean = false,
) : FeedItem()

data class DateSeparator(
    val type: Type,
) : FeedItem() {
    override val id: Long = type.ordinal.toLong()

    enum class Type {
        TODAY,
        YESTERDAY,
        WEEK_AGO,
    }
}

data class Attachment(
    val url: String,
    val type: AttachmentType,
)
