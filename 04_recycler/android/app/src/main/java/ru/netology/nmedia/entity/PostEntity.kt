package ru.netology.nmedia.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: Long,
    val likedByMe: Boolean,
    val likes: Int = 0,
    @Embedded
    var attachment: AttachmentEmbeddable?,
) {
    fun toDto() = Post(
        id,
        authorId,
        author,
        authorAvatar,
        content,
        LocalDateTime.ofInstant(Instant.ofEpochSecond(published), ZoneId.systemDefault()),
        likedByMe,
        likes,
        attachment?.toDto(),
    )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id,
                dto.authorId,
                dto.author,
                dto.authorAvatar,
                dto.content,
                dto.published.atZone(ZoneId.systemDefault()).toEpochSecond(),
                dto.likedByMe,
                dto.likes,
                AttachmentEmbeddable.fromDto(dto.attachment)
            )

    }
}

fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)
