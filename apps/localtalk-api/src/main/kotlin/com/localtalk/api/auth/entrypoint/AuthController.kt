package com.localtalk.api.auth.entrypoint

import com.localtalk.api.auth.application.TokenApplicationService
import com.localtalk.api.auth.entrypoint.document.AuthApi
import com.localtalk.api.auth.entrypoint.dto.TokenRefreshRequest
import com.localtalk.api.auth.entrypoint.dto.TokenRefreshResponse
import com.localtalk.api.auth.entrypoint.mapper.AuthRestMapper
import com.localtalk.common.model.RestResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    val tokenApplicationService: TokenApplicationService,
    val authRestMapper: AuthRestMapper,
) : AuthApi {

    @PostMapping("/refresh")
    override fun rotateRefreshToken(
        @RequestBody request: TokenRefreshRequest,
    ): ResponseEntity<RestResponse<TokenRefreshResponse>> {
        return authRestMapper.toCommand(request)
            .let { command -> tokenApplicationService.rotateRefreshToken(command) }
            .let { info -> authRestMapper.toResponse(info) }
            .let { response -> RestResponse.success(response, "토큰이 성공적으로 갱신되었습니다") }
            .let { ResponseEntity.ok(it) }
    }
}
