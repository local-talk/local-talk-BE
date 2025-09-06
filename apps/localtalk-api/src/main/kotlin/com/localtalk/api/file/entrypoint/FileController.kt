package com.localtalk.api.file.entrypoint

import com.localtalk.api.file.application.FileApplicationService
import com.localtalk.api.file.entrypoint.document.FileApi
import com.localtalk.api.file.entrypoint.dto.FileUploadResponse
import com.localtalk.api.file.entrypoint.mapper.FileRestMapper
import com.localtalk.common.model.RestResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/files")
class FileController(
    private val fileApplicationService: FileApplicationService,
    private val fileRestMapper: FileRestMapper,
) : FileApi {

    @PostMapping
    override suspend fun uploadFile(
        @RequestPart file: MultipartFile,
        @RequestParam type: String,
    ): ResponseEntity<RestResponse<FileUploadResponse>> {
        return fileRestMapper.toUploadFile(file, type)
            .let { uploadFile -> fileApplicationService.uploadFile(uploadFile) }
            .let { info -> fileRestMapper.toFileUploadResponse(info) }
            .let { response -> RestResponse.success(response, "파일 업로드가 완료되었습니다") }
            .let { ResponseEntity.ok(it) }
    }
}
