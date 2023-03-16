package ru.netology.nmedia

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.util.ResourceUtils
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Comment
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.enumeration.AttachmentType
import ru.netology.nmedia.service.CommentService
import ru.netology.nmedia.service.PostService
import ru.netology.nmedia.service.UserService
import java.time.Duration
import java.time.OffsetDateTime

@EnableScheduling
@SpringBootApplication
class NMediaApplication {
    @Bean
    fun runner(
        userService: UserService,
        postService: PostService,
        commentService: CommentService,
        @Value("\${app.media-location}") mediaLocation: String,
    ) = CommandLineRunner {
        ResourceUtils.getFile("classpath:static").copyRecursively(
            ResourceUtils.getFile(mediaLocation),
            true,
        )

        val netology = userService.create(
            login = "netology",
            pass = "secret",
            name = "Netology",
            avatar = "netology.jpg"
        )
        val sber =
            userService.create(login = "sber", pass = "secret", name = "Сбер", avatar = "sber.jpg")
        val tcs = userService.create(
            login = "tcs",
            pass = "secret",
            name = "Тинькофф",
            avatar = "tcs.jpg"
        )
        val got = userService.create(
            login = "got",
            pass = "secret",
            name = "Game of Thrones",
            avatar = "got.jpg"
        )
        val student = userService.create(
            login = "student",
            pass = "secret",
            name = "Студент",
            avatar = "netology.jpg"
        )

        userService.saveInitialToken(student.id, "x-token")

        val currentTime = OffsetDateTime.now()
        val yesterday = currentTime.minus(Duration.ofDays(1))
        val weekAgo = currentTime.minus(Duration.ofDays(7))
        val twoWeeksAgo = currentTime.minus(Duration.ofDays(14))

        val firstPost = postService.saveInitial(
            Post(
                id = 0,
                authorId = netology.id,
                author = netology.name,
                authorAvatar = netology.avatar,
                content = "15 дней назад",
                published = twoWeeksAgo.minus(Duration.ofDays(1)).toEpochSecond(),
                likedByMe = false,
                likes = 0,
            )
        )
        val secondPost = postService.saveInitial(
            Post(
                id = 0,
                authorId = sber.id,
                author = sber.name,
                authorAvatar = sber.avatar,
                content = "Две недели назад",
                published = twoWeeksAgo.toEpochSecond(),
                likedByMe = false,
                likes = 0,
            )
        )
        val thirdPost = postService.saveInitial(
            Post(
                id = 0,
                authorId = tcs.id,
                author = tcs.name,
                authorAvatar = tcs.avatar,
                content = "13 дней назад",
                published = twoWeeksAgo.plus(Duration.ofDays(1)).toEpochSecond(),
                likedByMe = false,
                likes = 0,
            )
        )
        val fourthPost = postService.saveInitial(
            Post(
                id = 0,
                authorId = netology.id,
                author = netology.name,
                authorAvatar = netology.avatar,
                content = "Десять дней назад",
                published = weekAgo.minus(Duration.ofDays(3)).toEpochSecond(),
                likedByMe = false,
                likes = 0,
                attachment = Attachment(
                    url = "podcast.jpg",
                    description = "Как запустить свой подкаст: подборка статей",
                    type = AttachmentType.IMAGE,
                ),
            )
        )
        val fifthPost = postService.saveInitial(
            Post(
                id = 0,
                authorId = sber.id,
                author = sber.name,
                authorAvatar = sber.avatar,
                content = "Девять дней назад",
                published = weekAgo.minus(Duration.ofDays(2)).toEpochSecond(),
                likedByMe = false,
                likes = 0,
                attachment = Attachment(
                    url = "sbercard.jpg",
                    description = "Предлагают новую карту? Проверьте, не мошенничество ли это!",
                    type = AttachmentType.IMAGE,
                ),
            )
        )
        val sixthPost = postService.saveInitial(
            Post(
                id = 0,
                authorId = student.id,
                author = student.name,
                authorAvatar = student.avatar,
                content = "Восемь дней назад",
                published = weekAgo.minus(Duration.ofDays(1)).toEpochSecond(),
                likedByMe = false,
                likes = 0,
            )
        )
        val seventhPost = postService.saveInitial(
            Post(
                id = 0,
                authorId = student.id,
                author = student.name,
                authorAvatar = student.avatar,
                content = "Пост был написан неделю назад",
                published = weekAgo.toEpochSecond(),
                likedByMe = false,
                likes = 0,
            )
        )
        val eighthPost = postService.saveInitial(
            Post(
                id = 0,
                authorId = student.id,
                author = student.name,
                authorAvatar = student.avatar,
                content = "Пост был написан 6 дней назад",
                published = weekAgo.plus(Duration.ofDays(1)).toEpochSecond(),
                likedByMe = false,
                likes = 0,
            )
        )
        val ninthPost = postService.saveInitial(
            Post(
                id = 0,
                authorId = student.id,
                author = student.name,
                authorAvatar = student.avatar,
                content = "Пост был написан 2 дня назад",
                published = yesterday.minus(Duration.ofDays(1)).toEpochSecond(),
                likedByMe = false,
                likes = 0,
            )
        )
        val tenthPost = postService.saveInitial(
            Post(
                id = 0,
                authorId = student.id,
                author = student.name,
                authorAvatar = student.avatar,
                content = "Пост был написан вчера",
                published = yesterday.toEpochSecond(),
                likedByMe = false,
                likes = 0,
            )
        )
        with(commentService) {
            saveInitial(
                Comment(
                    id = 0,
                    postId = firstPost.id,
                    authorId = netology.id,
                    author = netology.name,
                    authorAvatar = netology.avatar,
                    content = "Отлично!",
                    published = 0,
                    likedByMe = false,
                    likes = 0,
                )
            )
            saveInitial(
                Comment(
                    id = 0,
                    postId = firstPost.id,
                    authorId = sber.id,
                    author = sber.name,
                    authorAvatar = sber.avatar,
                    content = "Мы тоже обновились!",
                    published = 0,
                    likedByMe = false,
                    likes = 0,
                )
            )
            saveInitial(
                Comment(
                    id = 0,
                    postId = secondPost.id,
                    authorId = netology.id,
                    author = netology.name,
                    authorAvatar = netology.avatar,
                    content = "Новый логотип прекрасен!",
                    published = 0,
                    likedByMe = false,
                    likes = 0,
                )
            )
        }
    }

}

fun main(args: Array<String>) {
    runApplication<NMediaApplication>(*args)
}
