package com.klolarion.finance_buddy.service

import com.klolarion.finance_buddy.entity.Member
import com.klolarion.finance_buddy.repository.MemberRepository
import com.klolarion.finance_buddy.util.JwtTokenProvider
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomOAuth2UserService(
        private val memberRepository: MemberRepository,
        private val jwtTokenProvider: JwtTokenProvider
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Transactional
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        // 기본 OAuth2UserService를 통해 사용자 정보 가져오기
        val delegate = DefaultOAuth2UserService()
        val oAuth2User = delegate.loadUser(userRequest)



        // 사용자 정보를 가져온 후 커스터마이즈 처리
        val attributes = oAuth2User.attributes
        val registrationId = userRequest.clientRegistration.registrationId
        val userNameAttributeName = userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName

        // 사용자 정보를 데이터베이스에 저장하거나, 추가 로직 처리
        val member = saveOrUpdateMember(registrationId, attributes)

        // Access Token과 Refresh Token 발행
        val accessToken = jwtTokenProvider.createAccessToken(member.id)
        val refreshToken = jwtTokenProvider.createRefreshToken(member.id)

        // 사용자 객체 반환 (필요에 따라 DefaultOAuth2User를 커스터마이즈 가능)
        return DefaultOAuth2User(
                oAuth2User.authorities,
                attributes,
                userNameAttributeName
        )
    }

    // 사용자 정보를 저장 또는 업데이트하는 메서드 (커스터마이즈 필요)
    private fun saveOrUpdateMember(registrationId: String, attributes: Map<String, Any>): Member {
        // 사용자 정보를 안전하게 추출
        val email = attributes["email"] as? String ?: throw IllegalArgumentException("Email not found")
        val name = attributes["name"] as? String ?: "Unknown"

        // 사용자 정보를 저장 또는 업데이트하는 로직
        val member = memberRepository.findByAccount(email) ?: Member(email = email, memberName = name)
        member.memberName = name // 필요시 추가 업데이트
        return memberRepository.save(member) // 저장 또는 업데이트된 멤버 반환
    }
}