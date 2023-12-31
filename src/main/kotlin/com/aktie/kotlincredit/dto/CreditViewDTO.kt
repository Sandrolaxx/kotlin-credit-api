package com.aktie.kotlincredit.dto

import com.aktie.kotlincredit.enums.Status
import java.math.BigDecimal
import java.util.UUID

data class CreditViewDTO (
    val creditCode: UUID,
    val creditValue: BigDecimal,
    val numberInstallments: Int,
    val status: Status,
    val emailCustomer: String?,
    val incomeCustomer: BigDecimal?
)
