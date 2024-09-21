package com.klolarion.finance_buddy.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "investment_recommendations")
data class InvestmentRecommendation(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(nullable = false)
        @field:NotBlank
        val recommendationType: String, // 예: 채권, 펀드

        @Column(nullable = false)
        val description: String, // 추천 이유나 설명

        @Column(nullable = false)
        val riskLevel: String, // 추천된 투자 상품의 리스크 수준
) : BaseTime()