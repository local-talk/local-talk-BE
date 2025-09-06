package com.localtalk.api.member.application

import com.localtalk.api.auth.domain.AuthMember
import com.localtalk.api.auth.domain.AuthRole
import com.localtalk.api.auth.domain.LoginToken
import com.localtalk.api.auth.domain.service.SocialLoginService
import com.localtalk.api.auth.domain.service.TokenService
import com.localtalk.api.file.domain.DefaultImageProperties
import com.localtalk.api.file.domain.FileService
import com.localtalk.api.member.application.dto.DistrictCommand
import com.localtalk.api.member.application.dto.InterestCommand
import com.localtalk.api.member.application.dto.MemberCommand
import com.localtalk.api.member.application.dto.ProfileImageCommand
import com.localtalk.api.member.application.dto.TermCommand
import com.localtalk.api.member.domain.MemberService
import org.springframework.stereotype.Service

@Service
class SignupApplicationService(
    private val memberService: MemberService,
    private val fileService: FileService,
    private val defaultImageProperties: DefaultImageProperties,
    private val tokenService: TokenService,
    private val socialLoginService: SocialLoginService,
) {

    fun signup(
        authMember: AuthMember,
        termCommand: TermCommand,
        memberCommand: MemberCommand,
        profileImageCommand: ProfileImageCommand,
        districtCommand: DistrictCommand,
        interestCommand: InterestCommand,
    ): LoginToken {
        val socialLogin = socialLoginService.getUnregisteredSocialLogin(authMember.id)

        val profileImageUrl = profileImageCommand
            .profileImage
            ?: defaultImageProperties.profile

        val member = memberService.signup(
            memberCommand = memberCommand,
            termCommand = termCommand,
            profileImageUrl = profileImageUrl,
            districtCommand = districtCommand,
            interestCommand = interestCommand,
        )

        if (profileImageCommand.profileImage != null) {
            fileService.markAsUsed(profileImageCommand.profileImage, member.id)
        }

        socialLogin.connectToMember(member.id)
        return tokenService.generateToken(member.id, AuthRole.MEMBER)
    }
}
