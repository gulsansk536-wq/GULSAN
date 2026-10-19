package com.example.ui.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RechargePlan
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MobileRechargeSheet(
    isProcessing: Boolean,
    onDismiss: () -> Unit,
    onProceedRecharge: (phone: String, operator: String, amount: Double) -> Unit
) {
    var phoneNumber by remember { mutableStateOf("9876543210") }
    var selectedOperator by remember { mutableStateOf("Airtel") }
    val operators = listOf("Airtel", "Jio", "Vodafone Idea (Vi)", "BSNL")

    val plans = listOf(
        RechargePlan("p1", 299, "28 Days", "1.5 GB/day", "Truly Unlimited Calls + 100 SMS/day", isPopular = true),
        RechargePlan("p2", 349, "28 Days", "2.0 GB/day", "Unlimited 5G Data + Disney+ Hotstar Mobile"),
        RechargePlan("p3", 719, "84 Days", "1.5 GB/day", "Truly Unlimited Calls + 100 SMS/day", isPopular = true),
        RechargePlan("p4", 2999, "365 Days", "2.5 GB/day", "Annual Super Saver + Amazon Prime Lite")
    )

    var selectedPlan by remember { mutableStateOf(plans.first()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFF6B7280)) },
        modifier = Modifier.fillMaxHeight(0.9f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mobile Recharge",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { if (it.length <= 10) phoneNumber = it.filter { char -> char.isDigit() } },
                label = { Text("Mobile Number") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.PhoneIphone, contentDescription = null, tint = PhonePePurpleLight)
                },
                prefix = { Text("+91 ", color = TextPrimary) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PhonePePurple,
                    unfocusedBorderColor = SurfaceBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("recharge_phone_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Select Operator",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(operators) { op ->
                    val isSelected = op == selectedOperator
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) PhonePePurpleContainer else SurfaceCard)
                            .border(
                                1.dp,
                                if (isSelected) PhonePePurpleLight else Color.Transparent,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedOperator = op }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("operator_chip_$op")
                    ) {
                        Text(
                            text = op,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Select Recharge Plan",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(plans) { plan ->
                    val isSelected = plan.id == selectedPlan.id
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFF2E1065) else SurfaceCard
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.5.dp,
                                if (isSelected) PhonePePurpleLight else Color.Transparent,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedPlan = plan }
                            .testTag("plan_card_${plan.amount}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "₹${plan.amount}",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black,
                                        color = TextPrimary
                                    )
                                    if (plan.isPopular) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(AccentOrange)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "POPULAR",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = plan.validity,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = AccentBlueLight
                                    )
                                    Text(
                                        text = "• ${plan.data}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = AccentGreenLight
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = plan.description,
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (isProcessing) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PhonePePurpleLight)
                }
            } else {
                Button(
                    onClick = {
                        onProceedRecharge(
                            phoneNumber,
                            selectedOperator,
                            selectedPlan.amount.toDouble()
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_confirm_recharge")
                ) {
                    Text(
                        text = "Pay ₹${selectedPlan.amount} for $selectedOperator",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
