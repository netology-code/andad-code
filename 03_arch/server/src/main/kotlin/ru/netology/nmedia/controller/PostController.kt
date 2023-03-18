package ru.netology.nmedia.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.service.PostService

@RestController
@RequestMapping("/api/posts", "/api/slow/posts")
class PostController(private val service: PostService) {
    @GetMapping
    // TODO: uncomment for 500 status code generation
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun getAll() = service.getAll()

    @GetMapping("/{id:\\d+}")
    fun getById(@PathVariable id: Long) = service.getById(id)

    @GetMapping("/latest")
    fun getLatest(@RequestParam count: Int) = service.getLatest(count)

    @GetMapping("/{id}/newer")
    fun getNewer(@PathVariable id: Long) = service.getNewer(id)

    @GetMapping("/{id}/newer-count")
    fun getNewerCount(@PathVariable id: Long) = service.getNewerCount(id)

    @GetMapping("/{id}/before")
    fun getBefore(@PathVariable id: Long, @RequestParam count: Int) = service.getBefore(id, count)

    @GetMapping("/{id}/after")
    fun getAfter(@PathVariable id: Long, @RequestParam count: Int) = service.getAfter(id, count)

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    fun save(@RequestBody dto: Post) = service.save(dto)

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    fun removeById(@PathVariable id: Long) = service.removeById(id)

    @PostMapping("/{id}/likes")
    @PreAuthorize("hasRole('USER')")
    fun likeById(@PathVariable id: Long) = service.likeById(id)

    @DeleteMapping("/{id}/likes")
    @PreAuthorize("hasRole('USER')")
    fun unlikeById(@PathVariable id: Long) = service.unlikeById(id)

}