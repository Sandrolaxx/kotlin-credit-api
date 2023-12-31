package com.aktie.kotlincredit.controller

import com.aktie.kotlincredit.dto.CustomerDTO
import com.aktie.kotlincredit.dto.CustomerEditDTO
import com.aktie.kotlincredit.entity.Customer
import com.aktie.kotlincredit.mappers.CustomerMapper
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

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CustomerControllerTest {

    @Autowired
    private lateinit var customerRepository: ICustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/v1/customer"
        const val INVALID_CUSTOMER_ID: Long = 47
    }

    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @AfterEach
    fun tearDown() = customerRepository.deleteAll()

    @Test
    fun shouldCreateCustomerReturnOk() {
        //given
        val customerDTO: CustomerDTO = builderCustomerDTO()
        val valueAsString: String = objectMapper.writeValueAsString(customerDTO)

        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Sandrolax"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("05752069009"))

    }

    @Test
    fun shouldNotSavedCustomerWithSameCPFReturnConflict() {
        //given
        val customerDTO: CustomerDTO = builderCustomerDTO()
        val valueAsString: String = objectMapper.writeValueAsString(customerDTO)

        customerRepository.save(CustomerMapper.toEntity(customerDTO))

        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Conflict! Problems in DataBase."))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
    }

    @Test
    fun shouldNotSavedCustomerWithEmptyFirstNameReturnBadRequest() {
        //given
        val customerDTO: CustomerDTO = builderCustomerDTO("")
        val valueAsString: String = objectMapper.writeValueAsString(customerDTO)

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
    fun shouldFindCustomerByIdReturnOk() {
        //given
        val customerDTO: CustomerDTO = builderCustomerDTO()
        val savedCustomer: Customer = customerRepository.save(CustomerMapper.toEntity(customerDTO))

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get(URL).header("id", savedCustomer.id))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Sandrolax"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value("05752069009"))
    }

    @Test
    fun shouldNotFindCustomerWithIncorrectIdReturnBadRequest() {
        //given
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.get(URL).header("id", INVALID_CUSTOMER_ID))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad Request! Check the request fields"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
    }

    @Test
    fun shouldDeleteCustomerByIdReturnNoContent() {
        //given
        val customerDTO: CustomerDTO = builderCustomerDTO()
        val savedCustomer: Customer = customerRepository.save(CustomerMapper.toEntity(customerDTO))

        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.delete(URL).header("id", savedCustomer.id))
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    fun shouldNotDeleteCustomerWithIncorrectIdReturnBadRequest() {
        //given
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.delete(URL).header("id", INVALID_CUSTOMER_ID))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad Request! Check the request fields"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
    }

    @Test
    fun shouldUpdateCustomerReturnOk() {
        //given
        val customerDTO: CustomerDTO = builderCustomerDTO()
        val savedCustomer: Customer = customerRepository.save(CustomerMapper.toEntity(customerDTO))

        val customerEditDTO: CustomerEditDTO = builderCustomerEditDTO()
        val valueAsString: String = objectMapper.writeValueAsString(customerEditDTO)

        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.patch(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header("id", savedCustomer.id)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Melon"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Musk"))
    }

    @Test
    fun shouldNotUpdateCustomerWithIncorrectIdReturnBadRequest() {
        //given
        val customerEditDTO: CustomerEditDTO = builderCustomerEditDTO()
        val valueAsString: String = objectMapper.writeValueAsString(customerEditDTO)
        //when
        //then
        mockMvc.perform(
            MockMvcRequestBuilders.patch(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header("id", INVALID_CUSTOMER_ID)
                .content(valueAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad Request! Check the request fields"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
    }

    // Mocks ======================================================

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

    private fun builderCustomerEditDTO(): CustomerEditDTO {
        return CustomerEditDTO(
            "Melon",
            "Musk",
            BigDecimal.valueOf(4044.50),
            "85806137",
            "Rua dos testes, 47"
        )
    }
}