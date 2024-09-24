package com.klolarion.finance_buddy.controller

import com.klolarion.finance_buddy.dto.ChatRequest
import com.klolarion.finance_buddy.dto.Recommendation
import com.klolarion.finance_buddy.service.ChatBotService
import org.springframework.http.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("/api/chat")
class ChatBotController(
        private val restTemplate: RestTemplate,
        private val chatBotService: ChatBotService) {

    @PostMapping("/request")
    fun handleChatRequest(@RequestBody chatRequest: ChatRequest): ResponseEntity<out Any?>? {
//        val response = chatBotService.processRequest(chatRequest)

        val inputText = chatRequest.message;
        val flaskUrl = "http://localhost:5001/predict" // Flask server URL

        val requestBody = mapOf("input_text" to inputText)

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }

        val entity = HttpEntity(requestBody, headers)

        val response = restTemplate.exchange(flaskUrl, HttpMethod.POST, entity, Map::class.java)
        if(response.statusCode.equals(404)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("입력된 키워드가 없습니다. 다시 입력해주세요.");
        }
        val flaskResponse = response.body as? Map<String, Any>

        println(flaskResponse)
        // Extract the nested "extracted_features" map
        val extractedFeatures = flaskResponse?.get("extracted_features") as? Map<String, String>


        val duration = extractedFeatures?.get("duration")
        val riskLevel = extractedFeatures?.get("risk_level")
        val type = extractedFeatures?.get("type")

        var recommendations = chatBotService.getRecommendations(inputText, riskLevel, duration, type)

        return ResponseEntity.status(HttpStatus.OK).body(recommendations);
    }

}