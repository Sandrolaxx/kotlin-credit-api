package com.aktie.kotlincredit.mappers

import com.aktie.kotlincredit.dto.CreditDTO
import com.aktie.kotlincredit.dto.CreditResumeViewDTO
import com.aktie.kotlincredit.dto.CreditViewDTO
import com.aktie.kotlincredit.entity.Credit
import com.aktie.kotlincredit.entity.Customer

class CreditMapper {

    companion object {

        fun toEntity(dto: CreditDTO): Credit {
            return Credit(
                dto.creditValue,
                dto.dayFirstInstallment,
                dto.numberOfInstallments,
                Customer(id = dto.customerId)
            )
        }

        fun toResumeViewDTO(credit: Credit): CreditResumeViewDTO {
            return CreditResumeViewDTO(
                credit.creditCode,
                credit.creditValue,
                credit.numberOfInstallments
            )
        }

        fun toViewDTO(credit: Credit): CreditViewDTO {
            return CreditViewDTO(
                credit.creditCode,
                credit.creditValue,
                credit.numberOfInstallments,
                credit.status,
                credit.customer?.email,
                credit.customer?.income
            )
        }

    }

}