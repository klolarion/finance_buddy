package com.klolarion.finance_buddy.controller

import com.klolarion.finance_buddy.entity.Member
import com.klolarion.finance_buddy.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/member")
class MemberController(private val memberService: MemberService) {

    @PostMapping("/register")
    fun register(@RequestBody member: Member) : ResponseEntity<Member> {
        val registered = memberService.register(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(registered);
    }

    @GetMapping("/{email}")
    fun getMember(@PathVariable email: String) : ResponseEntity<Member> {
        val member = memberService.findUserByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(member);
    }
}