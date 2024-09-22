package com.klolarion.finance_buddy.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "member")
class Member(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Column(unique = true, nullable = false)
        @field:NotBlank
        var account: String = "",

        @Column(unique = true, nullable = false)
        @field:Email
        var email: String = "",

        @Column(nullable = false)
        var memberName: String = "",

        var provider: String = "",
        var socialId: String = "",

        @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
        @JoinColumn(name = "investment_profile_id", referencedColumnName = "id")
        var investmentProfile: InvestmentProfile? = null
) : BaseTime()


