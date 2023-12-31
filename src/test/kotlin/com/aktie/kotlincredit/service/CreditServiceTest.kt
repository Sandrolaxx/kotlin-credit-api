package com.aktie.kotlincredit.service

import com.aktie.kotlincredit.entity.Address
import com.aktie.kotlincredit.entity.Credit
import com.aktie.kotlincredit.entity.Customer
import com.aktie.kotlincredit.exception.BusinessException
import com.aktie.kotlincredit.impl.CreditService
import com.aktie.kotlincredit.impl.CustomerService
import com.aktie.kotlincredit.repositories.ICreditRepository
import com.aktie.kotlincredit.repositories.ICustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import kotlin.random.Random

@ExtendWith(MockKExtension::class)
class CreditServiceTest(
    @MockK
    val creditRepository: ICreditRepository,

    @MockK
    val customerRepository: ICustomerRepository
) {
    @InjectMockKs
    var customerService = CustomerService(customerRepository)

    @InjectMockKs
    var creditService = CreditService(creditRepository, customerService)

    @Test
    fun shouldCreateCredit() {
        //given
        val fakeId: Long = Random.nextLong()

        setupTests(fakeId)

        val fakeCredit = buildCredit(fakeId)

        every { creditRepository.save(any()) } returns fakeCredit

        //when
        val persistedCredit = this.creditService.save(fakeCredit)

        //then
        Assertions.assertThat(persistedCredit).isNotNull
        Assertions.assertThat(persistedCredit).isSameAs(fakeCredit)
        verify(exactly = 1) { creditRepository.save(fakeCredit) }
    }

    @Test
    fun shouldThrowBusinessExceptionTryCreateCreditWithInvalidCustomer() {
        //given
        val fakeId: Long = Random.nextLong()
        val fakeCredit = buildCredit(fakeId)

        every { customerRepository.findById(fakeId) } returns Optional.empty()

        //when
        //then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.save(fakeCredit) }
            .withMessage("Id $fakeId not found")
        verify(exactly = 1) { customerRepository.findById(fakeId) }
    }

    @Test
    fun shouldFindCreditByCustomer() {
        //given
        val fakeId: Long = Random.nextLong()

        setupTests(fakeId)

        val fakeCredit = buildCredit(fakeId)
        val expectedList = listOf(fakeCredit)

        every { creditRepository.findAllByCustomerId(fakeId) } returns expectedList

        //when
        val foundCredits = creditService.findAllByCustomerId(fakeId)

        //then
        Assertions.assertThat(foundCredits).isNotEmpty
        Assertions.assertThat(foundCredits).isSameAs(expectedList)
        verify(exactly = 1) { creditRepository.findAllByCustomerId(fakeId) }
    }

    @Test
    fun shouldFindCreditByCreditCode() {
        //given
        val fakeId: Long = Random.nextLong()

        val fakeCredit = buildCredit(fakeId)

        every { creditRepository.findByCreditCode(fakeCredit.creditCode) } returns fakeCredit

        //when
        val foundCredit = creditService.findByCreditCode(fakeId, fakeCredit.creditCode)

        //then
        Assertions.assertThat(foundCredit).isNotNull
        Assertions.assertThat(foundCredit).isSameAs(fakeCredit)
        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCredit.creditCode) }
    }

    @Test
    fun shouldThrowBusinessExceptionTryFindInvalidCredit() {
        //given
        val fakeId: Long = Random.nextLong()
        val fakeCredit = buildCredit(fakeId)

        every { creditRepository.findByCreditCode(fakeCredit.creditCode) } returns null

        //when
        //then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.findByCreditCode(fakeId, fakeCredit.creditCode) }
            .withMessage("Crédito não encontrado")
        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCredit.creditCode) }
    }

    @Test
    fun shouldThrowBusinessExceptionTryFindInvalidCustomerCredit() {
        //given
        val fakeId: Long = Random.nextLong()
        val invalidFakeCustomerId: Long = Random.nextLong()
        val fakeCredit = buildCredit(fakeId)

        every { creditRepository.findByCreditCode(fakeCredit.creditCode) } returns fakeCredit

        //when
        //then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.findByCreditCode(invalidFakeCustomerId, fakeCredit.creditCode) }
            .withMessage("Não é possível acessar essa informação")
        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCredit.creditCode) }
    }

    private fun setupTests(fakeCustomerId: Long) {
        val fakeCustomer: Customer = buildCustomer(fakeCustomerId)

        every { customerRepository.save(fakeCustomer) } returns fakeCustomer
        every { customerRepository.findById(fakeCustomerId) } returns Optional.of(fakeCustomer)
    }

    private fun buildCredit(fakeCustomerId: Long): Credit {
        return Credit(BigDecimal.valueOf(2000.0), LocalDate.now(), 4, Customer(fakeCustomerId))
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