package com.localtalk.api.file.domain

import org.springframework.data.jpa.repository.JpaRepository

interface FileRepository : JpaRepository<File, Long> {
    fun findByUrl(url: String): File?
}
