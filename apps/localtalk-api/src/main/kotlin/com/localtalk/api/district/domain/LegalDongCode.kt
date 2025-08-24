package com.localtalk.api.district.domain

@JvmInline
value class LegalDongCode(val value: String) {
    init {
        require(value.matches(LEGAL_DONG_CODE_PATTERN)) { 
            "올바른 법정동 코드 형식이 아닙니다: $value" 
        }
    }
    
    companion object {
        // 법정동 코드는 10자리 숫자 (시도 2자리 + 시군구 3자리 + 동 5자리)
        internal val LEGAL_DONG_CODE_PATTERN = Regex("^\\d{10}$")
    }
}