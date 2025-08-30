package com.localtalk.api.interest.entrypoint

import com.localtalk.api.interest.application.InterestApplicationService
import com.localtalk.api.interest.entrypoint.document.InterestApi
import com.localtalk.api.interest.entrypoint.dto.InterestResponse
import com.localtalk.api.interest.entrypoint.mapper.InterestRestMapper
import com.localtalk.common.model.RestResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class InterestController(
    val interestApplicationService: InterestApplicationService,
    val interestRestMapper: InterestRestMapper,
) : InterestApi {

    override fun findAllInterests(): ResponseEntity<RestResponse<List<InterestResponse>>> =
        interestApplicationService.findAllInterests()
            .let { interestRestMapper.toResponses(it) }
            .let { RestResponse.success(it, "관심사 목록 조회가 완료되었습니다") }
            .let { ResponseEntity.ok(it) }
}
