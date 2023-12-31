package com.aktie.kotlincredit.dto

import java.math.BigDecimal
import java.util.UUID

data class CreditResumeViewDTO (
    val creditCode: UUID,
    val creditValue: BigDecimal,
    val numberInstallments: Int,
)
