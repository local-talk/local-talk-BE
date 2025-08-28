package com.localtalk.api.interest.application.mapper

import com.localtalk.api.interest.application.dto.InterestInfo
import com.localtalk.api.interest.domain.Interest
import org.springframework.stereotype.Component

@Component
class InterestApplicationMapper {

    fun toInterestInfos(interests: List<Interest>): List<InterestInfo> =
        interests.map { toInterestInfo(it) }

    fun toInterestInfo(interest: Interest): InterestInfo =
        InterestInfo(
            id = interest.id,
            name = interest.name,
            emoji = interest.emoji,
        )
}
