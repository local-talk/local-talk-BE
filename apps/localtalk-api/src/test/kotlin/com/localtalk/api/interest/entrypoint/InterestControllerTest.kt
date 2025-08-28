package com.localtalk.api.interest.entrypoint

import com.localtalk.api.interest.domain.Interest
import com.localtalk.api.interest.domain.InterestRepository
import com.localtalk.api.support.IntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class InterestControllerTest : IntegrationTest() {

    @Autowired
    lateinit var interestRepository: InterestRepository

    @Test
    fun `관심사 목록을 조회할 수 있다`() {
        val interests = listOf(
            Interest(name = "데이트", emoji = "💖", displayOrder = 1),
            Interest(name = "자기계발", emoji = "📖", displayOrder = 2),
            Interest(name = "쇼핑", emoji = "👕", displayOrder = 3),
        )
        interestRepository.saveAll(interests)

        webTestClient.get()
            .uri("/api/v1/interests")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.code").isEqualTo(200)
            .jsonPath("$.message").isEqualTo("관심사 목록 조회가 완료되었습니다")
            .jsonPath("$.data").isArray
            .jsonPath("$.data.length()").isEqualTo(3)
            .jsonPath("$.data[0].name").isEqualTo("데이트")
            .jsonPath("$.data[0].emoji").isEqualTo("💖")
            .jsonPath("$.data[1].name").isEqualTo("자기계발")
            .jsonPath("$.data[1].emoji").isEqualTo("📖")
            .jsonPath("$.data[2].name").isEqualTo("쇼핑")
            .jsonPath("$.data[2].emoji").isEqualTo("👕")
    }

}
