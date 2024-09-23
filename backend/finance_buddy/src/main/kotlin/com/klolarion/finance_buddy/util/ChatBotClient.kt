package com.klolarion.finance_buddy.util

import com.klolarion.finance_buddy.dto.Recommendation
import org.springframework.stereotype.Component

@Component
class ChatBotClient {

    fun sendMessage(message: String): List<Recommendation> {
        // 간단한 키워드 추출 로직
        val keywords = extractKeywords(message)

        // 키워드에 따른 더미 데이터 생성
        val dummyResponse = generateDummyResponse(keywords)

        return dummyResponse
    }

    private fun extractKeywords(message: String): List<String> {
        val keywords = listOf("안정", "위험", "단기", "장기", "펀드", "배당", "채권", "연금")
        return keywords.filter { keyword -> message.contains(keyword) }
    }

    private fun generateDummyResponse(keywords: List<String>): List<Recommendation> {
        return when {
            keywords.containsAll(listOf("안정", "장기", "펀드")) -> {
                listOf(
                        Recommendation(
                                message = "안정적이고 장기적인 수익을 내는 펀드 추천: [안정 장기 펀드 A, 펀드 B]",
                                name = "안정 장기 펀드 A",
                                type = "펀드",
                                issuer = "신한자산운용",
                                issueDate = "2024-09-13",
                                category = "자산운용",
                                riskLevel = "낮음"
                        ),
                        Recommendation(
                                message = "안정적이고 장기적인 수익을 내는 펀드 추천: [안정 장기 펀드 A, 펀드 B]",
                                name = "안정 장기 펀드 B",
                                type = "펀드",
                                issuer = "미래에셋자산운용",
                                issueDate = "2024-09-10",
                                category = "자산운용",
                                riskLevel = "낮음"
                        )
                )
            }
            keywords.containsAll(listOf("위험", "단기", "펀드")) -> {
                listOf(
                        Recommendation(
                                message = "단기적인 수익을 내는 고위험 펀드 추천: [위험 단기 펀드 C, 펀드 D]",
                                name = "위험 단기 펀드 C",
                                type = "펀드",
                                issuer = "KB자산운용",
                                issueDate = "2024-09-01",
                                category = "자산운용",
                                riskLevel = "높음"
                        ),
                        Recommendation(
                                message = "단기적인 수익을 내는 고위험 펀드 추천: [위험 단기 펀드 C, 펀드 D]",
                                name = "위험 단기 펀드 D",
                                type = "펀드",
                                issuer = "삼성자산운용",
                                issueDate = "2024-09-05",
                                category = "자산운용",
                                riskLevel = "높음"
                        )
                )
            }
            keywords.containsAll(listOf("안정", "단기", "채권")) -> {
                listOf(
                        Recommendation(
                                message = "단기 채권 관련 추천: [채권 A]",
                                name = "채권 A",
                                type = "채권",
                                issuer = "국채",
                                issueDate = "2023-04-09",
                                expiryDate = "2024-07-30",
                                interestRate = 1.5,
                                category = "국채",
                                riskLevel = "낮음"
                        )
                )
            }
            keywords.containsAll(listOf("안정", "장기", "채권")) -> {
                listOf(
                        Recommendation(
                                message = "장기 채권 관련 추천: [채권 B, 채권 C]",
                                name = "채권 B",
                                type = "채권",
                                issuer = "기업채",
                                issueDate = "2023-04-09",
                                expiryDate = "2027-07-30",
                                interestRate = 2.0,
                                category = "기업채",
                                riskLevel = "낮음"
                        ),
                        Recommendation(
                                message = "장기 채권 관련 추천: [채권 B, 채권 C]",
                                name = "채권 C",
                                type = "채권",
                                issuer = "지방채",
                                issueDate = "2023-05-01",
                                expiryDate = "2028-05-01",
                                interestRate = 2.5,
                                category = "지방채",
                                riskLevel = "낮음"
                        )
                )
            }
            keywords.containsAll(listOf("안정", "장기", "연금")) -> {
                listOf(
                        Recommendation(
                                message = "장기 연금 관련 추천: [연금 A, 연금 B]",
                                name = "연금 A",
                                type = "연금",
                                issuer = "삼성화재",
                                issueDate = "2024-08-31",
                                category = "연금",
                                riskLevel = "낮음"
                        ),
                        Recommendation(
                                message = "장기 연금 관련 추천: [연금 A, 연금 B]",
                                name = "연금 B",
                                type = "연금",
                                issuer = "한화생명",
                                issueDate = "2024-09-01",
                                category = "연금",
                                riskLevel = "낮음"
                        )
                )
            }
            keywords.containsAll(listOf("안정", "장기", "배당")) -> {
                listOf(
                        Recommendation(
                                message = "배당수익을 위한 투자 상품 추천: [수익형 펀드 C]",
                                name = "수익형 펀드 C",
                                type = "펀드",
                                issuer = "NH투자증권",
                                issueDate = "2024-09-15",
                                category = "배당형 펀드",
                                riskLevel = "낮음"
                        )
                )
            }
            else -> {
                listOf(
                        Recommendation(
                                message = "추천 상품 없음",
                                name = "추천 상품 없음",
                                type = "알림",
                                issuer = "시스템",
                                category = "기타",
                                riskLevel = "N/A"
                        )
                )
            }
        }
    }
}
