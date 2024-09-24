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
        val accessToken = getCookie(request, "Access-Token") // 쿠키에서 엑세스 토큰 가져오기
        val refreshToken = getCookie(request, "Refresh-Token") // 쿠키에서 리프레시 토큰 가져오기.


        // 엑세스 토큰이 유효하다면?
        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            setAuthentication(accessToken, request)
            // 엑세스 토큰 만료, 리프레시 토큰이 유효하다면 !
        } else if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            val newAccessToken = jwtTokenProvider.refreshAccessToken(refreshToken)
            val newRefreshToken = jwtTokenProvider.createRefreshToken(jwtTokenProvider.getMemberIdFromToken(refreshToken))

            // 새로 발급한 Access Token을 HTTPOnly로 설정
            setCookie(response, "Access-Token", newAccessToken, 3600)
            setCookie(response, "Refresh-Token", newRefreshToken, 3600 * 24 * 30)
            // 새로운 accessToken으로 인증 설정 추가
            setAuthentication(newAccessToken, request)

            // 두 토큰이 모두 유효하지 않은 경우
        } else {
            //읹으 실패 처리 및 에러 띄우기
            response.contentType = "application/json"
            response.characterEncoding = "UTF-8"
            response.writer.write("{\"error\": \"Unauthorized\", \"message\": \"Invalid Token\"}")
            return

        }
        filterChain.doFilter(request, response)

    }

    // 쿠키에서 JWT 토큰 추출하는 메서드
    private fun getCookie(request: HttpServletRequest, name: String): String? {
        val cookies = request.cookies ?: return null
        for (cookie in cookies) {
            if (cookie.name == name) {
                return cookie.value
            }
        }
        return null
    }

    // 쿠키에서 JWT 토큰을 설정하는 메서드
    private fun setCookie(response: HttpServletResponse, name: String, value: String, maxAge: Int) {
        val cookie = Cookie(name, value)
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.maxAge = maxAge
        // 배포할때 https 일때 활성화 해줘야함.
//      cookie.secure = true
        response.addCookie(cookie)
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