package com.localtalk.domain

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import java.time.ZonedDateTime

@MappedSuperclass
abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column(name = "created_at", nullable = false, updatable = false)
    lateinit var createdAt: ZonedDateTime
        protected set

    @Column(name = "updated_at", nullable = false)
    lateinit var updatedAt: ZonedDateTime
        protected set

    @Column(name = "deleted_at")
    var deletedAt: ZonedDateTime? = null
        protected set

    open fun validate() = Unit

    @PrePersist
    private fun prePersist() {
        validate()

        val now = ZonedDateTime.now()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    private fun preUpdate() {
        validate()
        updatedAt = ZonedDateTime.now()
    }

    fun delete() {
        deletedAt = deletedAt ?: ZonedDateTime.now()
    }

    fun restore() {
        deletedAt = null
    }
}
