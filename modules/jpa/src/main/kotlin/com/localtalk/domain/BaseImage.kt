package com.localtalk.domain

import jakarta.persistence.*

@MappedSuperclass
abstract class BaseImage(
    @Column(name = "file_name", nullable = false)
    val fileName: String,

    @Column(name = "file_path", nullable = false)
    val filePath: String,
) : SoftDeleteBaseEntity()
