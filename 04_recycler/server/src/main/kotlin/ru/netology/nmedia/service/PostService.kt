package ru.netology.nmedia.service

import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.exception.NotFoundException
import ru.netology.nmedia.exception.PermissionDeniedException
import ru.netology.nmedia.extensions.principal
import ru.netology.nmedia.repository.PostRepository
import java.time.OffsetDateTime
import java.util.stream.Collectors
import org.springframework.data.domain.PageRequest
import ru.netology.nmedia.dto.NewerCount
import ru.netology.nmedia.entity.AttachmentEmbeddable
import ru.netology.nmedia.repository.UserRepository

const val maxLoadSize = 100

@Service
@Transactional
class PostService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val commentService: CommentService,
) {
    fun getAll(): List<Post> {
        val principal = principal()
        return postRepository
            .findAll(Sort.by(Sort.Direction.DESC, "id"))
            .map { it.toDto(principal.id) }
    }

    fun getById(id: Long): Post {
        val principal = principal()
        return postRepository
            .findById(id)
            .orElseThrow(::NotFoundException)
            .toDto(principal.id)
    }

    fun getLatest(count: Int): List<Post> {
        val principal = principal()
        return postRepository
            .findAll(
                PageRequest.of(
                    0,
                    minOf(maxLoadSize, count),
                    Sort.by(Sort.Direction.DESC, "id")
                )
            )
            .content
            .map { it.toDto(principal.id) }
    }

    fun getNewer(id: Long): List<Post> {
        val principal = principal()
        return postRepository
            .findAllByIdGreaterThan(id, Sort.by(Sort.Direction.ASC, "id"))
            .map { it.toDto(principal.id) }
            .collect(Collectors.toList())
    }

    fun getNewerCount(id: Long): NewerCount {
        return postRepository
            .findAllByIdGreaterThan(id, Sort.by(Sort.Direction.ASC, "id"))
            .count()
            .let(::NewerCount)
    }

    fun getBefore(id: Long, count: Int): List<Post> {
        val principal = principal()
        return postRepository
            .findAllByIdLessThan(id, Sort.by(Sort.Direction.DESC, "id"))
            // just for simplicity: use normal limiting in production
            .limit(minOf(maxLoadSize, count).toLong())
            .map { it.toDto(principal.id) }
            .collect(Collectors.toList())
    }

    fun getAfter(id: Long, count: Int): List<Post> {
        val principal = principal()
        return postRepository
            .findAllByIdGreaterThan(id, Sort.by(Sort.Direction.ASC, "id"))
            // just for simplicity: use normal limiting in production
            .limit(minOf(maxLoadSize, count).toLong())
            .map { it.toDto(principal.id) }
            .collect(Collectors.toList())
            .reversed()
    }

    fun save(dto: Post): Post {
        val principal = principal()
        return postRepository
            .findById(dto.id)
            .orElse(
                PostEntity.fromDto(
                    dto.copy(
                        authorId = principal.id,
                        author = principal.name,
                        authorAvatar = principal.avatar,
                        likes = 0,
                        likedByMe = false,
                        published = OffsetDateTime.now().toEpochSecond()
                    )
                ).copy(author = userRepository.getById(principal.id))
            )
            .let {
                if (it.author.id != principal.id) {
                    throw PermissionDeniedException()
                }

                it.content = dto.content
                it.attachment = AttachmentEmbeddable.fromDto(dto.attachment)
                if (it.id == 0L) postRepository.save(it)
                it
            }.toDto(principal.id)
    }

    fun removeById(id: Long) {
        val principal = principal()
        postRepository.findByIdOrNull(id)
            ?.let {
                if (it.author.id != principal.id) {
                    throw PermissionDeniedException()
                }
                postRepository.delete(it)
                it
            }
            ?.also {
                commentService.removeAllByPostId(it.id)
            }
    }

    fun likeById(id: Long): Post {
        val principal = principal()
        return postRepository
            .findById(id)
            .orElseThrow(::NotFoundException)
            .apply {
                likeOwnerIds.add(principal.id)
            }
            .toDto(principal.id)
    }

    fun unlikeById(id: Long): Post {
        val principal = principal()
        return postRepository
            .findById(id)
            .orElseThrow(::NotFoundException)
            .apply {
                likeOwnerIds.remove(principal.id)
            }
            .toDto(principal.id)
    }

    fun saveInitial(dto: Post) = PostEntity.fromDto(
        dto.copy(
            likes = 0,
            likedByMe = false,
            published = if (dto.published == 0L) {
                OffsetDateTime.now().toEpochSecond()
            } else {
                dto.published
            }
        )
    ).let {
        it.content = dto.content
        postRepository.save(it)
    }.toDto(0L)
}
