package com.localtalk.api.file.entrypoint.mapper

import com.localtalk.api.file.application.dto.FileUploadInfo
import com.localtalk.api.file.domain.FileType
import com.localtalk.api.file.domain.UploadFile
import com.localtalk.api.file.entrypoint.dto.FileUploadResponse
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class FileRestMapper {

    fun toUploadFile(file: MultipartFile, type: String): UploadFile {
        val fileType = FileType.fromString(type)
        return UploadFile(file, fileType)
    }

    fun toFileUploadResponse(info: FileUploadInfo): FileUploadResponse {
        return FileUploadResponse(
            id = info.id,
            url = info.url,
        )
    }
}
