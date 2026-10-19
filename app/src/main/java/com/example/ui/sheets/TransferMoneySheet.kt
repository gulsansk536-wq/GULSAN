package com.example.ui.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
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
import com.example.data.AppState
import com.example.model.Contact
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferMoneySheet(
    state: AppState,
    prefillName: String,
    prefillPhoneOrUpi: String,
    isProcessing: Boolean,
    onDismiss: () -> Unit,
    onSendMoney: (name: String, upiOrPhone: String, amount: Double, note: String, sourceBank: String) -> Unit
) {
    var selectedContact by remember {
        mutableStateOf(
            if (prefillName.isNotEmpty()) {
                Contact("temp", prefillName, prefillPhoneOrUpi, prefillPhoneOrUpi, "PA", 0xFF6366F1)
            } else null
        )
    }

    var manualInput by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("100") }
    var noteText by remember { mutableStateOf("") }
    var selectedBankIndex by remember { mutableStateOf(0) }
    var showPinStep by remember { mutableStateOf(false) }
    var enteredPin by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color(0xFF6B7280)) },
        modifier = Modifier.fillMaxHeight(0.92f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (showPinStep) "Enter UPI PIN" else "Send Money",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (!showPinStep) {
                // Step 1: Select recipient & enter amount
                if (selectedContact == null) {
                    // Contact search / manual input
                    OutlinedTextField(
                        value = manualInput,
                        onValueChange = { manualInput = it },
                        label = { Text("Enter Name, Mobile Number or UPI ID") },
                        placeholder = { Text("e.g. 9876543210 or name@upi") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PhonePePurple,
                            unfocusedBorderColor = SurfaceBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("transfer_recipient_input")
                    )

                    if (manualInput.length >= 4) {
                        Button(
                            onClick = {
                                selectedContact = Contact(
                                    id = "manual_${System.currentTimeMillis()}",
                                    name = manualInput,
                                    phone = manualInput,
                                    upiId = if (manualInput.contains("@")) manualInput else "$manualInput@ybl",
                                    initials = manualInput.take(2).uppercase(),
                                    colorHex = 0xFF8B5CF6
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .testTag("btn_proceed_manual_contact")
                        ) {
                            Text("Pay to \"$manualInput\"")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Recent Contacts",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val filteredContacts = state.contacts.filter {
                            manualInput.isBlank() ||
                                    it.name.contains(manualInput, ignoreCase = true) ||
                                    it.phone.contains(manualInput) ||
                                    it.upiId.contains(manualInput, ignoreCase = true)
                        }

                        items(filteredContacts) { contact ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SurfaceCard)
                                    .clickable { selectedContact = contact }
                                    .padding(12.dp)
                                    .testTag("contact_item_${contact.id}"),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(Color(contact.colorHex)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = contact.initials,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = contact.name,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = contact.upiId,
                                        fontSize = 12.sp,
                                        color = TextMuted
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Recipient selected, configure amount and bank
                    val contact = selectedContact!!
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceCard)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(contact.colorHex)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = contact.initials,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = contact.name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = contact.upiId,
                                fontSize = 12.sp,
                                color = TextMuted
                            )
                        }
                        TextButton(
                            onClick = { selectedContact = null },
                            modifier = Modifier.testTag("btn_change_recipient")
                        ) {
                            Text("Change", color = PhonePePurpleLight)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Amount input
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it.filter { char -> char.isDigit() } },
                        label = { Text("Amount") },
                        prefix = { Text("₹ ", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary) },
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
                            .testTag("transfer_amount_input")
                    )

                    // Quick amount chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("100", "200", "500", "1000", "2000").forEach { quickAmount ->
                            AssistChip(
                                onClick = { amountText = quickAmount },
                                label = { Text("₹$quickAmount", fontSize = 11.sp) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = SurfaceCard,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }

                    // Optional Note
                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        label = { Text("Add a note (optional)") },
                        placeholder = { Text("e.g. Dinner, Rent, Books") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PhonePePurple,
                            unfocusedBorderColor = SurfaceBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("transfer_note_input")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Bank Account Selector
                    Text(
                        text = "Debit From",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    val accountsList = state.bankAccounts.map {
                        "${it.bankName} ${it.accountNumberMasked} (Bal: ₹${"%.2f".format(it.balance)})"
                    } + listOf("PhonePe Wallet (Bal: ₹${"%.2f".format(state.walletBalance)})")

                    accountsList.forEachIndexed { index, accountTitle ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (selectedBankIndex == index) Color(0xFF381559) else SurfaceCard)
                                .border(
                                    1.dp,
                                    if (selectedBankIndex == index) PhonePePurpleLight else Color.Transparent,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedBankIndex = index }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = accountTitle,
                                fontSize = 12.sp,
                                color = if (selectedBankIndex == index) Color.White else TextSecondary
                            )
                            if (selectedBankIndex == index) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color(0xFFC084FC),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    Button(
                        onClick = {
                            if (amount > 0) {
                                showPinStep = true
                            }
                        },
                        enabled = amount > 0,
                        colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_proceed_to_pin")
                    ) {
                        Text(
                            text = "Proceed to Pay ₹${"%.0f".format(amount)}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            } else {
                // Step 2: Simulated UPI PIN screen (authentic 4-pin keypad)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val contact = selectedContact!!
                    Text(
                        text = "Paying ${contact.name}",
                        fontSize = 15.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = "₹$amountText",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 4 PIN Dots
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(vertical = 12.dp)
                    ) {
                        for (i in 0 until 4) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (i < enteredPin.length) PhonePePurpleLight else Color(0xFF374151)
                                    )
                                    .border(1.dp, Color(0xFF4B5563), CircleShape)
                            )
                        }
                    }

                    Text(
                        text = "Enter 4-digit UPI PIN (any 4 digits)",
                        fontSize = 11.sp,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Keypad 1-9, 0, Backspace
                    val keys = listOf(
                        listOf("1", "2", "3"),
                        listOf("4", "5", "6"),
                        listOf("7", "8", "9"),
                        listOf("Clear", "0", "⌫")
                    )

                    keys.forEach { row ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            row.forEach { key ->
                                Box(
                                    modifier = Modifier
                                        .size(68.dp, 52.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(SurfaceCard)
                                        .clickable {
                                            when (key) {
                                                "Clear" -> enteredPin = ""
                                                "⌫" -> if (enteredPin.isNotEmpty()) enteredPin = enteredPin.dropLast(1)
                                                else -> {
                                                    if (enteredPin.length < 4) {
                                                        enteredPin += key
                                                    }
                                                }
                                            }
                                        }
                                        .testTag("pin_key_$key"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = key,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (isProcessing) {
                        CircularProgressIndicator(
                            color = PhonePePurpleLight,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Securing transaction with UPI...",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    } else {
                        Button(
                            onClick = {
                                val amount = amountText.toDoubleOrNull() ?: 0.0
                                val bankName = if (selectedBankIndex < state.bankAccounts.size) {
                                    state.bankAccounts[selectedBankIndex].bankName
                                } else "PhonePe Wallet"

                                onSendMoney(
                                    contact.name,
                                    contact.upiId,
                                    amount,
                                    noteText,
                                    bankName
                                )
                            },
                            enabled = enteredPin.length == 4,
                            colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_submit_pin_payment")
                        ) {
                            Text(
                                text = "Confirm Payment",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
