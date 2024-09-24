package com.klolarion.finance_buddy.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "financial_products")
class FinancialProducts(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(nullable = false)
        val name: String,

        @Column(nullable = false)
        val type: String? = null,

        val issuer: String? = null,

        val issueDate: LocalDate? = null,

        val expiryDate: LocalDate? = null,

        @Column(precision = 15, scale = 2)
        val price: BigDecimal? = null,

        @Column(length = 10, nullable = false, columnDefinition = "varchar(10) default 'KRW'")
        val currency: String = "KRW",

        val category: String? = null,

        @Column(length = 50)
        val riskLevel: String? = null,

        @Column(precision = 5, scale = 2)
        val interestRate: BigDecimal? = null,

        val duration: String? = null,

        @Column(length = 50)
        val returnType: String? = null
)
