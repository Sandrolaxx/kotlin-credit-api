package com.aktie.kotlincredit.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "KT_CUSTOMER")
class Customer {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var firstName: String = ""

    @Column(nullable = false)
    var lastName: String = ""

    @Column(nullable = false)
    val cpf: String

    @Column(nullable = false, unique = true)
    var email: String = ""

    @Column(nullable = false, unique = true)
    var income: BigDecimal = BigDecimal.ZERO

    @Column(nullable = false, unique = true)
    var password: String = ""

    @Embedded
    var address: Address

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL))
    var credits: List<Credit>

    constructor(id: Long? = null, cpf: String = "", address: Address = Address()) {
        this.id = id
        this.cpf = cpf
        this.address = address
        this.credits = mutableListOf()
    };
}
