package com.localtalk.domain

import com.localtalk.internal.InternalBaseEntityApi
import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import java.time.ZonedDateTime

@OptIn(InternalBaseEntityApi::class)
@MappedSuperclass
abstract class SoftDeleteBaseEntity : BaseEntity() {
    @Column(name = "deleted_at")
    var deletedAt: ZonedDateTime? = null
        protected set

    fun delete() {
        deletedAt = deletedAt ?: ZonedDateTime.now()
    }

    fun restore() {
        deletedAt = null
    }
}
