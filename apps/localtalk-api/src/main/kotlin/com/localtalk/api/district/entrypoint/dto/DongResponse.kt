package com.localtalk.api.district.entrypoint.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "동 정보 응답")
data class DongResponse(
    @Schema(description = "동 이름", example = "길음1동")
    val name: String,
    
    @Schema(description = "법정동 코드 (10자리)", example = "1129010100")
    val legalCode: String
)