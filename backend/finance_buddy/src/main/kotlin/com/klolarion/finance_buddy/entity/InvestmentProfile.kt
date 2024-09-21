package com.klolarion.finance_buddy.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "investment_profiles")
data class InvestmentProfile(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(nullable = false)
        @field:NotBlank
        val riskTolerance: String,

        @Column(nullable = false)
        @field:NotBlank
        val investmentHorizon: String,

        // PreferredAsset과의 관계 설정: OneToMany 예시
        @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinColumn(name = "investment_profile_id")
        val preferredAssets: Set<PreferredAsset> = emptySet(),

        @Column(nullable = false)
        val funds: Double
) : BaseTime()
