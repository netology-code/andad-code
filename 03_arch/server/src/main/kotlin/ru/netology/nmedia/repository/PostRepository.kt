package ru.netology.nmedia.repository

import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import ru.netology.nmedia.entity.PostEntity
import java.util.stream.Stream

interface PostRepository : JpaRepository<PostEntity, Long> {
    fun findAllByIdLessThan(id: Long, sort: Sort): Stream<PostEntity>
    fun findAllByIdGreaterThan(id: Long, sort: Sort): Stream<PostEntity>
}