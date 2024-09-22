package com.klolarion.finance_buddy.controller

import com.klolarion.finance_buddy.dto.*
import com.klolarion.finance_buddy.service.AuthService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(private val authService: AuthService) {

    /*가입한 계정 입력*/
    @PostMapping("/login/{account}")
    fun login(@PathVariable account: String): ProviderResponse {
        return authService.login(account)
    }

    @PostMapping("/signup")
    fun signup(@RequestBody @Valid signupRequest: SignupRequest): AuthResponse {
        return authService.signup(signupRequest)
    }

    @GetMapping("/find-account")
    fun findAccount(@RequestParam email: String): ProviderResponse {
        return authService.findAccount(email)
    }

    @GetMapping("/social-login/{provider}")
    fun socialLogin(@PathVariable provider: String, @RequestParam code: String): AuthResponse {
        return authService.socialLogin(provider, code)
    }

}