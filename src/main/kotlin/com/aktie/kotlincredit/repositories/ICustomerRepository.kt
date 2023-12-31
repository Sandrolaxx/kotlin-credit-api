package com.aktie.kotlincredit.repositories

import com.aktie.kotlincredit.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ICustomerRepository: JpaRepository<Customer, Long>