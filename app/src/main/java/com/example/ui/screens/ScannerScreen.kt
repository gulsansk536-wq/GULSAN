package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ActiveModal
import com.example.ui.theme.*

@Composable
fun ScannerScreen(
    flashlightOn: Boolean,
    onToggleFlashlight: () -> Unit,
    onOpenModal: (ActiveModal) -> Unit,
    onCloseScanner: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Animated laser beam moving up and down inside scanner box
    val infiniteTransition = rememberInfiniteTransition(label = "laser")
    val laserOffsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 220f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_pos"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Scanner Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onCloseScanner,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0x66000000))
                        .testTag("btn_close_scanner")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }

                Text(
                    text = "Scan Any QR Code",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                IconButton(
                    onClick = onToggleFlashlight,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (flashlightOn) AccentGold else Color(0x66000000))
                        .testTag("btn_toggle_flashlight")
                ) {
                    Icon(
                        imageVector = if (flashlightOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                        contentDescription = "Torch",
                        tint = if (flashlightOn) Color.Black else Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Viewfinder Target Area (with glowing purple border & laser)
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(2.5.dp, if (flashlightOn) AccentGold else PhonePePurpleLight, RoundedCornerShape(24.dp))
                    .background(Color(0x331E1E1E)),
                contentAlignment = Alignment.TopCenter
            ) {
                // Moving laser scanner line
                Box(
                    modifier = Modifier
                        .offset(y = laserOffsetY.dp)
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, Color(0xFFC084FC), Color.White, Color(0xFFC084FC), Color.Transparent)
                            )
                        )
                )

                // Corner indicators
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Align QR code inside the frame to scan",
                        fontSize = 12.sp,
                        color = Color(0xCCFFFFFF),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Pills: Upload Gallery / Show My QR
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        Toast.makeText(context, "Scanning selected gallery image...", Toast.LENGTH_SHORT).show()
                        onOpenModal(ActiveModal.Transfer("Reliance Fresh Mart", "reliancefresh@upi"))
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("btn_gallery_qr")
                ) {
                    Icon(imageVector = Icons.Default.Image, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Upload from Gallery", fontSize = 12.sp)
                }

                Button(
                    onClick = { onOpenModal(ActiveModal.MyQrCode) },
                    colors = ButtonDefaults.buttonColors(containerColor = PhonePePurple),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.testTag("btn_show_my_qr")
                ) {
                    Icon(imageVector = Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("My QR", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Interactive Quick QR Demos
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Quick Demo Scans",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentGold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                onOpenModal(ActiveModal.Transfer("Chai Point Store #12", "chaipoint@upi"))
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27272A)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("quick_scan_chai")
                        ) {
                            Text("Chai Point ₹40", fontSize = 11.sp, color = TextPrimary)
                        }

                        Button(
                            onClick = {
                                onOpenModal(ActiveModal.Transfer("Indian Oil Fuel #481", "indianoil@upi"))
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27272A)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("quick_scan_fuel")
                        ) {
                            Text("Fuel ₹500", fontSize = 11.sp, color = TextPrimary)
                        }
                    }
                }
            }
        }
    }
}
