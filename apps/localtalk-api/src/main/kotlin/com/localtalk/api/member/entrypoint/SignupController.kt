package com.localtalk.api.member.entrypoint

import com.localtalk.api.member.application.SignupApplicationService
import com.localtalk.api.member.entrypoint.document.SignupApi
import com.localtalk.api.member.entrypoint.dto.SignupRequest
import com.localtalk.api.member.entrypoint.mapper.SignupRestMapper
import com.localtalk.common.model.RestResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class SignupController(
    private val signupApplicationService: SignupApplicationService,
    private val signupRestMapper: SignupRestMapper,
) : SignupApi {

    // TODO FormData 스네이크 케이스로 이름 자동 변환하도록 처리 필요
    @PostMapping("/signup")
    fun signup(
        request: SignupRequest,
    ): ResponseEntity<RestResponse<Unit>> {
        val termCommand = signupRestMapper.toTermCommand(request)
        val memberCommand = signupRestMapper.toMemberCommand(request)
        val profileImageCommand = signupRestMapper.toProfileImageCommand(request)
        val districtCommand = signupRestMapper.toDistrictCommand(request)
        val interestCommand = signupRestMapper.toInterestCommand(request)

        signupApplicationService.signup(termCommand, memberCommand, profileImageCommand, districtCommand, interestCommand)

        return ResponseEntity.ok(RestResponse.success(Unit, "회원가입이 완료되었습니다"))
    }
}
