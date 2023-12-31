package com.aktie.kotlincredit.controllers

import com.aktie.kotlincredit.dto.CreditDTO
import com.aktie.kotlincredit.dto.CreditResumeViewDTO
import com.aktie.kotlincredit.dto.CreditViewDTO
import com.aktie.kotlincredit.impl.CreditService
import com.aktie.kotlincredit.mappers.CreditMapper
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/credit")
class CreditController(
    private val creditService: CreditService,
) {

    @PostMapping()
    fun create(@RequestBody @Valid creditDTO: CreditDTO): ResponseEntity<CreditViewDTO> {
        val credit = CreditMapper.toEntity(creditDTO)

        val savedCredit = creditService.save(credit)

        return ResponseEntity.status(HttpStatus.CREATED).body(CreditMapper.toViewDTO(savedCredit))
    }

    @GetMapping
    fun findByCode(@RequestHeader customerId: Long, @RequestHeader creditCode: UUID): ResponseEntity<CreditViewDTO> {
        val credit = creditService.findByCreditCode(customerId, creditCode)

        return ResponseEntity.ok(CreditMapper.toViewDTO(credit))
    }

    @GetMapping("/all")
    fun findAllByCustomerId(@RequestHeader customerId: Long): ResponseEntity<List<CreditResumeViewDTO>> {
        val allCredits = creditService.findAllByCustomerId(customerId)
        val allCreditsDTO = allCredits.stream()
            .map(CreditMapper::toResumeViewDTO)
            .toList()

        return ResponseEntity.ok(allCreditsDTO)
    }

}