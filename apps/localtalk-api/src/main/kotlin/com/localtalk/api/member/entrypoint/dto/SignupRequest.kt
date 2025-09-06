package com.localtalk.api.member.entrypoint.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "회원가입 요청")
data class SignupRequest(
    @field:Schema(description = "마케팅 동의 여부", example = "true", required = true)
    val marketingConsentAgreed: Boolean?,
    
    @field:Schema(description = "사용자 닉네임", example = "로컬톡유저", required = true, maxLength = 20)
    val nickname: String?,
    
    @field:Schema(description = "프로필 이미지 URL", example = "https://localtalk-storage.s3.us-east-1.amazonaws.com/profile/example.jpg", required = false)
    val profileImageUrl: String?,
    
    @field:Schema(description = "생년월일 (YYYY-MM-DD 형식)", example = "1995-03-15", required = true, pattern = "^\\d{4}-\\d{2}-\\d{2}$")
    val birthday: String?,
    
    @field:Schema(description = "성별", example = "MALE", required = true, allowableValues = ["MALE", "FEMALE", "NONE"])
    val gender: String?,
    
    @field:Schema(description = "법정동 코드 (10자리 숫자)", example = "1129010100", required = true, pattern = "^\\d{10}$")
    val legalDongCode: String?,
    
    @field:Schema(description = "관심사 ID 목록", example = "[1, 3, 5]", required = true)
    val interests: List<Long>?,
) {
    init {
        requireNotNull { "nickname은 필수입니다. 현재값: $nickname" }
        requireNotNull { "birthday는 필수입니다. 현재값: $birthday" }
        requireNotNull { "gender는 필수입니다. 현재값: $gender" }
        requireNotNull { "legalDongCode는 필수입니다. 현재값: $legalDongCode" }
        requireNotNull { "marketingConsentAgreed는 필수입니다. 현재값: $marketingConsentAgreed" }
        requireNotNull { "interests는 필수입니다. 현재값: $interests" }
    }
}
