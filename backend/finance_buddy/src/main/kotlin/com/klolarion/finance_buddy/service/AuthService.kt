package com.klolarion.finance_buddy.service

import com.klolarion.finance_buddy.dto.*
import com.klolarion.finance_buddy.entity.Member
import com.klolarion.finance_buddy.repository.MemberRepository
import com.klolarion.finance_buddy.util.JwtTokenProvider
import com.nimbusds.openid.connect.sdk.LogoutRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AuthService(
        private val memberRepository: MemberRepository,
        private val jwtTokenProvider: JwtTokenProvider
) {

    // 로그인 처리
    fun login(account: String): ProviderResponse {
        val member = memberRepository.findByAccount(account)
                ?: throw IllegalArgumentException("Account not found")
        // 토큰 발급 및 기타 로그인 관련 로직
        return ProviderResponse(provider = member.provider)
    }

    // 회원가입 처리
    fun signup(signupRequest: SignupRequest): AuthResponse {
        val newUser = Member(
                account = signupRequest.account,
        )
        memberRepository.save(newUser)
        // 회원가입 후 토큰 발급 등 추가 로직
        return AuthResponse(token = "generated-token")
    }

    // 계정 찾기 처리
    fun findAccount(memberName: String): ProviderResponse {
        val user = memberRepository.findByMemberName(memberName)
                ?: throw IllegalArgumentException("User not found")
        return ProviderResponse(provider = user.provider)
    }

    // 소셜 로그인 처리
    fun socialLogin(provider: String, code: String): AuthResponse {
        // 소셜 로그인 처리 로직 (예: OAuth 인증 등)
        val user = authenticateSocialUser(provider, code)
        return AuthResponse(token = "generated-token", provider = provider)
    }

    // 예시 메서드: 소셜 사용자 인증
    private fun authenticateSocialUser(provider: String, code: String): Member {
        // 실제 소셜 로그인 인증 로직
        // 예: OAuth를 통해 토큰 교환 및 사용자 정보 조회
        return Member(email = "social@user.com", provider = provider)
    }



    // 멤버 등록 처리
    fun registerMember(member: Member): Member {
        return memberRepository.save(member)
    }
}