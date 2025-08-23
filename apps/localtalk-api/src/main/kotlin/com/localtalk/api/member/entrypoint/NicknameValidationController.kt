package com.localtalk.api.member.entrypoint

import com.localtalk.api.member.application.MemberApplicationService
import com.localtalk.api.member.entrypoint.document.NicknameValidationApi
import com.localtalk.api.member.entrypoint.dto.NicknameValidationRequest
import com.localtalk.api.member.entrypoint.mapper.NicknameValidationRestMapper
import com.localtalk.common.model.RestResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class NicknameValidationController(
    val memberApplicationService: MemberApplicationService,
    val nicknameValidationRestMapper: NicknameValidationRestMapper,
) : NicknameValidationApi {

    @PostMapping("/members/nickname/validate")
    override fun validateNickname(
        @RequestBody request: NicknameValidationRequest,
    ): ResponseEntity<RestResponse<Unit>> = 
        nicknameValidationRestMapper.toNickname(request)
            .let { nickname -> memberApplicationService.validateNickname(nickname) }
            .let { RestResponse.success(Unit, "닉네임 검증이 완료되었습니다") }
            .let { ResponseEntity.ok(it) }
}