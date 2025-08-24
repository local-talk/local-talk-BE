package com.localtalk.api.district.domain.service

import com.localtalk.api.district.domain.Dong
import com.localtalk.api.district.domain.SeongbukDistrict
import com.localtalk.api.district.domain.SupportedDistrict
import org.springframework.stereotype.Service

@Service
class DistrictService {

    fun findDongs(district: SupportedDistrict): List<Dong> {
        return when (district) {
            SupportedDistrict.SEONGBUK -> SeongbukDistrict.SEONGBUK_DISTRICT.dongs
        }
    }
}
