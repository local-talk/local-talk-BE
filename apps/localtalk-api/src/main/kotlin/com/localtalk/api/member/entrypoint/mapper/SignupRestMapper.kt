package com.localtalk.api.member.entrypoint.mapper

import com.localtalk.api.member.application.dto.DistrictCommand
import com.localtalk.api.member.application.dto.InterestCommand
import com.localtalk.api.member.application.dto.MemberCommand
import com.localtalk.api.member.application.dto.ProfileImageCommand
import com.localtalk.api.member.application.dto.TermCommand
import com.localtalk.api.member.domain.Gender
import com.localtalk.api.member.entrypoint.dto.SignupRequest
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class SignupRestMapper {
    
    fun toTermCommand(request: SignupRequest): TermCommand {
        return TermCommand(
            marketingConsentAgreed = request.marketingConsentAgreed!!
        )
    }
    
    fun toMemberCommand(request: SignupRequest): MemberCommand {
        return MemberCommand(
            nickname = request.nickname!!,
            birthday = runCatching { LocalDate.parse(request.birthday!!) }
                .getOrElse { throw IllegalArgumentException("생년월일 형식이 올바르지 않습니다: ${request.birthday}") },
            gender = Gender.fromString(request.gender!!)
        )
    }
    
    fun toProfileImageCommand(request: SignupRequest): ProfileImageCommand {
        return ProfileImageCommand(
            profileImage = request.profileImage
        )
    }
    
    fun toDistrictCommand(request: SignupRequest): DistrictCommand {
        return DistrictCommand(
            legalDongCode = request.legalDongCode!!
        )
    }
    
    fun toInterestCommand(request: SignupRequest): InterestCommand {
        return InterestCommand(
            interestIds = request.interests!!
        )
    }
}
