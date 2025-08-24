package com.localtalk.api.district.entrypoint

import com.localtalk.api.district.domain.SeongbukDistrict
import com.localtalk.api.support.IntegrationTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType

class DistrictControllerTest : IntegrationTest() {

    @Nested
    inner class `구 동 목록 조회 API를 호출할 때` {

        @Test
        fun `성북구 동 목록을 성공적으로 조회한다`() {
            val expectedDongs = SeongbukDistrict.SEONGBUK_DISTRICT.dongs
            
            val response = webTestClient.get()
                .uri("/api/v1/districts/seongbuk/dongs")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.code").isEqualTo(200)
                .jsonPath("$.message").isEqualTo("성북구 동 목록을 성공적으로 조회했습니다")
                .jsonPath("$.data").isArray
                .jsonPath("$.data.length()").isEqualTo(expectedDongs.size)

            // 모든 동 데이터를 하나씩 정확히 비교
            expectedDongs.forEachIndexed { index, dong ->
                response
                    .jsonPath("$.data[$index].name").isEqualTo(dong.name)
                    .jsonPath("$.data[$index].legal_code").isEqualTo(dong.legalCode.value)
            }
        }

        @Test
        fun `지원하지 않는 구를 조회하면 실패한다`() {
            webTestClient.get()
                .uri("/api/v1/districts/gangnam/dongs")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest
                .expectBody()
                .jsonPath("$.code").isEqualTo(400)
                .jsonPath("$.message").isEqualTo("지원하지 않는 구입니다: gangnam")
                .jsonPath("$.data").isEmpty
        }

    }
}