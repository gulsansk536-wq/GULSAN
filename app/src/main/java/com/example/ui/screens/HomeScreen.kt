package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppState
import com.example.model.Contact
import com.example.model.PromoBanner
import com.example.ui.ActiveModal
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    state: AppState,
    onOpenModal: (ActiveModal) -> Unit,
    onNavigateToAlerts: () -> Unit,
    onNavigateToHistory: () -> Unit = {},
    onQrScanClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val unreadCount = state.notifications.count { !it.isRead }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. Top Header Section with Profile Icon
        item {
            TopHeader(
                profile = state.userProfile,
                unreadAlertsCount = unreadCount,
                onAvatarClick = { onOpenModal(ActiveModal.MyQrCode) },
                onAddressClick = { onOpenModal(ActiveModal.AddressPicker) },
                onAlertsClick = onNavigateToAlerts,
                onHelpClick = { onOpenModal(ActiveModal.HelpSupport) },
                onQrScanClick = onQrScanClick,
                onWebClick = { onOpenModal(ActiveModal.WebPortal()) }
            )
        }

        // 2. Balance Display Card (Live Bank Balance with hide/show toggle)
        item {
            BalanceDisplayCard(
                state = state,
                onCheckBalanceClick = { onOpenModal(ActiveModal.CheckBalance) },
                onWalletClick = { onOpenModal(ActiveModal.Wallet) }
            )
        }

        // 3. Grid of Service Icons: 'Transfer Money', 'Mobile Recharge', 'Electricity Bill'
        item {
            QuickServicesGrid(
                onTransferMoneyClick = { onOpenModal(ActiveModal.Transfer()) },
                onMobileRechargeClick = { onOpenModal(ActiveModal.MobileRecharge) },
                onElectricityBillClick = { onOpenModal(ActiveModal.BillPayment("Electricity")) },
                onToBankClick = { onOpenModal(ActiveModal.BankTransfer) },
                onWalletClick = { onOpenModal(ActiveModal.Wallet) },
                onLoanRepaymentClick = { onOpenModal(ActiveModal.BillPayment("Loan")) }
            )
        }

        // 4. PhonePe Wallet, Rewards & Refer Row
        item {
            PhonePeQuickPillsRow(
                walletBalance = state.walletBalance,
                onWalletClick = { onOpenModal(ActiveModal.Wallet) },
                onRewardsClick = { onOpenModal(ActiveModal.ReferAndEarn) },
                onReferClick = { onOpenModal(ActiveModal.ReferAndEarn) }
            )
        }

        // 5. Recent Transactions List (Stored in & observed from Room DB)
        item {
            RecentTransactionsCard(
                transactions = state.transactions,
                onTransactionClick = { tx ->
                    onOpenModal(ActiveModal.TransactionDetails(tx))
                },
                onViewAllClick = onNavigateToHistory
            )
        }

        // Mode Switcher Banner (Native Compose <-> WebView HTML Clone)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF281C38))
                        .border(1.dp, Color(0xFF673AB7), RoundedCornerShape(20.dp))
                        .clickable { onOpenModal(ActiveModal.WebPortal()) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("btn_switch_to_webview"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        tint = AccentGold,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Open WebView (HTML Clone)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE9D5FF)
                    )
                }

                Text(
                    text = "Layout: @id/webView",
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }
        }

        // 4. Banner Section (Turn ₹100/day into ₹37,610/yr - Start Daily RD)
        item {
            PromoBannerCard(
                banners = state.banners,
                onCtaClick = { banner ->
                    if (banner.id == "b1") {
                        onOpenModal(ActiveModal.Wallet)
                    } else if (banner.id == "b2") {
                        onOpenModal(ActiveModal.Transfer(prefillRecipient = "MMTC-PAMP 24K Gold", prefillPhoneOrUpi = "gold@mmtcpamp"))
                    } else {
                        onOpenModal(ActiveModal.BillPayment("Insurance"))
                    }
                }
            )
        }

        // 4. Quick UPI Strip & Recent Transfers Tray
        item {
            QuickUpiTray(
                upiId = state.userProfile.upiId,
                contacts = state.contacts,
                onContactClick = { contact ->
                    onOpenModal(
                        ActiveModal.Transfer(
                            prefillRecipient = contact.name,
                            prefillPhoneOrUpi = contact.upiId
                        )
                    )
                }
            )
        }

        // 5. Recharge & Bills Section (Mobile, Rent, Electricity, Loan + expand)
        item {
            RechargeBillsSection(
                onMobileRechargeClick = { onOpenModal(ActiveModal.MobileRecharge) },
                onRentPaymentClick = { onOpenModal(ActiveModal.BillPayment("Rent")) },
                onElectricityClick = { onOpenModal(ActiveModal.BillPayment("Electricity")) },
                onLoanRepaymentClick = { onOpenModal(ActiveModal.BillPayment("Loan")) },
                onDthClick = { onOpenModal(ActiveModal.BillPayment("DTH")) },
                onGasClick = { onOpenModal(ActiveModal.BillPayment("Gas Cylinder")) },
                onWaterClick = { onOpenModal(ActiveModal.BillPayment("Water")) }
            )
        }

        // 6. Insurance & Wealth Quick Cards
        item {
            InsuranceWealthSection(
                onInsuranceClick = { category ->
                    onOpenModal(ActiveModal.BillPayment(category))
                },
                onWealthClick = { product ->
                    onOpenModal(ActiveModal.Transfer(prefillRecipient = product, prefillPhoneOrUpi = "wealth@phonepe"))
                }
            )
        }
    }
}

@Composable
fun InsuranceWealthSection(
    onInsuranceClick: (String) -> Unit,
    onWealthClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Insurance Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Insurance & Protection",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE5E7EB)
            )
            Text(
                text = "Starting ₹1/day",
                fontSize = 11.sp,
                color = AccentGold
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(SurfaceCard)
                .padding(horizontal = 8.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProductTile(
                icon = Icons.Default.DirectionsBike,
                iconTint = Color(0xFF38BDF8),
                label = "Bike",
                subLabel = "From ₹1.5/day",
                onClick = { onInsuranceClick("Bike Insurance") },
                modifier = Modifier.weight(1f)
            )
            ProductTile(
                icon = Icons.Default.HealthAndSafety,
                iconTint = Color(0xFF34D399),
                label = "Health",
                subLabel = "₹5L Cover",
                onClick = { onInsuranceClick("Health Insurance") },
                modifier = Modifier.weight(1f)
            )
            ProductTile(
                icon = Icons.Default.TrendingUp,
                iconTint = AccentGold,
                label = "24K Gold",
                subLabel = "From ₹10",
                onClick = { onWealthClick("24K Pure Gold") },
                modifier = Modifier.weight(1f)
            )
            ProductTile(
                icon = Icons.Default.Savings,
                iconTint = Color(0xFFA78BFA),
                label = "SIP Funds",
                subLabel = "Top Rated",
                onClick = { onWealthClick("Mutual Funds SIP") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ProductTile(
    icon: ImageVector,
    iconTint: Color,
    label: String,
    subLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFF27272A)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFD1D5DB)
        )
        Text(
            text = subLabel,
            fontSize = 9.sp,
            color = TextMuted
        )
    }
}
