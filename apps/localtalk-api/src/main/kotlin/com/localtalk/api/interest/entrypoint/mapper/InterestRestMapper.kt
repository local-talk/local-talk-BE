package com.localtalk.api.interest.entrypoint.mapper

import com.localtalk.api.interest.application.dto.InterestInfo
import com.localtalk.api.interest.entrypoint.dto.InterestResponse
import org.springframework.stereotype.Component

@Component
class InterestRestMapper {

    fun toResponses(interestInfos: List<InterestInfo>): List<InterestResponse> =
        interestInfos.map { toResponse(it) }

    fun toResponse(interestInfo: InterestInfo): InterestResponse =
        InterestResponse(
            id = interestInfo.id,
            name = interestInfo.name,
            emoji = interestInfo.emoji,
        )
}
