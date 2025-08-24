package com.localtalk.api.district.entrypoint.mapper

import com.localtalk.api.district.application.dto.DongInfo
import com.localtalk.api.district.domain.SupportedDistrict
import com.localtalk.api.district.entrypoint.dto.DongResponse
import org.springframework.stereotype.Component

@Component
class DistrictRestMapper {
    
    fun toSupportedDistrict(pathName: String): SupportedDistrict {
        return SupportedDistrict.getByPathName(pathName)
    }
    
    fun toDongResponse(dongInfo: DongInfo): DongResponse {
        return DongResponse(
            name = dongInfo.name,
            legalCode = dongInfo.legalCode
        )
    }
    
    fun toDongResponseList(dongInfos: List<DongInfo>): List<DongResponse> {
        return dongInfos.map { toDongResponse(it) }
    }
}