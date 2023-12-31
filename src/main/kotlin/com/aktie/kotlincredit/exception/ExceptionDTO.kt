package com.aktie.kotlincredit.exception

import java.time.LocalDateTime

data class ExceptionDTO(
    val message: String,
    val timestamp: LocalDateTime,
    val status: Int,
    val exception: String,
    val details: MutableMap<String, String?>
)