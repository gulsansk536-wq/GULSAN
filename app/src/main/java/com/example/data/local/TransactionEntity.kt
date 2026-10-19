package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.Transaction
import com.example.model.TransactionCategory
import com.example.model.TransactionStatus

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: Double,
    val timestamp: Long,
    val isDebit: Boolean,
    val status: String = "SUCCESS",
    val category: String = "TRANSFER",
    val utr: String = "428901928371",
    val bankName: String = "State Bank of India - 4821",
    val note: String = ""
) {
    fun toDomainModel(): Transaction {
        return Transaction(
            id = id,
            title = title,
            subtitle = subtitle,
            amount = amount,
            timestamp = timestamp,
            isDebit = isDebit,
            status = try { TransactionStatus.valueOf(status) } catch (_: Exception) { TransactionStatus.SUCCESS },
            category = try { TransactionCategory.valueOf(category) } catch (_: Exception) { TransactionCategory.TRANSFER },
            utr = utr,
            bankName = bankName,
            note = note
        )
    }

    companion object {
        fun fromDomainModel(tx: Transaction): TransactionEntity {
            return TransactionEntity(
                id = tx.id,
                title = tx.title,
                subtitle = tx.subtitle,
                amount = tx.amount,
                timestamp = tx.timestamp,
                isDebit = tx.isDebit,
                status = tx.status.name,
                category = tx.category.name,
                utr = tx.utr,
                bankName = tx.bankName,
                note = tx.note
            )
        }
    }
}
