package com.localtalk.api.district.infrastructure

import com.localtalk.api.district.domain.LegalDongCode
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class LegalDongCodeConverter : AttributeConverter<LegalDongCode, String> {
    override fun convertToDatabaseColumn(attribute: LegalDongCode?): String? = attribute?.value
    override fun convertToEntityAttribute(dbData: String?): LegalDongCode? = 
        dbData?.let { LegalDongCode(it) }
}