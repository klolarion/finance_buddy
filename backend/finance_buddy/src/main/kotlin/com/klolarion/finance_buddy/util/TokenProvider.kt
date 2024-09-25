package com.klolarion.finance_buddy.util
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

@Component
class TokenProvider(
) {
    // SecretKey 사용
    private val key: SecretKey = generateSecretKey()
    private val accessTokenValidityInMilliseconds = 1000 * 60 * 10L
    private val refreshTokenValidityInMilliseconds = 1000 * 60 * 60 * 24 * 7L

    private val logger = LoggerFactory.getLogger(TokenProvider::class.java)


    // SecretKey 생성 메서드
    private final fun generateSecretKey(): SecretKey {
        // 키를 위한 안전한 랜덤 바이트 생성
        val keyBytes = Decoders.BASE64.decode("A/SneBiWPDZE25AhVgvKxA0+HR9OUiaTMv+iVljMDqA=")
        return Keys.hmacShaKeyFor(keyBytes)
    }



    // Access Token 생성
    fun createAccessToken(memberId: Long): String {
        val token = createToken(memberId, accessTokenValidityInMilliseconds)
        return token
    }

    // Refresh Token 생성
    fun createRefreshToken(memberId: Long): String {
        val token = createToken(memberId, refreshTokenValidityInMilliseconds)
        return token
    }

    // 공통 토큰 생성 메서드
    private fun createToken(memberId: Long, validityInMilliseconds: Long): String {
        val claims: Claims = Jwts.claims().setSubject(memberId.toString())
        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact()
    }

    // 토큰 유효성 검증
    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            true
        } catch (e: ExpiredJwtException) {
            false
        } catch (e: Exception) {
            false
        }
    }

    // 토큰에서 memberId 추출
    fun getMemberFromToken(token: String): Long {
        val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
        return claims.subject.toLong()
    }

    // Refresh Token을 사용하여 Access Token 재발급
    fun refreshAccessToken(refreshToken: String): String {
        if (validateToken(refreshToken)) {
            val memberId = getMemberFromToken(refreshToken)
            logger.info("Refresh Token을 사용하여 새로운 Access Token 생성: Member ID = $memberId")
            return createAccessToken(memberId)
        } else {
            throw IllegalArgumentException("Invalid Refresh Token")
        }
    }
}