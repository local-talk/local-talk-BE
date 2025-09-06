package com.localtalk.api.file.domain

import com.localtalk.domain.SoftDeleteBaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.hibernate.annotations.Comment
import java.time.LocalDateTime

@Entity
@Table(name = "file")
class File(

    @Column(name = "origin_file_name", nullable = false)
    @Comment("원본 파일명")
    val originFileName: String,

    @Column(name = "name", nullable = false)
    @Comment("업로드 파일명")
    val name: String,

    @Column(name = "path", nullable = false)
    @Comment("업로드 파일 경로")
    val path: String,

    @Column(name = "url", nullable = false)
    @Comment("파일 업로드 URL")
    val url: String,

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("파일 타입")
    val type: FileType,

    @Column(name = "type_id")
    @Comment("파일 타입 ID")
    val typeId: Long = 0,

    @Column(name = "expires_at", nullable = true)
    @Comment("파일 만료 일시")
    var expiresAt: LocalDateTime?,

    ) : SoftDeleteBaseEntity() {

    companion object {
        fun create(
            uploadFile: UploadFile,
            url: String,
            expiresAt: LocalDateTime = LocalDateTime.now().plusHours(24),
        ): File {
            return File(
                originFileName = uploadFile.originFileName,
                name = uploadFile.uploadName,
                path = uploadFile.path,
                url = url,
                type = uploadFile.type,
                expiresAt = expiresAt,
            )
        }
    }
}
