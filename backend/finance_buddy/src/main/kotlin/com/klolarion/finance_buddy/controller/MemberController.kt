package com.klolarion.finance_buddy.controller

import com.klolarion.finance_buddy.entity.Member
import com.klolarion.finance_buddy.service.MemberService
import com.nimbusds.openid.connect.sdk.LogoutRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/member")
class MemberController(private val memberService: MemberService) {

    @PostMapping("/logout")
    fun logout(@RequestBody logoutRequest: LogoutRequest) {
        memberService.logout(logoutRequest)
    }

    @GetMapping("/{id}")
    fun getMember(@PathVariable id: Long) : ResponseEntity<Member> {
        val member = memberService.findMember(id);
        return ResponseEntity.status(HttpStatus.OK).body(member);
    }
}