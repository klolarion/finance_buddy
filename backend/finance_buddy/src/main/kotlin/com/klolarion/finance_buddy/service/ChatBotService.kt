package com.klolarion.finance_buddy.service

import com.klolarion.finance_buddy.dto.ChatRequest
import com.klolarion.finance_buddy.dto.Recommendation
import com.klolarion.finance_buddy.util.ChatBotClient
import org.springframework.stereotype.Service

@Service
class ChatBotService(private val chatBotClient: ChatBotClient) {

    // 챗봇 요청 처리
    fun processRequest(chatRequest: ChatRequest): List<Recommendation> {
        // 챗봇 API와 상호작용 로직
        val response = chatBotClient.sendMessage(chatRequest.message)
        return response
    }

}