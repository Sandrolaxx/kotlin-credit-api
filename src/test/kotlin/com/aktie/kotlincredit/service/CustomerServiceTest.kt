package com.aktie.kotlincredit.service

import com.aktie.kotlincredit.entity.Address
import com.aktie.kotlincredit.entity.Customer
import com.aktie.kotlincredit.exception.BusinessException
import com.aktie.kotlincredit.impl.CustomerService
import com.aktie.kotlincredit.repositories.ICustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    @MockK
    lateinit var customerRepository: ICustomerRepository

    @InjectMockKs
    lateinit var customerService: CustomerService

    @Test
    fun shouldCreateCustomer() {
        //given
        val fakeCustomer = buildCustomer()
        every { customerRepository.save(any()) } returns fakeCustomer

        //when
        val persistedCustomer = customerService.save(fakeCustomer)

        //then
        Assertions.assertThat(persistedCustomer).isNotNull
        Assertions.assertThat(persistedCustomer).isSameAs(fakeCustomer)
        verify(exactly = 1) { customerRepository.save(fakeCustomer) }
    }

    @Test
    fun shouldFindByCustomer() {
        //given
        val fakeId: Long = Random.nextLong()
        val fakeCustomer: Customer = buildCustomer(fakeId)

        every { customerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)

        //when
        val foundCustomer = customerService.findById(fakeId)

        //then
        Assertions.assertThat(foundCustomer).isNotNull
        Assertions.assertThat(foundCustomer).isExactlyInstanceOf(Customer::class.java)
        Assertions.assertThat(foundCustomer).isSameAs(fakeCustomer)
        verify(exactly = 1) { customerRepository.findById(fakeId) }
    }

    @Test
    fun shouldThrowBusinessExceptionTryFindInvalidCustomer() {
        //given
        val fakeId: Long = Random.nextLong()

        every { customerRepository.findById(fakeId) } returns Optional.empty()

        //when
        //then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { customerService.findById(fakeId) }
            .withMessage("Id $fakeId not found")
        verify(exactly = 1) { customerRepository.findById(fakeId) }
    }

    @Test
    fun shouldDeleteCustomer() {
        //given
        val fakeId: Long = Random.nextLong()
        val fakeCustomer: Customer = buildCustomer(fakeId)

        every { customerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)
        every { customerRepository.delete(fakeCustomer) } just runs

        //when
        val foundCustomer = customerService.delete(fakeId)

        //then
        verify(exactly = 1) { customerRepository.findById(fakeId) }
        verify(exactly = 1) { customerRepository.delete(fakeCustomer) }
    }

    private fun buildCustomer(id: Long = 1L): Customer {
        val address = Address("0293840", "Rua dos testes")
        val customer = Customer(id, "61363327011", address);

        customer.firstName = "Melon"
        customer.lastName = "Musk"
        customer.email = "musk@foguetes.com"
        customer.password = "teslaboy"
        customer.income = BigDecimal.valueOf(6700.0)

        return customer
    }

}