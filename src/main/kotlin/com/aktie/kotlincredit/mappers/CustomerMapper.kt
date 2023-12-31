package com.aktie.kotlincredit.mappers

import com.aktie.kotlincredit.dto.CustomerDTO
import com.aktie.kotlincredit.dto.CustomerEditDTO
import com.aktie.kotlincredit.dto.CustomerViewDTO
import com.aktie.kotlincredit.entity.Address
import com.aktie.kotlincredit.entity.Customer

class CustomerMapper {

    companion object {
        fun toEntity(dto: CustomerDTO): Customer {
            val address = Address(dto.zipCode, dto.street)
            val entity = Customer(cpf = dto.cpf, address = address)

            entity.lastName = dto.lastName
            entity.firstName = dto.firstName
            entity.email = dto.email
            entity.password = dto.password
            entity.income = dto.income

            return entity
        }

        fun toEntity(entity: Customer, customerDTO: CustomerEditDTO): Customer {
            customerDTO.firstName?.let { entity.firstName = it }
            customerDTO.lastName?.let { entity.lastName = it }
            customerDTO.income?.let { entity.income = it }
            customerDTO.zipCode?.let { entity.address.zipCode = it }
            customerDTO.street?.let { entity.address.street = it }

            return entity
        }

        fun toDTO(entity: Customer): CustomerDTO {
            return CustomerDTO(
                entity.firstName,
                entity.lastName,
                entity.cpf,
                entity.income,
                entity.email,
                entity.password,
                entity.address.zipCode,
                entity.address.street
            )
        }

        fun toViewDTO(entity: Customer): CustomerViewDTO {
            return CustomerViewDTO(
                entity.id,
                entity.firstName,
                entity.lastName,
                entity.cpf,
                entity.income,
                entity.email,
                entity.address.zipCode,
                entity.address.street
            )
        }
    }

}