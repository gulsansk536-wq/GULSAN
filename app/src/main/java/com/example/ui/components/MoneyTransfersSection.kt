package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun MoneyTransfersSection(
    onToMobileClick: () -> Unit,
    onToBankClick: () -> Unit,
    onWalletClick: () -> Unit,
    onCheckBalanceClick: () -> Unit,
    onReferClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        // Section Header with Refer ➔ ₹50 badge
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Money Transfers",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE5E7EB)
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF431407))
                    .border(1.dp, Color(0xFF9A3412), RoundedCornerShape(12.dp))
                    .clickable { onReferClick() }
                    .padding(horizontal = 10.dp, vertical = 3.dp)
                    .testTag("refer_earn_badge")
            ) {
                Text(
                    text = "Refer ➔ ₹50",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFFB923C)
                )
            }
        }

        // 4-Column Grid inside card surface #242424
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(SurfaceCard)
                .padding(horizontal = 8.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TransferItem(
                icon = Icons.Default.PhoneAndroid,
                label = "To Mobile Number",
                badge = null,
                testTag = "btn_to_mobile",
                onClick = onToMobileClick,
                modifier = Modifier.weight(1f)
            )

            TransferItem(
                icon = Icons.Default.AccountBalance,
                label = "To Bank & Self A/c",
                badge = null,
                testTag = "btn_to_bank",
                onClick = onToBankClick,
                modifier = Modifier.weight(1f)
            )

            TransferItem(
                icon = Icons.Default.AccountBalanceWallet,
                label = "PhonePe Wallet",
                badge = "Cashback",
                testTag = "btn_wallet",
                onClick = onWalletClick,
                modifier = Modifier.weight(1f)
            )

            TransferItem(
                icon = Icons.Default.RequestQuote,
                label = "Check Balance",
                badge = null,
                testTag = "btn_check_balance",
                onClick = onCheckBalanceClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TransferItem(
    icon: ImageVector,
    label: String,
    badge: String?,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(horizontal = 2.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(52.dp),
            contentAlignment = Alignment.Center
        ) {
            // Circular Purple Container #581c87
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF581C87)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = Color(0xFFD8B4FE),
                    modifier = Modifier.size(24.dp)
                )
            }

            // Optional Top-Right Cashback badge
            if (badge != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 4.dp, y = (-2).dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(AccentGold)
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = badge,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xFFD1D5DB),
            textAlign = TextAlign.Center,
            lineHeight = 13.sp,
            maxLines = 2,
            modifier = Modifier.heightIn(min = 28.dp)
        )
    }
}
