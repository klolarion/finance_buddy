package com.klolarion.finance_buddy.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import io.jsonwebtoken.ExpiredJwtException
import org.slf4j.LoggerFactory

@Component
class JwtTokenProvider {

    private val logger = LoggerFactory.getLogger(JwtTokenProvider::class.java)
    private val key: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    private val accessTokenValidityInMilliseconds: Long = 1000 * 60 * 15 // Access Token 유효 시간 (15분)
    private val refreshTokenValidityInMilliseconds: Long = 1000 * 60 * 60 * 24 * 7 // Refresh Token 유효 시간 (7일)

    // Access Token 생성
    fun createAccessToken(memberId: Long): String {
        logger.info("Access Token 생성 중: Member ID = $memberId")
        val token = createToken(memberId, accessTokenValidityInMilliseconds)
        logger.info("Access Token 생성 완료: $token")
        return token
    }

    // Refresh Token 생성
    fun createRefreshToken(memberId: Long): String {
        logger.info("Refresh Token 생성 중: Member ID = $memberId")
        val token = createToken(memberId, refreshTokenValidityInMilliseconds)
        logger.info("Refresh Token 생성 완료: $token")
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
            logger.info("토큰 검증 성공: $token")
            true
        } catch (e: ExpiredJwtException) {
            logger.warn("토큰 만료: $token")
            false
        } catch (e: Exception) {
            logger.error("토큰 검증 실패: ${e.message}")
            false
        }
    }

    // 토큰에서 memberId 추출
    fun getMemberIdFromToken(token: String): Long {
        val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
        logger.info("토큰에서 추출된 Member ID: ${claims.subject}")
        return claims.subject.toLong()
    }
    fun refreshAccessToken(refreshToken: String): String {
        if (validateToken(refreshToken)) {
            val memberId = getMemberIdFromToken(refreshToken)
            logger.info("Refresh Token을 사용하여 새로운 Access Token 생성: Member ID = $memberId")
            return createAccessToken(memberId)
        } else {
            logger.error("유효하지 않은 Refresh Token: $refreshToken")
            throw IllegalArgumentException("Invalid Refresh Token")
        }
    }
}