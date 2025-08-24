package com.localtalk.api.district.entrypoint

import com.localtalk.api.district.application.DistrictApplicationService
import com.localtalk.api.district.entrypoint.document.DistrictApi
import com.localtalk.api.district.entrypoint.dto.DongResponse
import com.localtalk.api.district.entrypoint.mapper.DistrictRestMapper
import com.localtalk.common.model.RestResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class DistrictController(
    private val districtApplicationService: DistrictApplicationService,
    private val districtRestMapper: DistrictRestMapper,
) : DistrictApi {

    @GetMapping("/districts/{district}/dongs")
    override fun findDongs(
        @PathVariable district: String,
    ): ResponseEntity<RestResponse<List<DongResponse>>> {
        val supportedDistrict = districtRestMapper.toSupportedDistrict(district)

        return districtApplicationService.findDongs(supportedDistrict)
            .let { dongInfos -> districtRestMapper.toDongResponseList(dongInfos) }
            .let { dongResponses -> RestResponse.success(dongResponses, "${supportedDistrict.displayName} 동 목록을 성공적으로 조회했습니다") }
            .let { ResponseEntity.ok(it) }
    }
}
