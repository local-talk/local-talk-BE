package com.localtalk.api.member.entrypoint.dto

import org.springframework.web.multipart.MultipartFile

data class SignupRequest(
    val marketingConsentAgreed: Boolean?,
    val nickname: String?,
    val profileImage: MultipartFile?,
    val birthday: String?,
    val gender: String?,
    val legalDongCode: String?,
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
