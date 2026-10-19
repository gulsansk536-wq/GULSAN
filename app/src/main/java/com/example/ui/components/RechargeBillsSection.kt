package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
fun RechargeBillsSection(
    onMobileRechargeClick: () -> Unit,
    onRentPaymentClick: () -> Unit,
    onElectricityClick: () -> Unit,
    onLoanRepaymentClick: () -> Unit,
    onDthClick: () -> Unit,
    onGasClick: () -> Unit,
    onWaterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(20.dp))
            .testTag("phonepe_recharge_bills_card")
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
                    .padding(bottom = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recharge & Pay Bills",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = if (expanded) "Show Less" else "My Bills",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFC084FC),
                    modifier = Modifier
                        .clickable { expanded = !expanded }
                        .padding(4.dp)
                        .testTag("toggle_all_bills")
                )
            }

            // Row 1 (4 items)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BillItem(
                    icon = Icons.Default.PhoneIphone,
                    label = "Mobile Recharge",
                    iconColor = Color(0xFF60A5FA),
                    testTag = "bill_mobile_recharge",
                    onClick = onMobileRechargeClick,
                    modifier = Modifier.weight(1f)
                )

                BillItem(
                    icon = Icons.Default.Tv,
                    label = "DTH",
                    iconColor = Color(0xFF38BDF8),
                    testTag = "bill_dth",
                    onClick = onDthClick,
                    modifier = Modifier.weight(1f)
                )

                BillItem(
                    icon = Icons.Default.ElectricBolt,
                    label = "Electricity",
                    iconColor = Color(0xFFFB923C),
                    testTag = "bill_electricity",
                    onClick = onElectricityClick,
                    modifier = Modifier.weight(1f)
                )

                BillItem(
                    icon = Icons.Default.CreditCard,
                    label = "Credit Card Bill",
                    iconColor = Color(0xFFA78BFA),
                    testTag = "bill_credit_card",
                    onClick = onRentPaymentClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Row 2 (4 items)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BillItem(
                    icon = Icons.Default.Home,
                    label = "Rent Payment",
                    iconColor = Color(0xFFF87171),
                    testTag = "bill_rent_payment",
                    onClick = onRentPaymentClick,
                    modifier = Modifier.weight(1f)
                )

                BillItem(
                    icon = Icons.Default.Payment,
                    label = "Loan Repayment",
                    iconColor = Color(0xFFFACC15),
                    testTag = "bill_loan_repayment",
                    onClick = onLoanRepaymentClick,
                    modifier = Modifier.weight(1f)
                )

                BillItem(
                    icon = Icons.Default.LocalGasStation,
                    label = "Book Cylinder",
                    iconColor = Color(0xFFFB7185),
                    testTag = "bill_gas",
                    onClick = onGasClick,
                    modifier = Modifier.weight(1f)
                )

                BillItem(
                    icon = Icons.Default.MoreHoriz,
                    label = "See All",
                    iconColor = Color(0xFFE0E7FF),
                    testTag = "bill_see_all",
                    onClick = { expanded = !expanded },
                    modifier = Modifier.weight(1f)
                )
            }

            // Expanded utilities
            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BillItem(
                            icon = Icons.Default.WaterDrop,
                            label = "Water Bill",
                            iconColor = Color(0xFF34D399),
                            testTag = "bill_water",
                            onClick = onWaterClick,
                            modifier = Modifier.weight(1f)
                        )

                        BillItem(
                            icon = Icons.Default.Router,
                            label = "Broadband",
                            iconColor = Color(0xFF818CF8),
                            testTag = "bill_broadband",
                            onClick = onDthClick,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.weight(2f))
                    }
                }
            }
        }
    }
}

@Composable
private fun BillItem(
    icon: ImageVector,
    label: String,
    iconColor: Color,
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
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF27272A))
                .border(1.dp, Color(0xFF383838), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
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
