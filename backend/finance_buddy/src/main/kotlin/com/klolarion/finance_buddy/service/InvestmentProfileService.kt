package com.klolarion.finance_buddy.service

import com.klolarion.finance_buddy.entity.InvestmentProfile
import com.klolarion.finance_buddy.repository.InvestmentProfileRepository
import org.springframework.stereotype.Service

@Service
class InvestmentProfileService(private val profileRepository: InvestmentProfileRepository) {

    // 투자 프로필 저장 처리
    fun saveInvestmentProfile(profile: InvestmentProfile): InvestmentProfile {
        return profileRepository.save(profile)
    }

    // 투자 프로필 ID로 가져오기
    fun getInvestmentProfileById(id: Long): InvestmentProfile {
        return profileRepository.findById(id)
                .orElseThrow { IllegalArgumentException("Profile not found") }
    }

    // 투자 프로필 수정 처리
    fun updateInvestmentProfileById(id: Long, profile: InvestmentProfile): InvestmentProfile {
        val existingProfile = profileRepository.findById(id)
                .orElseThrow { IllegalArgumentException("Profile not found") }
        existingProfile.investmentAmount = profile.investmentAmount
        existingProfile.investmentPeriod = profile.investmentPeriod
        existingProfile.preferredProduct = profile.preferredProduct
        return profileRepository.save(existingProfile)
    }

}