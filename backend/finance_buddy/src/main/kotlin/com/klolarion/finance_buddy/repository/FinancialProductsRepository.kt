package com.klolarion.finance_buddy.repository
import com.klolarion.finance_buddy.entity.FinancialProducts
import org.springframework.data.domain.Pageable

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
@Repository
interface FinancialProductsRepository : JpaRepository<FinancialProducts, Long> {
    @Query("SELECT fp FROM FinancialProducts fp " +
            "WHERE (fp.riskLevel = :riskLevel) " +
            "AND (fp.duration = :duration) " +
            "AND (fp.type = :type) " +
            "ORDER BY FUNCTION('RAND')")
    fun findRecommendations(
            @Param("riskLevel") riskLevel:
            String?,
            @Param("duration") duration: String?,
            @Param("type") type: String?,
            pageable: Pageable
    ): List<FinancialProducts>
}