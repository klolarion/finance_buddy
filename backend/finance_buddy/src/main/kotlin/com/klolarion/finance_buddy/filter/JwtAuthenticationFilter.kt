package com.klolarion.finance_buddy.filter

import com.klolarion.finance_buddy.util.JwtTokenProvider
import jakarta.servlet.http.Cookie
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

        if(request.requestURL.contains("/auth")){
            filterChain.doFilter(request, response);
        }


        val accessToken = request.getHeader("access")
        val refreshToken = request.getHeader("refresh")

        println(accessToken)
        println(refreshToken)


        try {
            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
//                val memberId = jwtTokenProvider.getMemberFromToken(accessToken)
//                val userDetails = userDetailsService.loadUserByUsername(memberId)

                val authentication = UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.authorities
                )
                SecurityContextHolder.getContext().authentication = authentication
            } else {
                throw RuntimeException("유효하지 않은 액세스 토큰")
            }
        } catch (ex: Exception) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰 검증 실패")
            return
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