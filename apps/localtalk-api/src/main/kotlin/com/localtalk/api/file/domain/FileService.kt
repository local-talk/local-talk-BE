package com.localtalk.api.file.domain

import com.localtalk.s3.storage.FileStorage
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class FileService(
    private val fileRepository: FileRepository,
    private val fileStorage: FileStorage,
) {
    fun uploadFile(
        uploadFile: UploadFile,
        expiresAt: LocalDateTime = LocalDateTime.now().plusHours(24),
    ): File {
        val fileUrl = fileStorage.uploadFile(
            directory = uploadFile.path,
            fileName = uploadFile.uploadName,
            file = uploadFile.file,
        )

        val savedFile = File.create(
            uploadFile = uploadFile,
            url = fileUrl,
            expiresAt = expiresAt,
        )

        return fileRepository.save(savedFile)
    }

}
