package net.corda.finance

import net.corda.core.contracts.Amount
import net.corda.core.internal.deleteRecursively
import net.corda.core.internal.div
import net.corda.testing.core.BOB_NAME
import net.corda.testing.node.internal.FINANCE_WORKFLOWS_CORDAPP
import net.corda.testing.node.internal.InternalMockNetwork
import net.corda.testing.node.internal.findCordapp
import org.junit.After
import org.junit.Test
import kotlin.test.assertEquals

class TestTest {
    private val mockNet = InternalMockNetwork(cordappsForAllNodes = listOf(FINANCE_WORKFLOWS_CORDAPP))

    @After
    fun cleanUp() {
        mockNet.stopNodes()
    }

//    @Test
//    fun restart() {
//        var bob = mockNet.createPartyNode(legalName = ALICE_NAME)
//        mockNet.runNetwork()
//        bob = mockNet.restartNode(bob)
//        mockNet.runNetwork()
//    }

    @Test
    fun `delete jar`() {
        val bob = mockNet.createPartyNode(legalName = BOB_NAME)
        mockNet.runNetwork()
        bob.dispose()
        mockNet.runNetwork()
        (mockNet.baseDirectory(bob) / "cordapps").deleteRecursively()
    }
}

class CurrenciesTests {
    @Test
    fun `basic currency`() {
        val expected = 1000L
        val amount = Amount(expected, GBP)
        assertEquals(expected, amount.quantity)
    }

    @Test
    fun parseCurrency() {
        assertEquals(Amount(1234L, GBP), Amount.parseCurrency("£12.34"))
        assertEquals(Amount(1200L, GBP), Amount.parseCurrency("£12"))
        assertEquals(Amount(1000L, USD), Amount.parseCurrency("$10"))
        assertEquals(Amount(5000L, JPY), Amount.parseCurrency("¥5000"))
        assertEquals(Amount(500000L, RUB), Amount.parseCurrency("₽5000"))
        assertEquals(Amount(1500000000L, CHF), Amount.parseCurrency("15,000,000 CHF"))
    }

    @Test
    fun rendering() {
        assertEquals("5000 JPY", Amount.parseCurrency("¥5000").toString())
        assertEquals("50.12 USD", Amount.parseCurrency("$50.12").toString())
    }
}