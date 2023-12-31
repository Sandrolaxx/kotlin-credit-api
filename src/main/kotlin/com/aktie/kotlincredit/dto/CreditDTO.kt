package com.aktie.kotlincredit.dto

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDTO(

    @field:NotNull(message = "Credit value cannot be null!")
    val creditValue: BigDecimal,

    @field:Future(message = "Day first installment cannot be on the past or today!")
    val dayFirstInstallment: LocalDate,

    @field:NotNull(message = "Number of installments cannot be null!")
    @field:Max(value = 48, message = "Number of installments cannot be greater than 48!")
    val numberOfInstallments: Int,

    @field:NotNull(message = "Customer id cannot be null!")
    val customerId: Long
)