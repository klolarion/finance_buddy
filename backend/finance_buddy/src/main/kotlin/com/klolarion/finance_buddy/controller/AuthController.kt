package com.klolarion.finance_buddy.controller

import com.klolarion.finance_buddy.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
        private val authService: AuthService
) {

    @GetMapping("/provider/{email}")
    fun checkProvider(@PathVariable email: String) : ResponseEntity<String> {
        val savedProvider = authService.checkProvider(email)
        return ResponseEntity.status((HttpStatus.OK)).body(savedProvider)
    }
}