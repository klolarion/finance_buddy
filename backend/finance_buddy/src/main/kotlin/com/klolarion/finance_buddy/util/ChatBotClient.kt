package com.klolarion.finance_buddy.util

import org.springframework.stereotype.Component

@Component
class ChatBotClient {

    /**
     * 사용자의 메시지를 처리하고 키워드를 추출하여 더미 데이터를 반환합니다.
     *
     * @param message 사용자가 입력한 메시지
     * @return 추출된 키워드와 관련된 더미 데이터
     */
    fun sendMessage(message: String): String {
        // 간단한 키워드 추출 로직
        val keywords = extractKeywords(message)

        // 키워드에 따른 더미 데이터 생성
        val dummyResponse = generateDummyResponse(keywords)

        return dummyResponse
    }

    /**
     * 입력된 메시지에서 특정 키워드를 추출합니다.
     *
     * @param message 사용자가 입력한 메시지
     * @return 추출된 키워드 목록
     */
    private fun extractKeywords(message: String): List<String> {
        val keywords = listOf("안정", "장기", "펀드", "수익", "투자", "단기", "채권")
        return keywords.filter { keyword -> message.contains(keyword) }
    }

    /**
     * 추출된 키워드에 따라 더미 데이터를 반환합니다.
     *
     * @param keywords 추출된 키워드 목록
     * @return 더미 응답 메시지
     */
    private fun generateDummyResponse(keywords: List<String>): String {
        return when {
            keywords.containsAll(listOf("안정", "장기", "펀드")) -> {
                "안정적이고 장기적인 수익을 내는 펀드 추천: [안정 장기 펀드 A, 펀드 B]"
            }
            keywords.contains("채권") -> {
                "채권 관련 추천: [채권 A, 채권 B]"
            }
            keywords.contains("수익") -> {
                "수익을 위한 투자 상품: [수익형 펀드 C]"
            }
            else -> {
                "해당 조건에 맞는 추천이 없습니다. 다른 키워드를 입력해 주세요."
            }
        }
    }
}

