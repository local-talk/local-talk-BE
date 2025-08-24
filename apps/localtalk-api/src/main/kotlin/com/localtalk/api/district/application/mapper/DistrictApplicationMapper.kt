package com.localtalk.api.district.application.mapper

import com.localtalk.api.district.application.dto.DongInfo
import com.localtalk.api.district.domain.Dong
import org.springframework.stereotype.Component

@Component
class DistrictApplicationMapper {

    fun toDongInfo(dong: Dong): DongInfo {
        return DongInfo(
            name = dong.name,
            legalCode = dong.legalCode.value
        )
    }

    fun toDongInfoList(dongs: List<Dong>): List<DongInfo> {
        return dongs.map { toDongInfo(it) }
    }
}