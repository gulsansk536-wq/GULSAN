package com.example.data

import android.content.Context
import com.example.data.local.PhonePeDatabase
import com.example.data.local.TransactionEntity
import com.example.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

data class UserProfile(
    val name: String = "Gulsan Kumar",
    val phone: String = "+91 98765 43210",
    val upiId: String = "gulsan@ybl",
    val secondaryUpiId: String = "gulsan@axl",
    val addressTitle: String = "Home",
    val addressDetail: String = "Flat 402, Green Park Avenue, Bengaluru - 560034"
)

data class AppState(
    val userProfile: UserProfile = UserProfile(),
    val walletBalance: Double = 1250.00,
    val cashbackBalance: Double = 150.00,
    val bankAccounts: List<BankAccount> = listOf(
        BankAccount(
            id = "bank_sbi",
            bankName = "State Bank of India",
            accountNumberMasked = "•••• 4821",
            balance = 24850.50,
            isPrimary = true,
            ifsc = "SBIN0004821"
        ),
        BankAccount(
            id = "bank_hdfc",
            bankName = "HDFC Bank",
            accountNumberMasked = "•••• 1092",
            balance = 8320.00,
            isPrimary = false,
            ifsc = "HDFC0001092"
        )
    ),
    val contacts: List<Contact> = listOf(
        Contact("c1", "Rahul Sharma", "+91 98765 11223", "rahul.sharma@okaxis", "RS", 0xFF6366F1),
        Contact("c2", "Priya Patel", "+91 98112 23344", "priyapatel@ybl", "PP", 0xFFEC4899),
        Contact("c3", "Amit Verma", "+91 97654 32109", "amit.v@oksbi", "AV", 0xFF10B981),
        Contact("c4", "Vikram Singh", "+91 99887 76655", "vikram@paytm", "VS", 0xFFF59E0B),
        Contact("c5", "Neha Gupta", "+91 91234 56780", "neha.g@icici", "NG", 0xFF8B5CF6),
        Contact("c6", "Chai Point", "+91 90000 12345", "chaipoint@upi", "CP", 0xFFEF4444),
        Contact("c7", "Supermarket Fresh", "+91 90000 67890", "freshmart@upi", "SF", 0xFF14B8A6)
    ),
    val transactions: List<Transaction> = listOf(
        Transaction(
            id = "tx_101",
            title = "Paid to Chai Point",
            subtitle = "UPI: chaipoint@upi",
            amount = 40.0,
            timestamp = System.currentTimeMillis() - 3600000 * 2,
            isDebit = true,
            status = TransactionStatus.SUCCESS,
            category = TransactionCategory.TRANSFER,
            utr = "428901928371",
            bankName = "State Bank of India - 4821",
            note = "Ginger Tea & Samosa"
        ),
        Transaction(
            id = "tx_102",
            title = "Received from Priya Patel",
            subtitle = "UPI: priyapatel@ybl",
            amount = 500.0,
            timestamp = System.currentTimeMillis() - 3600000 * 18,
            isDebit = false,
            status = TransactionStatus.SUCCESS,
            category = TransactionCategory.TRANSFER,
            utr = "428892109284",
            bankName = "State Bank of India - 4821",
            note = "Dinner split"
        ),
        Transaction(
            id = "tx_103",
            title = "Electricity Bill - BESCOM",
            subtitle = "Consumer No: 89201928",
            amount = 1420.0,
            timestamp = System.currentTimeMillis() - 86400000 * 2,
            isDebit = true,
            status = TransactionStatus.SUCCESS,
            category = TransactionCategory.BILL,
            utr = "428781923019",
            bankName = "State Bank of India - 4821",
            note = "Electricity Bill Oct"
        ),
        Transaction(
            id = "tx_104",
            title = "Paid to Rahul Sharma",
            subtitle = "UPI: rahul.sharma@okaxis",
            amount = 1200.0,
            timestamp = System.currentTimeMillis() - 86400000 * 3,
            isDebit = true,
            status = TransactionStatus.SUCCESS,
            category = TransactionCategory.TRANSFER,
            utr = "428610293847",
            bankName = "HDFC Bank - 1092",
            note = "Movie tickets and snacks"
        ),
        Transaction(
            id = "tx_105",
            title = "Mobile Recharge 9876543210",
            subtitle = "Airtel 1.5GB/Day Plan",
            amount = 299.0,
            timestamp = System.currentTimeMillis() - 86400000 * 4,
            isDebit = true,
            status = TransactionStatus.SUCCESS,
            category = TransactionCategory.RECHARGE,
            utr = "428509182736",
            bankName = "PhonePe Wallet",
            note = "Prepaid recharge"
        ),
        Transaction(
            id = "tx_106",
            title = "Cashback from PhonePe",
            subtitle = "Daily RD Referral Bonus",
            amount = 50.0,
            timestamp = System.currentTimeMillis() - 86400000 * 6,
            isDebit = false,
            status = TransactionStatus.SUCCESS,
            category = TransactionCategory.WALLET,
            utr = "428391827461",
            bankName = "PhonePe Wallet",
            note = "Promo cashback"
        ),
        Transaction(
            id = "tx_107",
            title = "Rent via Credit Card",
            subtitle = "House Owner: S. Rao",
            amount = 18500.0,
            timestamp = System.currentTimeMillis() - 86400000 * 12,
            isDebit = true,
            status = TransactionStatus.SUCCESS,
            category = TransactionCategory.BILL,
            utr = "427901827364",
            bankName = "HDFC Bank - 1092",
            note = "October Rent"
        )
    ),
    val notifications: List<NotificationItem> = listOf(
        NotificationItem(
            id = "notif_1",
            title = "Cashback Credited! 🎉",
            message = "₹50 flat cashback added to your PhonePe wallet for your UPI transfer.",
            timeAgo = "1 hour ago",
            isRead = false,
            category = "Cashback"
        ),
        NotificationItem(
            id = "notif_2",
            title = "BESCOM Bill Due Reminder",
            message = "Electricity bill for Consumer #89201928 of ₹1,420 was paid successfully.",
            timeAgo = "2 days ago",
            isRead = false,
            category = "Bill Reminder"
        ),
        NotificationItem(
            id = "notif_3",
            title = "Start Daily RD at ₹100/day",
            message = "Turn daily spare change into ₹37,610/year with guaranteed returns. Withdraw anytime!",
            timeAgo = "3 days ago",
            isRead = true,
            category = "Investment"
        ),
        NotificationItem(
            id = "notif_4",
            title = "Security Tip",
            message = "PhonePe or bank representatives NEVER ask for your UPI PIN to transfer or receive funds.",
            timeAgo = "5 days ago",
            isRead = true,
            category = "Security"
        )
    ),
    val banners: List<PromoBanner> = listOf(
        PromoBanner(
            id = "b1",
            titlePrefix = "Turn ",
            titleHighlight = "₹100/day",
            subtitle = "into",
            amountHighlight = "₹37,610",
            amountSuffix = "/yr",
            ctaText = "Start Daily RD",
            badgeText = "Withdraw anytime",
            gradientStartHex = 0xFF2563EB,
            gradientEndHex = 0xFF312E81
        ),
        PromoBanner(
            id = "b2",
            titlePrefix = "Buy ",
            titleHighlight = "24K Gold",
            subtitle = "starting at",
            amountHighlight = "₹10",
            amountSuffix = " only",
            ctaText = "Buy Pure Gold",
            badgeText = "99.9% Purity Assured",
            gradientStartHex = 0xFFB45309,
            gradientEndHex = 0xFF78350F
        ),
        PromoBanner(
            id = "b3",
            titlePrefix = "Get ",
            titleHighlight = "₹5 Lakh Cover",
            subtitle = "at just",
            amountHighlight = "₹12",
            amountSuffix = "/day",
            ctaText = "View Health Plans",
            badgeText = "Instant Policy",
            gradientStartHex = 0xFF047857,
            gradientEndHex = 0xFF064E3B
        )
    )
)

