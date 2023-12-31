package com.aktie.kotlincredit.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class Address {

    @Column(nullable = false)
    var zipCode: String

    @Column(nullable = false)
    var street: String

    constructor(zipCode: String = "", street: String = "") {
        this.zipCode = zipCode
        this.street = street
    }
}
