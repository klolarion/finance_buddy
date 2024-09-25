package com.klolarion.finance_buddy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class FinanceBuddyApplication

fun main(args: Array<String>) {
	runApplication<FinanceBuddyApplication>(*args)
}