object PhonePeRepository {
    private val _state = MutableStateFlow(AppState())
    val state: StateFlow<AppState> = _state.asStateFlow()

    private var database: PhonePeDatabase? = null
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun initDatabase(context: Context) {
        if (database != null) return
        val db = PhonePeDatabase.getDatabase(context)
        database = db

        coroutineScope.launch {
            val dao = db.transactionDao()
            // Check if seeded with dummy payment history
            if (dao.getCount() == 0) {
                val initialEntities = _state.value.transactions.map { TransactionEntity.fromDomainModel(it) }
                dao.insertAll(initialEntities)
            }

            // Observe reactive Room stream
            dao.getAllTransactions().collect { entities ->
                if (entities.isNotEmpty()) {
                    val domainList = entities.map { it.toDomainModel() }
                    _state.update { current -> current.copy(transactions = domainList) }
                }
            }
        }
    }

    fun sendMoney(
        recipientName: String,
        recipientUpiOrPhone: String,
        amount: Double,
        note: String,
        sourceBankName: String
    ): Transaction {
        val newTx = Transaction(
            id = "tx_${System.currentTimeMillis()}",
            title = "Paid to $recipientName",
            subtitle = if (recipientUpiOrPhone.contains("@")) "UPI: $recipientUpiOrPhone" else "Mobile: $recipientUpiOrPhone",
            amount = amount,
            timestamp = System.currentTimeMillis(),
            isDebit = true,
            status = TransactionStatus.SUCCESS,
            category = TransactionCategory.TRANSFER,
            utr = "42${Random.nextLong(1000000000L, 9999999999L)}",
            bankName = sourceBankName,
            note = note.ifBlank { "UPI Transfer" }
        )

        // Persist to Room
        coroutineScope.launch {
            database?.transactionDao()?.insertTransaction(TransactionEntity.fromDomainModel(newTx))
        }

        _state.update { current ->
            val updatedAccounts = current.bankAccounts.map { bank ->
                if (sourceBankName.contains(bank.bankName)) {
                    bank.copy(balance = (bank.balance - amount).coerceAtLeast(0.0))
                } else bank
            }
            val updatedWallet = if (sourceBankName.contains("Wallet")) {
                (current.walletBalance - amount).coerceAtLeast(0.0)
            } else current.walletBalance

            current.copy(
                bankAccounts = updatedAccounts,
                walletBalance = updatedWallet,
                transactions = listOf(newTx) + current.transactions.filter { it.id != newTx.id }
            )
        }

        return newTx
    }

