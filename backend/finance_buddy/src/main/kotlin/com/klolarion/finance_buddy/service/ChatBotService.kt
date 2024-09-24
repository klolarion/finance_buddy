package com.klolarion.finance_buddy.service

import com.klolarion.finance_buddy.repository.FinancialProductsRepository
import com.klolarion.finance_buddy.dto.Recommendation
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ChatBotService(
        private val financialProductsRepository: FinancialProductsRepository
) {

    // 챗봇 요청 처리
    fun getRecommendations(inputText: String, riskLevel: String?, duration: String?, type: String?): List<Recommendation>? {

        val pageable: Pageable = PageRequest.of(0, 10)
        // 추천 상품을 검색하여 10개만 무작위로 선택
        val recommendations = financialProductsRepository.findRecommendations(
                riskLevel = riskLevel,
                duration = duration,
                type = type,
                pageable = pageable
        )

        // 10개만 무작위로 선택
        return recommendations?.shuffled()?.take(10)?.map { product ->
            Recommendation(
                    message = inputText,
                    name = product?.name ?: "Unknown",
                    type = product?.type ?: "Unknown",
                    issuer = product?.issuer ?: "Unknown",
                    issueDate = product?.issueDate?.toString(),
                    expiryDate = product?.expiryDate?.toString(),
                    price = product?.price ?: 0.0,
                    currency = product?.currency ?: "KRW",
                    category = product?.category ?: "Unknown",
                    riskLevel = product?.riskLevel,
                    interestRate = product?.interestRate ?: 0.0
            )
        }
    }

}