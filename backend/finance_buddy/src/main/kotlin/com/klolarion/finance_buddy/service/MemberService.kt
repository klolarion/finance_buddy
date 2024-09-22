package com.klolarion.finance_buddy.service

import com.klolarion.finance_buddy.dto.InvestmentRequest
import com.klolarion.finance_buddy.dto.LogoutRequest
import com.klolarion.finance_buddy.entity.Member
import com.klolarion.finance_buddy.repository.MemberRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
        private val memberRepository: MemberRepository
) {


    // 멤버 정보 가져오기
    fun findMember(id: Long): Member {
        return memberRepository.findById(id).orElseThrow { UsernameNotFoundException("Member not found") }
    }

    // 로그아웃 처리
    fun logout(logoutRequest: LogoutRequest) {
        // 토큰 무효화 처리 로직
        invalidateToken(logoutRequest.token)
    }

    // 예시 메서드: 토큰 무효화
    private fun invalidateToken(token: String) {
        // 토큰 무효화 처리 로직 (예: Redis 또는 DB에 저장된 토큰 제거)
    }

}