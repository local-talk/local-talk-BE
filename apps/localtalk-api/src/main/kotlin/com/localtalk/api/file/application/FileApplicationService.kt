package com.localtalk.api.file.application

import com.localtalk.api.file.application.dto.FileUploadInfo
import com.localtalk.api.file.domain.FileService
import com.localtalk.api.file.domain.UploadFile
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class FileApplicationService(
    private val fileService: FileService,
) {

    fun uploadFile(
        uploadFile: UploadFile,
        expiresAt: LocalDateTime = LocalDateTime.now().plusHours(24),
    ): FileUploadInfo {
        val savedFile = fileService.uploadFile(uploadFile, expiresAt)
        return FileUploadInfo(
            id = savedFile.id,
            url = savedFile.url,
        )
    }
}
