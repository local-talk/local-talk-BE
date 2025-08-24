package com.localtalk.api.district.application

import com.localtalk.api.district.application.dto.DongInfo
import com.localtalk.api.district.application.mapper.DistrictApplicationMapper
import com.localtalk.api.district.domain.SupportedDistrict
import com.localtalk.api.district.domain.service.DistrictService
import org.springframework.stereotype.Service

@Service
class DistrictApplicationService(
    val districtService: DistrictService,
    val districtApplicationMapper: DistrictApplicationMapper,
) {

    fun findDongs(district: SupportedDistrict): List<DongInfo> {
        return districtService.findDongs(district)
            .let { dongs -> districtApplicationMapper.toDongInfoList(dongs) }
    }
}
