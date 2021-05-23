package ru.netology.nmedia.service

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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
import java.util.stream.Stream

const val maxLoadSize = 100

@Service
@Transactional
class PostService(
    private val repository: PostRepository,
    private val commentService: CommentService,
) {
    fun getAll(): List<Post> {
        val principal = principal()
        return repository
            .findAll(Sort.by(Sort.Direction.DESC, "id"))
            .map { it.toDto(principal.id) }
    }

    fun getById(id: Long): Post {
        val principal = principal()
        return repository
            .findById(id)
            .orElseThrow(::NotFoundException)
            .toDto(principal.id)
    }

    fun getLatest(count: Int): List<Post> {
        val principal = principal()
        return repository
            .findAll(PageRequest.of(0, minOf(maxLoadSize, count), Sort.by(Sort.Direction.DESC, "id")))
            .content
            .map { it.toDto(principal.id) }
    }

    fun getNewer(id: Long): List<Post> {
        val principal = principal()
        return repository
            .findAllByIdGreaterThan(id, Sort.by(Sort.Direction.ASC, "id"))
            .map { it.toDto(principal.id) }
            .collect(Collectors.toList())
    }

    fun getBefore(id: Long, count: Int): List<Post> {
        val principal = principal()
        return repository
            .findAllByIdLessThan(id, Sort.by(Sort.Direction.DESC, "id"))
            // just for simplicity: use normal limiting in production
            .limit(minOf(maxLoadSize, count).toLong())
            .map { it.toDto(principal.id) }
            .collect(Collectors.toList())
    }

    fun getAfter(id: Long, count: Int): List<Post> {
        val principal = principal()
        return repository
            .findAllByIdGreaterThan(id, Sort.by(Sort.Direction.ASC, "id"))
            // just for simplicity: use normal limiting in production
            .limit(minOf(maxLoadSize, count).toLong())
            .map { it.toDto(principal.id) }
            .collect(Collectors.toList())
            .reversed()
    }

    fun save(dto: Post): Post {
        val principal = principal()
        return repository
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
                )
            )
            .let {
                if (it.author.id != principal.id) {
                    throw PermissionDeniedException()
                }

                it.content = dto.content
                if (it.id == 0L) repository.save(it)
                it
            }.toDto(principal.id)
    }

    fun removeById(id: Long) {
        val principal = principal()
        repository.findByIdOrNull(id)
            ?.let {
                if (it.author.id != principal.id) {
                    throw PermissionDeniedException()
                }
                repository.delete(it)
                it
            }
            ?.also {
                commentService.removeAllByPostId(it.id)
            }
    }

    fun likeById(id: Long): Post {
        val principal = principal()
        return repository
            .findById(id)
            .orElseThrow(::NotFoundException)
            .apply {
                likeOwnerIds.add(principal.id)
            }
            .toDto(principal.id)
    }

    fun unlikeById(id: Long): Post {
        val principal = principal()
        return repository
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
            published = OffsetDateTime.now().toEpochSecond()
        )
    ).let {
        it.content = dto.content
        repository.save(it)
        it
    }.toDto(0L)
}