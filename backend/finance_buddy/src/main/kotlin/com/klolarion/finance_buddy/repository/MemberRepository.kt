package com.klolarion.finance_buddy.repository

import com.klolarion.finance_buddy.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>{
    fun findByAccount(account: String): Member?
}