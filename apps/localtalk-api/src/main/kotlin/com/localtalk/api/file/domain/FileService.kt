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


    fun markAsUsed(profileImage: String, typeId: Long) {
        val file = fileRepository.findByUrl(profileImage)
            ?: throw IllegalArgumentException("파일을 찾을 수 없습니다: $profileImage")

        file.markAsUsed(typeId)
        fileRepository.save(file)
    }

}
