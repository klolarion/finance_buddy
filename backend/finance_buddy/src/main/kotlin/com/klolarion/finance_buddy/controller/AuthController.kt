package com.klolarion.finance_buddy.controller

import com.klolarion.finance_buddy.dto.*
import com.klolarion.finance_buddy.service.AuthService
import com.klolarion.finance_buddy.util.TokenProvider
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(private val authService: AuthService,
        private val jwtTokenProvider: TokenProvider) {

    /*가입한 계정 입력*/
    @PostMapping("/login/{account}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun login(@PathVariable account: String, response: HttpServletResponse): ResponseEntity<Any> {

        val member = authService.login(account);

        val accessToken = jwtTokenProvider.createAccessToken(member.id);
        val refreshToken = jwtTokenProvider.createRefreshToken(member.id);
        response.setHeader("Access", accessToken)
        response.setHeader("Refresh", refreshToken)

        return ResponseEntity<Any>(HttpStatus.OK);
    }

    @PostMapping("/signup")
    fun signup(@RequestBody @Valid signupRequest: SignupRequest): ResponseEntity<Any> {
        authService.signup(signupRequest)
        return ResponseEntity<Any>(HttpStatus.OK);
    }


}