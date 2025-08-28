package com.localtalk.api.interest.application

import com.localtalk.api.interest.application.dto.InterestInfo
import com.localtalk.api.interest.application.mapper.InterestApplicationMapper
import com.localtalk.api.interest.domain.InterestService
import org.springframework.stereotype.Service

@Service
class InterestApplicationService(
    val interestService: InterestService,
    val interestApplicationMapper: InterestApplicationMapper,
) {
    fun findAllInterests(): List<InterestInfo> =
        interestService.findAllInterests()
            .let { interestApplicationMapper.toInterestInfos(it) }
}
