package com.aktie.kotlincredit.exception

data class BusinessException(override val message: String?): RuntimeException(message)