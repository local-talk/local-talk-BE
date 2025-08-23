package com.localtalk.api.member.infrastructure.persistence

import com.localtalk.api.member.domain.Nickname
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class NicknameConverter : AttributeConverter<Nickname, String> {
    override fun convertToDatabaseColumn(attribute: Nickname?): String? = attribute?.value
    override fun convertToEntityAttribute(dbData: String?): Nickname? = dbData?.let { Nickname(it) }
}
