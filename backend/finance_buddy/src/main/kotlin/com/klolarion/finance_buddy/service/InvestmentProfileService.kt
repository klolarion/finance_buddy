package com.klolarion.finance_buddy.service

import com.klolarion.finance_buddy.entity.InvestmentProfile
import com.klolarion.finance_buddy.repository.InvestmentProfileRepository
import org.springframework.stereotype.Service

@Service
class InvestmentProfileService(private val profileRepository: InvestmentProfileRepository) {

    fun saveProfile(profile: InvestmentProfile): InvestmentProfile {
        return profileRepository.save(profile)
    }

    fun getProfileById(id: Long): InvestmentProfile? {
        return profileRepository.findById(id).orElse(null)
    }
}