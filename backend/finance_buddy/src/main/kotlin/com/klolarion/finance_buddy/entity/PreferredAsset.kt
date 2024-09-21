package com.klolarion.finance_buddy.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "preferred_assets")
data class PreferredAsset(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(unique = true, nullable = false)
        @field:NotBlank
        val name: String // "채권", "펀드", "연금" ...
) : BaseTime()
