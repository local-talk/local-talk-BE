package com.localtalk.api.auth.entrypoint

import com.localtalk.api.auth.application.SocialLoginApplicationService
import com.localtalk.api.auth.domain.SocialLoginProvider
import com.localtalk.api.auth.entrypoint.dto.SocialLoginRequest
import com.localtalk.api.auth.entrypoint.dto.SocialLoginResponse
import com.localtalk.api.auth.entrypoint.mapper.SocialLoginRestMapper
import com.localtalk.common.model.RestResponse
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class SocialLoginController(
    val socialLoginApplicationService: SocialLoginApplicationService,
    val socialLoginRestMapper: SocialLoginRestMapper,
) {

    @PostMapping("/social-logins/{provider}")
    fun socialLogin(
        @PathVariable provider: String,
        @RequestBody request: SocialLoginRequest,
    ): ResponseEntity<RestResponse<SocialLoginResponse>> = runBlocking {
        SocialLoginProvider.fromString(provider)
            .let { socialProvider -> socialLoginRestMapper.toCommand(request, socialProvider) }
            .let { command -> socialLoginApplicationService.processSocialLogin(command) }
            .let { result -> socialLoginRestMapper.toResponse(result) }
            .let { response -> RestResponse.success(response, "소셜 로그인이 성공했습니다") }
            .let { ResponseEntity.ok(it) }
    }
}
