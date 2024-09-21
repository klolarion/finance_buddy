package com.klolarion.finance_buddy.repository

import com.klolarion.finance_buddy.entity.InvestmentProfile
import org.springframework.data.jpa.repository.JpaRepository

interface InvestmentProfileRepository : JpaRepository<InvestmentProfile, Long> {
}