package com.example.model

enum class TransactionStatus {
    SUCCESS,
    PENDING,
    FAILED
}

enum class TransactionCategory {
    TRANSFER,
    RECHARGE,
    BILL,
    WALLET,
    INVESTMENT
}

data class Transaction(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: Double,
    val timestamp: Long,
    val isDebit: Boolean,
    val status: TransactionStatus = TransactionStatus.SUCCESS,
    val category: TransactionCategory = TransactionCategory.TRANSFER,
    val utr: String = "428901928371",
    val bankName: String = "State Bank of India - 4821",
    val note: String = ""
)

data class Contact(
    val id: String,
    val name: String,
    val phone: String,
    val upiId: String,
    val initials: String,
    val colorHex: Long
)

data class BankAccount(
    val id: String,
    val bankName: String,
    val accountNumberMasked: String,
    val balance: Double,
    val isPrimary: Boolean = false,
    val ifsc: String = "SBIN0001234"
)

data class BillBiller(
    val id: String,
    val name: String,
    val category: String, // Electricity, Rent, Loan, DTH, Water, Gas
    val stateOrRegion: String = "All India"
)

data class RechargePlan(
    val id: String,
    val amount: Int,
    val validity: String,
    val data: String,
    val description: String,
    val isPopular: Boolean = false
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timeAgo: String,
    val isRead: Boolean = false,
    val category: String = "Alert"
)

data class PromoBanner(
    val id: String,
    val titlePrefix: String,
    val titleHighlight: String,
    val subtitle: String,
    val amountHighlight: String,
    val amountSuffix: String,
    val ctaText: String,
    val badgeText: String,
    val gradientStartHex: Long,
    val gradientEndHex: Long
)
