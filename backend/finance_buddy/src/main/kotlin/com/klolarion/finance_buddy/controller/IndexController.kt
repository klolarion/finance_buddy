package com.klolarion.finance_buddy.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/index")
class IndexController {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getProfileById(): Any {
        return ResponseEntity.ok("인증 성공");
    }

}