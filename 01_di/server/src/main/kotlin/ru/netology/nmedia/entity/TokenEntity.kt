package ru.netology.nmedia.entity

import jakarta.persistence.*

@Entity
data class TokenEntity(
    @Id var token: String,
    @ManyToOne
    var user: UserEntity,
)
