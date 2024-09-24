package com.klolarion.finance_buddy.service

import com.klolarion.finance_buddy.dto.AuthResponse
import com.klolarion.finance_buddy.entity.Member
import com.klolarion.finance_buddy.repository.MemberRepository
import com.klolarion.finance_buddy.util.JwtTokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OAuth2ProviderService(
        private val memberRepository: MemberRepository,
        private val memberService: MemberService,
        private val jwtTokenProvider: JwtTokenProvider
) {

    @Transactional
    fun handleSocialLogin(provider: String, socialId: String, name: String, account: String): AuthResponse {
        // 소셜 로그인 후 받은 정보로 회원 확인 및 등록
        val member = memberRepository.findByAccount(account) ?: Member(
                account = account,
                memberName = name,
                provider = provider
        ).also { memberRepository.save(it) }

        // 소셜 ID가 일치하는지 확인
        if (member.provider != provider) {
            throw IllegalArgumentException("해당 계정은 다른 소셜 로그인 제공자로 등록되었습니다.")
        }

        // 토큰 발급
        val accessToken = jwtTokenProvider.createAccessToken(member.id)
        val refreshToken = jwtTokenProvider.createRefreshToken(member.id)

        return AuthResponse(accessToken, refreshToken)
    }
}