package com.klolarion.finance_buddy.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/index")
class IndexController {

    @GetMapping
    fun getProfileById(): Any {
        return ResponseEntity.ok();
    }

}