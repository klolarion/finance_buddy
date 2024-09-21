package com.klolarion.finance_buddy.service

import com.klolarion.finance_buddy.entity.Member
import com.klolarion.finance_buddy.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(private val memberRepository: MemberRepository) {

    fun register(member: Member): Member {
        // 사용자 등록 로직
        return memberRepository.save(member)
    }

    fun findUserByEmail(email: String): Member? {
        return memberRepository.findByEmail(email)
    }
}