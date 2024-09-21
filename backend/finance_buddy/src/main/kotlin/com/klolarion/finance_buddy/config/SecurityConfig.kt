package com.klolarion.finance_buddy.config

import com.klolarion.finance_buddy.filter.JwtAuthenticationFilter
import com.klolarion.finance_buddy.service.CustomOAuth2UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(private val customOAuth2UserService: CustomOAuth2UserService, private val jwtAuthenticationFilter: JwtAuthenticationFilter) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
                .cors { it.configurationSource(corsConfigurationSource()) } // 최신 방식의 CORS 설정
                .csrf { it.disable() }
                .authorizeHttpRequests { authz ->
                    authz
                            .requestMatchers("/api/auth/**").permitAll() // 인증 관련 API는 허용
                            .anyRequest().authenticated() // 그 외 요청은 인증 필요
                }
                .oauth2Login { oauth2 -> // OAuth2 로그인 설정
                    oauth2
                            .loginPage("/login") // 사용자 정의 로그인 페이지 설정 (옵션)
                            .userInfoEndpoint { userInfo ->
                                userInfo.userService(customOAuth2UserService) // 커스텀 OAuth2UserService 설정
                            }
                            .defaultSuccessUrl("/index") // 로그인 성공 시 이동할 기본 URL
                            .failureUrl("/login?error=true") // 로그인 실패 시 이동할 URL
                }
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    // CORS 설정을 위한 Bean 생성
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration().apply {
            allowedOrigins = listOf("http://localhost:5173") // 허용할 출처 설정
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
            allowedHeaders = listOf("*") // 허용할 헤더
            allowCredentials = true // 자격 증명 허용
            maxAge = 3600 // CORS 설정 캐시 시간 (1시간)
        }
        source.registerCorsConfiguration("/**", config)
        return source
    }
}