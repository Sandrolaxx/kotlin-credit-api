package com.aktie.kotlincredit.repository

import com.aktie.kotlincredit.entity.Address
import com.aktie.kotlincredit.entity.Credit
import com.aktie.kotlincredit.entity.Customer
import com.aktie.kotlincredit.repositories.ICreditRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditRepositoryTest {

    @Autowired
    lateinit var creditRepository: ICreditRepository

    @Autowired
    lateinit var testEntityManager: TestEntityManager

    private lateinit var customer: Customer
    private lateinit var creditOne: Credit
    private lateinit var creditTwo: Credit

    @BeforeEach
    fun setup() {
        customer = testEntityManager.persist(buildCustomer())
        creditOne = testEntityManager.persist(buildCredit(customer))
        creditTwo = testEntityManager.persist(buildCredit(customer))
    }

    @Test
    fun shouldFindCreditByCode() {
        //given
        val creditCode = creditOne.creditCode

        //when
        val fakeCreditOne: Credit = creditRepository.findByCreditCode(creditCode)!!

        //then
        Assertions.assertThat(fakeCreditOne).isNotNull
        Assertions.assertThat(fakeCreditOne).isSameAs(creditOne)
    }

    @Test
    fun shouldFindAllByCustomerId() {
        //given
        val fakeCustomerId = customer.id

        //when
        val fakeCredits: List<Credit> = creditRepository.findAllByCustomerId(fakeCustomerId!!)

        //then
        Assertions.assertThat(fakeCredits).isNotEmpty
        Assertions.assertThat(fakeCredits).size().isSameAs(2)
        Assertions.assertThat(fakeCredits).contains(creditOne, creditTwo)
    }

    private fun buildCredit(customer: Customer): Credit {
        return Credit(BigDecimal.valueOf(2000.0), LocalDate.now(), 4, customer)
    }

    private fun buildCustomer(): Customer {
        val address = Address("0293840", "Rua dos testes")
        val customer = Customer(cpf = "61363327011", address = address);

        customer.firstName = "Melon"
        customer.lastName = "Musk"
        customer.email = "musk@foguetes.com"
        customer.password = "teslaboy"
        customer.income = BigDecimal.valueOf(6700.0)

        return customer
    }

}