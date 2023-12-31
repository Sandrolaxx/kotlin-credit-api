package com.aktie.kotlincredit.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class CustomerEditDTO(

    @field:NotEmpty(message = "First name cannot be empty!")
    val firstName: String?,

    @field:NotEmpty(message = "Last name cannot be empty!")
    val lastName: String?,

    @field:NotNull(message = "Income cannot be null!")
    val income: BigDecimal?,

    @field:NotEmpty(message = "ZipCode cannot be empty!")
    val zipCode: String?,

    @field:NotEmpty(message = "Street cannot be empty!")
    val street: String?
)
