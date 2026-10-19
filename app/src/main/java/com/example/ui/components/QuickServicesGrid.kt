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
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
fun QuickServicesGrid(
    onTransferMoneyClick: () -> Unit,
    onMobileRechargeClick: () -> Unit,
    onElectricityBillClick: () -> Unit,
    onToBankClick: () -> Unit,
    onWalletClick: () -> Unit,
    onLoanRepaymentClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp))
            .testTag("dashboard_quick_services_grid")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quick Services",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = "UPI & Bills",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFC084FC)
                )
            }

            // Row 1: The 3 core requested services: 'Transfer Money', 'Mobile Recharge', 'Electricity Bill'
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ServiceGridItem(
                    title = "Transfer Money",
                    icon = Icons.Default.Send,
                    containerColor = Color(0xFF581C87), // Deep purple
                    iconColor = Color(0xFFE9D5FF),
                    badge = "Instant",
                    testTag = "service_transfer_money",
                    onClick = onTransferMoneyClick,
                    modifier = Modifier.weight(1f)
                )

                ServiceGridItem(
                    title = "Mobile Recharge",
                    icon = Icons.Default.PhoneAndroid,
                    containerColor = Color(0xFF1E3A8A), // Navy blue
                    iconColor = Color(0xFF93C5FD),
                    badge = null,
                    testTag = "service_mobile_recharge",
                    onClick = onMobileRechargeClick,
                    modifier = Modifier.weight(1f)
                )

                ServiceGridItem(
                    title = "Electricity Bill",
                    icon = Icons.Default.ElectricBolt,
                    containerColor = Color(0xFF7C2D12), // Deep orange
                    iconColor = Color(0xFFFDBA74),
                    badge = "BBPS",
                    testTag = "service_electricity_bill",
                    onClick = onElectricityBillClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Row 2: Complementary banking & wallet services
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ServiceGridItem(
                    title = "To Bank / Self",
                    icon = Icons.Default.AccountBalance,
                    containerColor = Color(0xFF27272A),
                    iconColor = Color(0xFFC4B5FD),
                    badge = null,
                    testTag = "service_to_bank",
                    onClick = onToBankClick,
                    modifier = Modifier.weight(1f)
                )

                ServiceGridItem(
                    title = "PhonePe Wallet",
                    icon = Icons.Default.AccountBalanceWallet,
                    containerColor = Color(0xFF27272A),
                    iconColor = AccentGold,
                    badge = "Cashback",
                    testTag = "service_wallet",
                    onClick = onWalletClick,
                    modifier = Modifier.weight(1f)
                )

                ServiceGridItem(
                    title = "Loan Repayment",
                    icon = Icons.Default.Payment,
                    containerColor = Color(0xFF27272A),
                    iconColor = Color(0xFFFDE047),
                    badge = null,
                    testTag = "service_loan_repayment",
                    onClick = onLoanRepaymentClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ServiceGridItem(
    title: String,
    icon: ImageVector,
    containerColor: Color,
    iconColor: Color,
    badge: String?,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 2.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(54.dp),
            contentAlignment = Alignment.Center
        ) {
            // Circle container
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(containerColor)
                    .border(1.dp, iconColor.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Top-right badge
            if (badge != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-2).dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (badge == "Cashback") AccentGold else AccentGreen)
                        .padding(horizontal = 5.dp, vertical = 1.5.dp)
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
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFE5E7EB),
            textAlign = TextAlign.Center,
            lineHeight = 13.sp,
            maxLines = 2,
            modifier = Modifier.heightIn(min = 28.dp)
        )
    }
}
