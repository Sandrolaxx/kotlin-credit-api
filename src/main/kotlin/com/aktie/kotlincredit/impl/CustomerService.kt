package com.aktie.kotlincredit.impl

import com.aktie.kotlincredit.entity.Customer
import com.aktie.kotlincredit.exception.BusinessException
import com.aktie.kotlincredit.repositories.ICustomerRepository
import com.aktie.kotlincredit.service.ICustomerService
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepo: ICustomerRepository
) : ICustomerService {

    override fun save(customer: Customer): Customer {
        return customerRepo.save(customer);
    }

    override fun findById(id: Long): Customer {
        return customerRepo.findById(id).orElseThrow {
            throw BusinessException("Id $id not found")
        }
    }

    override fun delete(id: Long) {
        val customer: Customer = this.findById(id)

        customerRepo.delete(customer)
    }

}