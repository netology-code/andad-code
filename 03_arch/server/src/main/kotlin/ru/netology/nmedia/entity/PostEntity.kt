package ru.netology.nmedia.entity

import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.enumeration.AttachmentType
import jakarta.persistence.*

@Entity
data class PostEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long,
    @ManyToOne
    var author: UserEntity,
    @Column(columnDefinition = "TEXT")
    var content: String,
    var published: Long,
    @ElementCollection
    var likeOwnerIds: MutableSet<Long> = mutableSetOf(),
    @Embedded
    var attachment: AttachmentEmbeddable?,
) {
    fun toDto(myId: Long) = Post(
        id = id,
        authorId = author.id,
        author = author.name,
        authorAvatar = author.avatar,
        content = content,
        published = published,
        likedByMe = likeOwnerIds.contains(myId),
        ownedByMe = author.id == myId,
        likes = likeOwnerIds.size,
        attachment = attachment?.toDto()
    )

    companion object {
        fun fromDto(dto: Post) = PostEntity(
            id = dto.id,
            author = UserEntity(dto.authorId),
            content = dto.content,
            published = dto.published,
            likeOwnerIds = mutableSetOf(),
            attachment = AttachmentEmbeddable.fromDto(dto.attachment),
        )
    }
}

@Embeddable
data class AttachmentEmbeddable(
    var url: String,
    @Column(columnDefinition = "TEXT")
    var description: String?,
    @Enumerated(EnumType.STRING)
    var type: AttachmentType,
) {
    fun toDto() = Attachment(url, description, type)

    companion object {
        fun fromDto(dto: Attachment?) = dto?.let {
            AttachmentEmbeddable(it.url, it.description, it.type)
        }
    }
}
