package com.klolarion.finance_buddy.repository

import com.klolarion.finance_buddy.entity.PreferredAsset
import org.springframework.data.jpa.repository.JpaRepository

interface PreferredAssetRepository : JpaRepository<PreferredAsset, Long> {
}