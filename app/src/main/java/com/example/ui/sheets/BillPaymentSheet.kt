package com.example.ui.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Receipt
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
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillPaymentSheet(
    category: String,
    isProcessing: Boolean,
    onDismiss: () -> Unit,
    onProceedPayment: (biller: String, consumerNo: String, amount: Double, category: String) -> Unit
) {
    val billers = when (category) {
        "Electricity" -> listOf(
            "BESCOM (Bangalore Electricity)",
            "Tata Power (Delhi & Mumbai)",
            "Adani Electricity Mumbai",
            "MSEDCL (Mahavitaran)",
            "Torrent Power",
            "WBSEDCL (West Bengal)"
        )
        "Rent" -> listOf(
            "House Rent Payment",
            "Office Rent Payment",
            "Society Maintenance",
            "Security Deposit"
        )
        "Loan" -> listOf(
            "Bajaj Finance Limited",
            "Home Credit India",
            "Tata Capital Financial Services",
            "Muthoot Finance",
            "ZestMoney"
        )
        "DTH" -> listOf(
            "Tata Play (Tata Sky)",
            "Airtel Digital TV",
            "Dish TV India",
            "Sun Direct"
        )
        else -> listOf(
            "Indane Gas (LPG Cylinder)",
            "Bharat Gas",
            "HP Gas",
            "Municipal Water Board"
        )
    }

    var selectedBiller by remember { mutableStateOf(billers.first()) }
    var consumerNumber by remember { mutableStateOf(if (category == "Electricity") "89201928" else "9876543210") }
    var billAmountText by remember { mutableStateOf(if (category == "Electricity") "1420" else if (category == "Rent") "18500" else "2500") }
    var isBillFetched by remember { mutableStateOf(true) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFF6B7280)) },
        modifier = Modifier.fillMaxHeight(0.88f)
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = when (category) {
                            "Electricity" -> Icons.Default.ElectricBolt
                            "Rent" -> Icons.Default.Home
                            "Loan" -> Icons.Default.Payment
                            else -> Icons.Default.Receipt
                        },
                        contentDescription = null,
                        tint = PhonePePurpleLight,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$category Bill Payment",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Select Provider / Biller",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(6.dp))

            LazyColumn(
                modifier = Modifier.heightIn(max = 130.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(billers) { b ->
                    val isSelected = b == selectedBiller
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) PhonePePurpleContainer else SurfaceCard)
                            .border(
                                1.dp,
                                if (isSelected) PhonePePurpleLight else Color.Transparent,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedBiller = b }
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = b,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = consumerNumber,
                onValueChange = { consumerNumber = it },
                label = { Text(if (category == "Rent") "Landlord Account / UPI ID" else "Consumer ID / Account No") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PhonePePurple,
                    unfocusedBorderColor = SurfaceBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bill_consumer_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = billAmountText,
                onValueChange = { billAmountText = it.filter { char -> char.isDigit() } },
                label = { Text("Bill Amount") },
                prefix = { Text("₹ ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PhonePePurple,
                    unfocusedBorderColor = SurfaceBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bill_amount_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Bill Due info banner
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Due Date: 28 Oct 2026",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF93C5FD)
                        )
                        Text(
                            text = "Verified BBPS Bill Fetcher",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF065F46))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Fetched",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF34D399)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            val amount = billAmountText.toDoubleOrNull() ?: 0.0

            if (isProcessing) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = PhonePePurpleLight)
                }
            } else {
                Button(
                    onClick = {
                        onProceedPayment(
                            selectedBiller,
                            consumerNumber,
                            amount,
                            category
                        )
                    },
                    enabled = amount > 0 && consumerNumber.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_confirm_bill_pay")
                ) {
                    Text(
                        text = "Pay ₹${"%,.0f".format(amount)} via UPI",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
