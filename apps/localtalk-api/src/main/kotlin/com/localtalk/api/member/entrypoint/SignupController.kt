package com.localtalk.api.member.entrypoint

import com.localtalk.api.auth.domain.AuthMember
import com.localtalk.api.member.application.SignupApplicationService
import com.localtalk.api.member.entrypoint.document.SignupApi
import com.localtalk.api.member.entrypoint.dto.SignupRequest
import com.localtalk.api.member.entrypoint.dto.SignupResponse
import com.localtalk.api.member.entrypoint.mapper.SignupRestMapper
import com.localtalk.common.model.RestResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class SignupController(
    private val signupApplicationService: SignupApplicationService,
    private val signupRestMapper: SignupRestMapper,
) : SignupApi {

    @PostMapping("/signup")
    override fun signup(
        @RequestBody request: SignupRequest,
        @AuthenticationPrincipal authMember: AuthMember,
    ): ResponseEntity<RestResponse<SignupResponse>> {
        val termCommand = signupRestMapper.toTermCommand(request)
        val memberCommand = signupRestMapper.toMemberCommand(request)
        val profileImageCommand = signupRestMapper.toProfileImageCommand(request)
        val districtCommand = signupRestMapper.toDistrictCommand(request)
        val interestCommand = signupRestMapper.toInterestCommand(request)

        val loginToken = signupApplicationService.signup(authMember, termCommand, memberCommand, profileImageCommand, districtCommand, interestCommand)
        val response = signupRestMapper.toResponse(loginToken)

        return ResponseEntity.ok(RestResponse.success(response, "회원가입이 완료되었습니다"))
    }
}
