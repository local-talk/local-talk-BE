package com.localtalk.api.member.application

import com.localtalk.api.member.application.dto.DistrictCommand
import com.localtalk.api.member.application.dto.InterestCommand
import com.localtalk.api.member.application.dto.MemberCommand
import com.localtalk.api.member.application.dto.ProfileImageCommand
import com.localtalk.api.member.application.dto.TermCommand
import com.localtalk.api.member.domain.MemberService
import com.localtalk.s3.storage.FileStorage
import org.springframework.stereotype.Service

@Service
class SignupApplicationService(
    private val memberService: MemberService,
    private val fileStorage: FileStorage,
) {

    fun signup(
        termCommand: TermCommand,
        memberCommand: MemberCommand,
        profileImageCommand: ProfileImageCommand?,
        districtCommand: DistrictCommand,
        interestCommand: InterestCommand,
    ) {
        val profileImageUrl = profileImageCommand
            ?.profileImage
            ?.let { profileImage -> fileStorage.uploadFile("profiles", "test",  profileImage) }
            ?: "https://example.com/default-profile.jpg"

        memberService.signup(
            memberCommand = memberCommand,
            termCommand = termCommand,
            profileImageUrl = profileImageUrl,
            districtCommand = districtCommand,
            interestCommand = interestCommand,
        )
    }
}
