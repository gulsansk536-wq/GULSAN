package com.example.ui.sheets

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressPickerSheet(
    currentTitle: String,
    currentDetail: String,
    onSaveAddress: (title: String, detail: String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf(currentTitle) }
    var detail by remember { mutableStateOf(currentDetail) }

    val presetAddresses = listOf(
        Pair("Home", "Flat 402, Green Park Avenue, Bengaluru - 560034"),
        Pair("Office", "Tower 3, EcoSpace Business Park, Bellandur, Bengaluru - 560103"),
        Pair("Current Location", "Koramangala 4th Block, Bengaluru - 560034")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFF6B7280)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select Delivery / Default Address",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            presetAddresses.forEach { (presetTitle, presetDetail) ->
                val isSelected = title == presetTitle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) PhonePePurpleContainer else SurfaceCard)
                        .border(
                            1.dp,
                            if (isSelected) PhonePePurpleLight else Color.Transparent,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            title = presetTitle
                            detail = presetDetail
                        }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (presetTitle == "Home") Icons.Default.Home else if (presetTitle == "Office") Icons.Default.Business else Icons.Default.MyLocation,
                        contentDescription = null,
                        tint = if (isSelected) Color(0xFFD8B4FE) else TextSecondary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = presetTitle,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else TextPrimary
                        )
                        Text(
                            text = presetDetail,
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                    if (isSelected) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = PhonePePurpleLight)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { onSaveAddress(title, detail) },
                colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_confirm_address")
            ) {
                Text("Confirm Address", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReferAndEarnSheet(
    onDismiss: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val referralCode = "PHONEEPE50"

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFF6B7280)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Refer & Earn ₹50",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF431407)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CardGiftcard,
                    contentDescription = null,
                    tint = AccentOrange,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Earn ₹50 on Every Invite!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary
            )

            Text(
                text = "When your friend installs PhonePe and makes their first UPI transfer, you both get cashback directly in your wallet.",
                fontSize = 12.sp,
                color = TextMuted,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Code box
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceCard)
                    .border(1.dp, Color(0xFF9A3412), RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Code: $referralCode",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = AccentGold
                )
                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString("https://phon.pe/$referralCode"))
                        Toast.makeText(context, "Referral link copied!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy", tint = TextSecondary, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    clipboardManager.setText(AnnotatedString("Join me on PhonePe! Use code $referralCode: https://phon.pe/$referralCode"))
                    Toast.makeText(context, "Invite link shared!", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_share_referral")
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Share Invite Link", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportSheet(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val faqs = listOf(
        Pair("How do I check my bank balance?", "Go to Home > Check Balance, enter your 4-digit UPI PIN, and view balances of all linked accounts."),
        Pair("What is PhonePe Wallet?", "PhonePe Wallet allows instant payments without a UPI PIN. You can store funds and earn cashback on merchant transactions."),
        Pair("Is UPI transfer safe on PhonePe?", "PhonePe uses 256-bit bank-grade encryption and NPCI-certified UPI protocols. Never share your UPI PIN to receive money."),
        Pair("What should I do if a transaction is pending?", "UPI transactions resolve within 24-48 hours. If money was debited, it will either credit to the beneficiary or auto-refund.")
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFF6B7280)) },
        modifier = Modifier.fillMaxHeight(0.85f)
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
                    text = "Help & 24x7 Customer Support",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Support Card
            Card(
                colors = CardDefaults.cardColors(containerColor = PhonePePurpleContainer),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.HeadsetMic, contentDescription = null, tint = Color(0xFFD8B4FE), modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("24x7 Support Available", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Instant resolution for payments, recharge, or fraud alerts", fontSize = 11.sp, color = Color(0xFFE9D5FF))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Frequently Asked Questions", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(faqs) { (q, a) ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = q, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = a, fontSize = 11.sp, color = TextMuted)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    Toast.makeText(context, "Support ticket opened. Ticket #PE-98210", Toast.LENGTH_SHORT).show()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_open_support_ticket")
            ) {
                Text("Chat with Support Agent", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
