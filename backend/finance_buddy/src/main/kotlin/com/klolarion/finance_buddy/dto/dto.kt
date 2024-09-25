package com.klolarion.finance_buddy.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive


// 회원가입 요청
data class SignupRequest(
        @field:NotBlank val account: String,
)

// 계정 찾기 응답
data class ProviderResponse(
        val provider: String
)

// 챗봇 요청
data class ChatRequest(
        @field:NotBlank(message = "Message is required")
        val message: String
)

// Recommendation 데이터 클래스 정의
data class Recommendation(
        val message: String,
        val name: String,
        val type: String,
        val issuer: String,
        val issueDate: String? = null,
        val expiryDate: String? = null,
        val price: Number? = null,
        val currency: String? = null,
        val category: String,
        val riskLevel: String? = null,
        val interestRate: Number? = null
)
