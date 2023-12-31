package com.aktie.kotlincredit.entity

import com.aktie.kotlincredit.enums.Status
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "KT_CREDIT")
class Credit {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false, unique = true)
    val creditCode: UUID = UUID.randomUUID()

    @Column(nullable = false)
    val creditValue: BigDecimal

    @Column(nullable = false)
    val dayFirstInstallment: LocalDate

    @Column(nullable = false)
    val numberOfInstallments: Int

    @Enumerated
    val status: Status

    @ManyToOne
    var customer: Customer?

    constructor(
        creditValue: BigDecimal = BigDecimal.ZERO,
        dayFirstInstallment: LocalDate, numberOfInstallments: Int = 0,
        customer: Customer? = null
    ) {
        this.creditValue = creditValue
        this.dayFirstInstallment = dayFirstInstallment
        this.numberOfInstallments = numberOfInstallments
        this.status = Status.IN_PROGRESS
        this.customer = customer
    }
}
