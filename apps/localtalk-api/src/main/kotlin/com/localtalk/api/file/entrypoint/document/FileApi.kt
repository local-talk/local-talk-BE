package com.localtalk.api.file.entrypoint.document

import com.localtalk.api.file.entrypoint.dto.FileUploadResponse
import com.localtalk.common.model.RestResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile

@Tag(name = "파일 관리", description = "파일 관련 API")
interface FileApi {

    @Operation(
        summary = "파일 업로드",
        description = "전달한 파일 업로드를 진행합니다. type 파라미터를 통해 어떤 이미지인지 지정합니다. 현재는 profile(프로필 이미지)만 지원합니다.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "파일 업로드가 완료되었습니다",
                content = [
                    Content(
                        mediaType = "application/json",
                        examples = [
                            ExampleObject(
                                name = "성공 응답",
                                description = "파일 업로드 성공",
                            )
                        ]
                    )
                ]
            )
        ]
    )
    suspend fun uploadFile(
        file: MultipartFile,
        type: String,
    ): ResponseEntity<RestResponse<FileUploadResponse>>
}
