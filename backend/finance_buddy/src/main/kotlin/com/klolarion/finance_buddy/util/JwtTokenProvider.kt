package com.klolarion.finance_buddy.util
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import io.jsonwebtoken.ExpiredJwtException


@Component
class JwtTokenProvider {

    private val key: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    private val accessTokenValidityInMilliseconds: Long = 1000 * 60 * 15 // Access Token 유효 시간 (15분)
    private val refreshTokenValidityInMilliseconds: Long = 1000 * 60 * 60 * 24 * 7 // Refresh Token 유효 시간 (7일)

    fun createAccessToken(memberId: Long): String {
        return createToken(memberId, accessTokenValidityInMilliseconds)
    }

    fun createRefreshToken(memberId: Long): String {
        return createToken(memberId, refreshTokenValidityInMilliseconds)
    }

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

    fun getMemberIdFromToken(token: String): Long {
        val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
        return claims.subject.toLong()
    }
//
    fun refreshAccessToken(refreshToken: String): String {
        if (validateToken(refreshToken)) {
            val memberId = getMemberIdFromToken(refreshToken)
            return createAccessToken(memberId)
        } else {
            throw IllegalArgumentException("Invalid Refresh Token")
        }
    }
}