// JwtAuthenticationFilter.kt
package com.klolarion.finance_buddy.filter

import com.klolarion.finance_buddy.util.TokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
        private val tokenProvider: TokenProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        // "/auth" 경로는 필터링하지 않고 통과
        if (request.requestURI.contains("/auth")) {
            filterChain.doFilter(request, response)
            return
        }

        val accessToken = request.getHeader("access")
        val refreshToken = request.getHeader("refresh")

        try {
            if (accessToken != null && tokenProvider.validateToken(accessToken)) {
                // Access Token이 유효한 경우
                val memberId = tokenProvider.getMemberFromToken(accessToken)
                setAuthentication(memberId)
            } else if (refreshToken != null && tokenProvider.validateToken(refreshToken)) {
                // Access Token이 만료되었고, Refresh Token이 유효한 경우 Access Token 재발급
                val newAccessToken = tokenProvider.refreshAccessToken(refreshToken)
                response.setHeader("access", newAccessToken)

                val memberId = tokenProvider.getMemberFromToken(refreshToken)
                setAuthentication(memberId)
            } else {
                // 둘 다 만료된 경우 인증 거부
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.")
                return
            }
        } catch (ex: Exception) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰 검증 실패: ${ex.message}")
            return
        }

        // 필터 체인 계속 진행
        filterChain.doFilter(request, response)
    }

    // 인증 객체 설정
    private fun setAuthentication(memberId: Long) {
        val authentication = UsernamePasswordAuthenticationToken(memberId, null, emptyList())
        SecurityContextHolder.getContext().authentication = authentication
    }
}