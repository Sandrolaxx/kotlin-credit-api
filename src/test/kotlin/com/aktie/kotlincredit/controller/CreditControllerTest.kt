package com.aktie.kotlincredit.controller

import com.aktie.kotlincredit.dto.CreditDTO
import com.aktie.kotlincredit.dto.CustomerDTO
import com.aktie.kotlincredit.entity.Customer
import com.aktie.kotlincredit.mappers.CreditMapper
import com.aktie.kotlincredit.mappers.CustomerMapper
import com.aktie.kotlincredit.repositories.ICreditRepository
import com.aktie.kotlincredit.repositories.ICustomerRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditControllerTest {

    @Autowired
    private lateinit var customerRepository: ICustomerRepository

    @Autowired
    private lateinit var creditRepository: ICreditRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/v1/credit"
        const val INVALID_CUSTOMER_ID: Long = 47
    }

    @BeforeEach
    fun setup() {
        customerRepository.deleteAll()
        creditRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        customerRepository.deleteAll()
        creditRepository.deleteAll()
    }

    @Test
    fun shouldCreateCreditReturnOk() {
        //given
        val customerDTO: CustomerDTO = builderCustomerDTO()
        val savedCustomer = customerRepository.save(CustomerMapper.toEntity(customerDTO))

        val creditDTO = builderCreditDTO(savedCustomer.id!!)
        val valueAsString: String = objectMapper.writeValueAsString(creditDTO)

        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value(creditDTO.creditValue))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberInstallments").value(creditDTO.numberOfInstallments))
    }

    @Test
    fun shouldNotCreateCreditWithIncorrectDayFirstInstallmentDateReturnBadRequest() {
        //given
        val creditDTO = builderCreditDTO(1, LocalDate.now())
        val valueAsString: String = objectMapper.writeValueAsString(creditDTO)

        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad Request! Check the request fields"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
    }

    @Test
    fun shouldFindCreditByCustomerIdAndCreditCodeReturnOk() {
        //given
        val customerDTO: CustomerDTO = builderCustomerDTO()
        val savedCustomer: Customer = customerRepository.save(CustomerMapper.toEntity(customerDTO))

        val savedCredit = creditRepository.save(CreditMapper.toEntity(builderCreditDTO(savedCustomer.id!!)))

        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get(URL)
                .header("customerId", savedCustomer.id)
                .header("creditCode", savedCredit.creditCode)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.creditValue").value("44000.0"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.numberInstallments").value("12"))
    }

    @Test
    fun shouldNotFindCreditWithIncorrectCreditCodeReturnBadRequest() {
        //given
        val customerDTO: CustomerDTO = builderCustomerDTO()
        val savedCustomer: Customer = customerRepository.save(CustomerMapper.toEntity(customerDTO))

        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get(URL)
                .header("customerId", savedCustomer.id)
                .header("creditCode", UUID.randomUUID().toString())
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad Request! Check the request fields"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
    }

    @Test
    fun shouldFindAllCreditsByCustomerIdReturnOk() {
        //given
        val customerDTO: CustomerDTO = builderCustomerDTO()
        val savedCustomer: Customer = customerRepository.save(CustomerMapper.toEntity(customerDTO))

        creditRepository.save(CreditMapper.toEntity(builderCreditDTO(savedCustomer.id!!)))

        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/all").header("customerId", savedCustomer.id)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0]").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].creditValue").value("44000.0"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].numberInstallments").value("12"))
    }

    @Test
    fun shouldNotFindAllCreditsWithIncorrectCustomerIDReturnOkWithEmptyArray() {
        //given
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/all").header("customerId", INVALID_CUSTOMER_ID)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    // Mocks ======================================================

    private fun builderCreditDTO(
        customerId: Long,
        dayFirstInstallment: LocalDate = LocalDate.now().plusDays(7)
    ): CreditDTO {
        return CreditDTO(
            BigDecimal.valueOf(44000),
            dayFirstInstallment,
            12,
            customerId
        )
    }

    private fun builderCustomerDTO(firstName: String = "Sandrolax"): CustomerDTO {
        return CustomerDTO(
            firstName,
            "Remax",
            "05752069009",
            BigDecimal.valueOf(4044.50),
            "sandrolax@gmail.com",
            "123213",
            "85806137",
            "Rua dos testes, 47"
        )
    }

}