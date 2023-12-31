package com.aktie.kotlincredit.service

import com.aktie.kotlincredit.entity.Customer

interface ICustomerService {

    fun save(customer: Customer): Customer

    fun findById(id: Long): Customer

    fun delete(id: Long)

}