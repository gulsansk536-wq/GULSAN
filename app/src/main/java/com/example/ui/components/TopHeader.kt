package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserProfile
import com.example.ui.theme.*

@Composable
fun TopHeader(
    profile: UserProfile,
    unreadAlertsCount: Int,
    onAvatarClick: () -> Unit,
    onAddressClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onHelpClick: () -> Unit,
    onQrScanClick: () -> Unit = {},
    onWebClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        PhonePePurple,
                        PhonePePurpleDeep
                    )
                )
            )
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left: Profile Avatar with QR badge + Location/Address
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clickable { onAvatarClick() }
                    .testTag("header_avatar_qr"),
                contentAlignment = Alignment.Center
            ) {
                // Profile circle
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4A148C))
                        .border(1.5.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = profile.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").ifEmpty { "GK" },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Small QR badge at the bottom right corner
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 2.dp, y = 2.dp)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, PhonePePurple, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = "My QR Code",
                        tint = PhonePePurple,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .clickable { onAddressClick() }
                    .testTag("header_address_picker")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = profile.addressTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select Address",
                        tint = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = profile.addressDetail,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.75f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.widthIn(max = 180.dp)
                )
            }
        }

        // Right Action Icons (QR Scanner, Notifications, Help, Web)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // QR Scanner Icon in Header (Exact PhonePe style)
            IconButton(
                onClick = onQrScanClick,
                modifier = Modifier.size(36.dp).testTag("header_qr_scanner_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = "Scan QR",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Notifications
            IconButton(
                onClick = onAlertsClick,
                modifier = Modifier.size(36.dp).testTag("header_notifications_btn")
            ) {
                if (unreadAlertsCount > 0) {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = AccentRed,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = unreadAlertsCount.toString(),
                                    fontSize = 9.sp
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Help & Support
            IconButton(
                onClick = onHelpClick,
                modifier = Modifier.size(36.dp).testTag("header_help_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.HelpOutline,
                    contentDescription = "Help & Support",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Web Mode Switcher
            IconButton(
                onClick = onWebClick,
                modifier = Modifier.size(36.dp).testTag("header_web_portal_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Web Portal",
                    tint = AccentGold,
                    modifier = Modifier.size(19.dp)
                )
            }
        }
    }
}
