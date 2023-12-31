package com.aktie.kotlincredit.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDTO(
    @field:NotEmpty(message = "First name cannot be empty!")
    val firstName: String,

    @field:NotEmpty(message = "First name cannot be empty!")
    val lastName: String,

    @field:CPF(message = "Invalid CPF")
    @field:NotEmpty(message = "CPF cannot be empty!")
    val cpf: String,

    @field:NotNull(message = "Income cannot be null!")
    val income: BigDecimal,

    @field:Email(message = "Invalid e-mail!")
    @field:NotEmpty(message = "E-mail cannot be empty!")
    val email: String,

    @field:NotEmpty(message = "Password cannot be empty!")
    val password: String,

    @field:NotEmpty(message = "ZipCode cannot be empty!")
    val zipCode: String,

    @field:NotEmpty(message = "Street cannot be empty!")
    val street: String
)