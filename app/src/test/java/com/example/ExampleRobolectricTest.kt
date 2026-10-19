package com.example

import android.content.Context
import android.view.LayoutInflater
import android.webkit.WebView
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.PhonePeRepository
import com.example.data.local.PhonePeDatabase
import com.example.data.local.TransactionEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("PhonePe", appName)
  }

  @Test
  fun `test send money updates repository balance and adds transaction`() {
    val initialBalance = PhonePeRepository.state.value.bankAccounts.first { it.isPrimary }.balance
    val initialTxCount = PhonePeRepository.state.value.transactions.size

    val tx = PhonePeRepository.sendMoney(
        recipientName = "Priya Patel",
        recipientUpiOrPhone = "priyapatel@ybl",
        amount = 100.0,
        note = "Coffee",
        sourceBankName = "State Bank of India"
    )

    assertEquals("Paid to Priya Patel", tx.title)
    assertEquals(100.0, tx.amount, 0.01)
    assertEquals(initialTxCount + 1, PhonePeRepository.state.value.transactions.size)
    val updatedBalance = PhonePeRepository.state.value.bankAccounts.first { it.isPrimary }.balance
    assertEquals(initialBalance - 100.0, updatedBalance, 0.01)
  }

  @Test
  fun `test pay electricity bill and mobile recharge`() {
    val initialTxCount = PhonePeRepository.state.value.transactions.size
    val billTx = PhonePeRepository.payBill(
        billerName = "BESCOM",
        consumerNumber = "89201928",
        amount = 500.0,
        categoryName = "Electricity"
    )
    assertTrue(billTx.title.contains("Electricity"))
    assertEquals(500.0, billTx.amount, 0.01)
    assertEquals(initialTxCount + 1, PhonePeRepository.state.value.transactions.size)

    val rechargeTx = PhonePeRepository.rechargeMobile(
        phoneNumber = "9876543210",
        operator = "Airtel",
        planAmount = 299.0
    )
    assertTrue(rechargeTx.title.contains("9876543210"))
    assertEquals(299.0, rechargeTx.amount, 0.01)
  }

  @Test
  fun `test room database stores and retrieves payment transactions`() = runBlocking {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = Room.inMemoryDatabaseBuilder(context, PhonePeDatabase::class.java).build()
    val dao = db.transactionDao()

    val dummyTx = TransactionEntity(
        id = "test_1",
        title = "Paid to Test Merchant",
        subtitle = "UPI: test@upi",
        amount = 75.0,
        timestamp = System.currentTimeMillis(),
        isDebit = true,
        status = "SUCCESS",
        category = "TRANSFER",
        utr = "429999999999",
        bankName = "State Bank of India - 4821",
        note = "Test Note"
    )

    dao.insertTransaction(dummyTx)
    val count = dao.getCount()
    assertEquals(1, count)

    val list = dao.getRecentTransactions(5).first()
    assertEquals(1, list.size)
    assertEquals("Paid to Test Merchant", list[0].title)
    assertEquals(75.0, list[0].amount, 0.01)
    db.close()
  }

  @Test
  fun `test webView layout inflation and html asset availability`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val view = LayoutInflater.from(context).inflate(R.layout.activity_web_view, null, false)
    val webView = view.findViewById<WebView>(R.id.webView)
    assertNotNull(webView)

    val assetList = context.assets.list("")?.toList() ?: emptyList()
    assertTrue(assetList.contains("phonepe_web.html"))
  }
}
