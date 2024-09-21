package com.klolarion.finance_buddy.filter

import com.klolarion.finance_buddy.util.JwtTokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
        private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val accessToken = resolveToken(request)
        val refreshToken = request.getHeader("Refresh-Token")

        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            setAuthentication(accessToken, request)
        } else if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            // Access Token이 만료되고 Refresh Token이 유효한 경우
            val newAccessToken = jwtTokenProvider.refreshAccessToken(refreshToken)
            response.setHeader("Access-Token", newAccessToken) // 새로 발행한 Access Token을 응답 헤더에 설정
        }

        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }

    private fun setAuthentication(token: String, request: HttpServletRequest) {
        val memberId = jwtTokenProvider.getMemberIdFromToken(token)
        val authentication = UsernamePasswordAuthenticationToken(memberId, null, emptyList())
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authentication
    }
}