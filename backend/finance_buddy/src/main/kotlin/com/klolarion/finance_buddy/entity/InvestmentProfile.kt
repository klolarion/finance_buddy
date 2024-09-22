package com.klolarion.finance_buddy.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

@Entity
@Table(name = "investment_profiles")
data class InvestmentProfile(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @field:Positive(message = "Age must be positive")
        val age: Int,

        @field:Positive(message = "Investment amount must be positive")
        var investmentAmount: Double,

        @field:NotBlank(message = "Investment period is required")
        var investmentPeriod: String,

        @field:NotBlank(message = "Preferred product is required")
        var preferredProduct: String
) : BaseTime()
