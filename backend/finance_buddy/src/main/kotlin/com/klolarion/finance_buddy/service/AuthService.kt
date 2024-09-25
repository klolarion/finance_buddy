package com.klolarion.finance_buddy.service

import com.klolarion.finance_buddy.dto.SignupRequest
import com.klolarion.finance_buddy.entity.Member
import com.klolarion.finance_buddy.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class AuthService(
        private val memberRepository: MemberRepository
) {

    // 로그인
    fun login(account: String): Member {
        return memberRepository.findByAccount(account)
                ?: throw IllegalArgumentException("Account not found")
    }

    // 회원가입
    fun signup(signupRequest: SignupRequest) {
        val newUser = Member(
                account = signupRequest.account
        )
        memberRepository.save(newUser)
    }
}