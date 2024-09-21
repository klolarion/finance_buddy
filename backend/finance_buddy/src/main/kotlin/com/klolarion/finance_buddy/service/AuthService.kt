package com.klolarion.finance_buddy.service

import com.klolarion.finance_buddy.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class AuthService(
        private val memberRepository: MemberRepository
) {
    fun checkProvider(email: String): String? {
        return memberRepository.findByEmail(email)?.provider
    }

}