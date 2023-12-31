package com.aktie.kotlincredit.controllers

import com.aktie.kotlincredit.dto.CustomerDTO
import com.aktie.kotlincredit.dto.CustomerEditDTO
import com.aktie.kotlincredit.dto.CustomerViewDTO
import com.aktie.kotlincredit.impl.CustomerService
import com.aktie.kotlincredit.mappers.CustomerMapper
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/customer")
class CustomerController(
    private val customerService: CustomerService
) {

    @PostMapping()
    fun register(@RequestBody @Valid customerDTO: CustomerDTO): ResponseEntity<CustomerViewDTO> {
        val customerEntity = CustomerMapper.toEntity(customerDTO)
        val savedCustomer = customerService.save(customerEntity)

        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerMapper.toViewDTO(savedCustomer))
    }

    @GetMapping
    fun find(@RequestHeader id: Long): ResponseEntity<CustomerViewDTO> {
        val customerEntity = customerService.findById(id)

        return ResponseEntity.ok(CustomerMapper.toViewDTO(customerEntity))
    }

    @PatchMapping
    fun update(
        @RequestHeader id: Long,
        @RequestBody @Valid customerDTO: CustomerEditDTO
    ): ResponseEntity<CustomerViewDTO> {
        val customerEntity = customerService.findById(id)

        customerService.save(CustomerMapper.toEntity(customerEntity, customerDTO))

        return ResponseEntity.ok(CustomerMapper.toViewDTO(customerEntity))
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@RequestHeader id: Long) = customerService.delete(id)
}