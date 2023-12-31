package com.aktie.kotlincredit.impl

import com.aktie.kotlincredit.entity.Credit
import com.aktie.kotlincredit.exception.BusinessException
import com.aktie.kotlincredit.repositories.ICreditRepository
import com.aktie.kotlincredit.service.ICreditService
import com.aktie.kotlincredit.service.ICustomerService
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class CreditService(
    private val creditRepo: ICreditRepository,
    private val customerService: ICustomerService
) : ICreditService {
    override fun save(credit: Credit): Credit {
        if (credit.dayFirstInstallment.isAfter(LocalDate.now().plusMonths(3))) {
            throw BusinessException("Não é possível criar um empréstimo com a data primeira parcela superior três meses.")
        }

        credit.customer?.apply { customerService.findById(id!!) }

        return creditRepo.save(credit)
    }

    override fun findAllByCustomerId(customerId: Long): List<Credit> {
        return creditRepo.findAllByCustomerId(customerId)
    }

    override fun findByCreditCode(customerId: Long, creditCode: UUID): Credit {
        val credit = creditRepo.findByCreditCode(creditCode) ?: throw BusinessException("Crédito não encontrado")

        return if (credit.customer?.id == customerId) credit else throw BusinessException("Não é possível acessar essa informação")
    }
}