    fun rechargeMobile(
        phoneNumber: String,
        operator: String,
        planAmount: Double
    ): Transaction {
        val newTx = Transaction(
            id = "tx_${System.currentTimeMillis()}",
            title = "Recharge of $phoneNumber",
            subtitle = "$operator Prepaid Plan",
            amount = planAmount,
            timestamp = System.currentTimeMillis(),
            isDebit = true,
            status = TransactionStatus.SUCCESS,
            category = TransactionCategory.RECHARGE,
            utr = "42${Random.nextLong(1000000000L, 9999999999L)}",
            bankName = "State Bank of India - 4821",
            note = "Mobile Recharge"
        )

        coroutineScope.launch {
            database?.transactionDao()?.insertTransaction(TransactionEntity.fromDomainModel(newTx))
        }

        _state.update { current ->
            val updatedAccounts = current.bankAccounts.map { bank ->
                if (bank.isPrimary) bank.copy(balance = (bank.balance - planAmount).coerceAtLeast(0.0)) else bank
            }
            current.copy(
                bankAccounts = updatedAccounts,
                transactions = listOf(newTx) + current.transactions.filter { it.id != newTx.id }
            )
        }

        return newTx
    }

    fun payBill(
        billerName: String,
        consumerNumber: String,
        amount: Double,
        categoryName: String
    ): Transaction {
        val newTx = Transaction(
            id = "tx_${System.currentTimeMillis()}",
            title = "$categoryName - $billerName",
            subtitle = "Consumer No: $consumerNumber",
            amount = amount,
            timestamp = System.currentTimeMillis(),
            isDebit = true,
            status = TransactionStatus.SUCCESS,
            category = TransactionCategory.BILL,
            utr = "42${Random.nextLong(1000000000L, 9999999999L)}",
            bankName = "State Bank of India - 4821",
            note = "$categoryName Bill"
        )

        coroutineScope.launch {
            database?.transactionDao()?.insertTransaction(TransactionEntity.fromDomainModel(newTx))
        }

        _state.update { current ->
            val updatedAccounts = current.bankAccounts.map { bank ->
                if (bank.isPrimary) bank.copy(balance = (bank.balance - amount).coerceAtLeast(0.0)) else bank
            }
            current.copy(
                bankAccounts = updatedAccounts,
                transactions = listOf(newTx) + current.transactions.filter { it.id != newTx.id }
            )
        }

        return newTx
    }

    fun addMoneyToWallet(amount: Double): Transaction {
        val newTx = Transaction(
            id = "tx_${System.currentTimeMillis()}",
            title = "PhonePe Wallet Top-up",
            subtitle = "Added from SBI •••• 4821",
            amount = amount,
            timestamp = System.currentTimeMillis(),
            isDebit = false,
            status = TransactionStatus.SUCCESS,
            category = TransactionCategory.WALLET,
            utr = "42${Random.nextLong(1000000000L, 9999999999L)}",
            bankName = "PhonePe Wallet",
            note = "Wallet Topup"
        )

        coroutineScope.launch {
            database?.transactionDao()?.insertTransaction(TransactionEntity.fromDomainModel(newTx))
        }

        _state.update { current ->
            val updatedAccounts = current.bankAccounts.map { bank ->
                if (bank.isPrimary) bank.copy(balance = (bank.balance - amount).coerceAtLeast(0.0)) else bank
            }
            current.copy(
                walletBalance = current.walletBalance + amount,
                bankAccounts = updatedAccounts,
                transactions = listOf(newTx) + current.transactions.filter { it.id != newTx.id }
            )
        }

        return newTx
    }

    fun updateAddress(title: String, detail: String) {
        _state.update { current ->
            current.copy(userProfile = current.userProfile.copy(addressTitle = title, addressDetail = detail))
        }
    }

    fun markNotificationsAsRead() {
        _state.update { current ->
            current.copy(notifications = current.notifications.map { it.copy(isRead = true) })
        }
    }
}
