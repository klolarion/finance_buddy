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

        // OAuth2 제공자 정보 가져오기 (구글, 네이버 구분해서)
        val attributes = oAuth2User.attributes
        val registrationId = userRequest.clientRegistration.registrationId
        val userNameAttributeName =
            userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName

        // 사용자 정보를 DB 저장하거나 추가 로직 처리
        val member = saveOrUpdateMember(registrationId, attributes)

        // Access Token과 Refresh Token 발행
        val accessToken = jwtTokenProvider.createAccessToken(member.id)
        val refreshToken = jwtTokenProvider.createRefreshToken(member.id)

        // 콘솔에 토큰 정보 출력
        println("Access Token: $accessToken")
        println("Refresh Token: $refreshToken")

        // 사용자 객체 반환 (필요에 따라 DefaultOAuth2User를 커스터마이즈 가능)
        return DefaultOAuth2User(
            oAuth2User.authorities,
            attributes,
            userNameAttributeName
        )
    }

    // 사용자 정보를 저장 또는 업데이트하는 메서드 (커스터마이즈 필요)
    private fun saveOrUpdateMember(registrationId: String, attributes: Map<String, Any>): Member {
        val email: String?
        val name: String?
        val id: String?

        // 구글과 네이버에 따라 다른 방식으로 사용자 정보를 추출
        when (registrationId) {
            "google" -> {
                email = attributes["email"] as? String
                name = attributes["name"] as? String ?: "Unknown"
                id = attributes["sub"] as? String // Google의 경우, 'sub' 필드가 고유 ID
            }
            "naver" -> {
                // 네이버는 response 안에 사용자 정보가 있음
                val response = attributes["response"] as? Map<String, Any>
                    ?: throw IllegalArgumentException("네이버 응답에서 사용자 정보를 찾을 수 없습니다.")
                id = response["id"] as? String ?: throw IllegalArgumentException("Missing attribute 'response.id' in attributes")
                email = response["email"] as? String
                name = response["name"] as? String ?: "Unknown"
            }
            else -> throw IllegalArgumentException("지원하지 않는 OAuth2 공급자입니다.")
        }
        println("네이버 응답: $attributes")

        email ?: throw IllegalArgumentException("이메일을 찾을 수 없습니다.")
        val account = email // email을 account로 사용

        // 사용자 정보를 저장 또는 업데이트하는 로직
        val member = memberRepository.findByAccount(account)
            ?: Member(account = account, email = email, memberName = name)  // 존재하지 않으면 새 사용자 생성
        member.memberName = name // 필요 시 이름 업데이트
        return memberRepository.save(member) // 저장 또는 업데이트된 멤버 반환
    }
}