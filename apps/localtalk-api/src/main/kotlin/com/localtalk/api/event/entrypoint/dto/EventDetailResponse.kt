package com.localtalk.api.event.entrypoint.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate

@Schema(description = "행사 상세조회 응답")
data class EventDetailResponse(
    @field:Schema(description = "행사 제목", example = "성북구 겨울 축제")
    val title: String,

    @field:Schema(description = "행사 설명", example = "성북구에서 열리는 겨울 축제입니다.")
    val description: String?,

    @field:Schema(description = "이미지 URL", example = "https://example/default.jpg")
    val imageUrl: String,

    @field:Schema(description = "행사 시작 일시", example = "2024-12-01")
    val startDate: LocalDate,

    @field:Schema(description = "행사 종료 일시", example = "2024-12-31")
    val endDate: LocalDate?,

    @field:Schema(description = "운영 시간", example = "매일 10:00~18:00")
    val operatingHours: String?,

    @field:Schema(description = "행사 장소", example = "성북구청 앞 광장")
    val location: String?,

    @field:Schema(description = "상세주소", example = "서울특별시 성북구 보문로 168")
    val address: String?,

    @field:Schema(description = "가격", example = "0")
    val price: Int,

    @field:Schema(description = "문의 전화번호", example = "02-1234-5678")
    val contactPhone: String?,

    @field:Schema(description = "공식 웹사이트", example = "https://seongbuk.go.kr")
    val officialWebsite: String?,

    @field:Schema(description = "경도", example = "127.0167")
    val latitude: BigDecimal?,

    @field:Schema(description = "위도", example = "37.5665")
    val longitude: BigDecimal?,

    @field:Schema(description = "로그인 여부", example = "true")
    val isLoggedIn: Boolean,

    @field:Schema(description = "북마크 여부", example = "false")
    val isBookmarked: Boolean,

    @field:Schema(description = "방문 여부", example = "true")
    val isVisited: Boolean,

    @field:Schema(description = "리뷰 평균", example = "4.5")
    val averageRating: Double,

    @field:Schema(description = "총 리뷰 수", example = "12")
    val totalCount: Int,
)
