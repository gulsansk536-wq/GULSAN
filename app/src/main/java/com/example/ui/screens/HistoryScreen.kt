package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppState
import com.example.model.Transaction
import com.example.model.TransactionCategory
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    state: AppState,
    filter: String,
    onFilterChange: (String) -> Unit,
    onTransactionClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = listOf("ALL", "DEBIT", "CREDIT", "BILLS")
    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(16.dp)
    ) {
        Text(
            text = "Transaction History",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter chips row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { f ->
                val isSelected = f == filter
                val label = when (f) {
                    "ALL" -> "All Payments"
                    "DEBIT" -> "Money Sent"
                    "CREDIT" -> "Money Received"
                    else -> "Recharge & Bills"
                }
                FilterChip(
                    selected = isSelected,
                    onClick = { onFilterChange(f) },
                    label = { Text(label, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PhonePePurple,
                        selectedLabelColor = Color.White,
                        containerColor = SurfaceCard,
                        labelColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("filter_chip_$f")
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        val filteredTransactions = state.transactions.filter { tx ->
            when (filter) {
                "DEBIT" -> tx.isDebit
                "CREDIT" -> !tx.isDebit
                "BILLS" -> tx.category == TransactionCategory.BILL || tx.category == TransactionCategory.RECHARGE
                else -> true
            }
        }

        if (filteredTransactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No transactions found in this filter", color = TextMuted)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(filteredTransactions) { tx ->
                    TransactionListItem(
                        transaction = tx,
                        dateString = dateFormat.format(Date(tx.timestamp)),
                        onClick = { onTransactionClick(tx) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionListItem(
    transaction: Transaction,
    dateString: String,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("history_item_${transaction.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        when (transaction.category) {
                            TransactionCategory.TRANSFER -> if (transaction.isDebit) Color(0xFF4C1D95) else Color(0xFF064E3B)
                            TransactionCategory.BILL -> Color(0xFF1E3A8A)
                            TransactionCategory.RECHARGE -> Color(0xFF701A75)
                            else -> Color(0xFF78350F)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (transaction.category) {
                        TransactionCategory.TRANSFER -> if (transaction.isDebit) Icons.Default.CallMade else Icons.Default.CallReceived
                        TransactionCategory.BILL -> Icons.Default.ElectricBolt
                        TransactionCategory.RECHARGE -> Icons.Default.PhoneIphone
                        else -> Icons.Default.AccountBalanceWallet
                    },
                    contentDescription = null,
                    tint = if (!transaction.isDebit) AccentGreenLight else Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Text(
                    text = "$dateString • ${transaction.bankName}",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (transaction.isDebit) "-" else "+"}₹${"%,.2f".format(transaction.amount)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (transaction.isDebit) Color.White else AccentGreen
                )
                Text(
                    text = if (transaction.isDebit) "Debited" else "Credited",
                    fontSize = 10.sp,
                    color = if (transaction.isDebit) TextMuted else AccentGreen
                )
            }
        }
    }
}
