package com.aktie.kotlincredit.dto

import java.math.BigDecimal

data class CustomerViewDTO(
    val id: Long?,
    val firstName: String,
    val lastName: String,
    val cpf: String,
    val income: BigDecimal,
    val email: String,
    val zipCode: String,
    val street: String
)
