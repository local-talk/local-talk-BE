package com.localtalk.api.event.fixture

import com.localtalk.api.event.domain.Event
import com.localtalk.api.member.domain.Member
import com.localtalk.api.member.fixture.MemberFixture
import java.time.LocalDate

object EventFixture {

    fun createEvent(
        title: String = "테스트 행사 제목",
        description: String? = "테스트 행사 설명",
        eventImageKey: String = "test/event-image.jpg",
        startDate: LocalDate = LocalDate.of(2025, 8, 29),
        endDate: LocalDate? = LocalDate.of(2025, 8, 31),
        operatingHours: String? = "10:00~18:00",
        location: String? = "테스트 장소",
        address: String? = "서울시 테스트구 테스트동",
        price: Int = 5000,
        contactPhone: String? = "02-1234-5678",
        officialWebsite: String? = "https://test.com",
        member: Member = MemberFixture.createMember()
    ): Event {
        return Event(
            title = title,
            description = description,
            eventImageKey = eventImageKey,
            startDate = startDate,
            endDate = endDate,
            operatingHours = operatingHours,
            location = location,
            address = address,
            price = price,
            contactPhone = contactPhone,
            officialWebsite = officialWebsite,
            latitude = null,
            longitude = null,
            member = member
        )
    }
}