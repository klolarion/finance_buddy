package com.klolarion.finance_buddy.controller

import com.klolarion.finance_buddy.dto.ChatRequest
import com.klolarion.finance_buddy.dto.ChatResponse
import com.klolarion.finance_buddy.service.ChatBotService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chat")
class ChatBotController(private val chatBotService: ChatBotService) {

    @PostMapping("/request")
    fun handleChatRequest(@RequestBody chatRequest: ChatRequest): ResponseEntity<ChatResponse> {
        val response = chatBotService.processRequest(chatRequest)
        return ResponseEntity.ok(response)
    }
}