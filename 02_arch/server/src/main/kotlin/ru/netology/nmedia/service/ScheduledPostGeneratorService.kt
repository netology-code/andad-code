package ru.netology.nmedia.service

import com.github.javafaker.Faker
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.netology.nmedia.dto.Post
import javax.transaction.Transactional

@Service
@Transactional
class ScheduledPostGeneratorService(
    private val userService: UserService,
    private val postService: PostService,
) {
    private val faker = Faker()

    @Scheduled(initialDelay = 5000, fixedRate = 5000)
    fun generate() {
        val user = userService.getByLogin("student")

        postService.saveInitial(
            Post(
                id = 0,
                authorId = user.id,
                author = user.name,
                authorAvatar = user.avatar,
                content = faker.gameOfThrones().quote(),
                published = 0,
                likedByMe = false,
                likes = 0,
            )
        )
    }
}