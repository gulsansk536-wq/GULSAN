package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Transaction
import com.example.model.TransactionCategory
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RecentTransactionsCard(
    transactions: List<Transaction>,
    onTransactionClick: (Transaction) -> Unit,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier,
    maxItems: Int = 4
) {
    val displayList = transactions.take(maxItems)
    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp))
            .testTag("dashboard_recent_transactions_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Recent Transactions",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF381559))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Room DB",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD8B4FE)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onViewAllClick() }
                        .padding(4.dp)
                        .testTag("btn_view_all_transactions")
                ) {
                    Text(
                        text = "View All",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFC084FC)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "View all transactions",
                        tint = Color(0xFFC084FC),
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            if (displayList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No recent transactions found",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    displayList.forEachIndexed { index, tx ->
                        RecentTransactionRow(
                            transaction = tx,
                            dateStr = dateFormat.format(Date(tx.timestamp)),
                            onClick = { onTransactionClick(tx) }
                        )

                        if (index < displayList.lastIndex) {
                            Divider(
                                color = SurfaceBorder.copy(alpha = 0.6f),
                                thickness = 0.8.dp,
                                modifier = Modifier.padding(start = 50.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentTransactionRow(
    transaction: Transaction,
    dateStr: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(vertical = 6.dp)
            .testTag("recent_tx_row_${transaction.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Category Icon Circle
        Box(
            modifier = Modifier
                .size(40.dp)
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
                modifier = Modifier.size(19.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Title and Subtitle
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                maxLines = 1
            )
            Text(
                text = "$dateStr • ${transaction.bankName.take(18)}",
                fontSize = 11.sp,
                color = TextMuted,
                maxLines = 1
            )
        }

        // Amount & Status
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${if (transaction.isDebit) "-" else "+"}₹${"%,.2f".format(transaction.amount)}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (transaction.isDebit) Color.White else AccentGreenLight
            )
            Text(
                text = if (transaction.isDebit) "Debited" else "Credited",
                fontSize = 10.sp,
                color = if (transaction.isDebit) TextMuted else AccentGreenLight
            )
        }
    }
}
