package com.klolarion.finance_buddy.controller

import com.klolarion.finance_buddy.entity.InvestmentProfile
import com.klolarion.finance_buddy.service.InvestmentProfileService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/profiles")
class InvestmentProfileController(private val profileService: InvestmentProfileService) {

    @PostMapping
    fun saveProfile(@RequestBody profile: InvestmentProfile): ResponseEntity<InvestmentProfile> {
        val savedProfile = profileService.saveProfile(profile)
        return ResponseEntity.ok(savedProfile)
    }

    @GetMapping("/{id}")
    fun getProfileById(@PathVariable id: Long): ResponseEntity<InvestmentProfile> {
        val profile = profileService.getProfileById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(profile)
    }

    @PutMapping("/{id}")
    fun modifyProfileById(@PathVariable id: Long, @RequestBody profile: InvestmentProfile): ResponseEntity<InvestmentProfile> {
        val existingProfile = profileService.getProfileById(id) ?: return ResponseEntity.notFound().build()
        val updatedProfile = profileService.modifyProfileById(id, profile)
        return ResponseEntity.ok(updatedProfile)
    }
}