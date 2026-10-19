package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NavigationTab
import com.example.ui.theme.*

@Composable
fun PhonePeBottomNavBar(
    selectedTab: NavigationTab,
    unreadAlertsCount: Int,
    onTabSelected: (NavigationTab) -> Unit,
    onQrScanClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceDark)
    ) {
        // Border on top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFF27272A))
                .align(Alignment.TopCenter)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Home
            NavItem(
                icon = if (selectedTab == NavigationTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                label = "Home",
                isSelected = selectedTab == NavigationTab.HOME,
                testTag = "nav_home",
                onClick = { onTabSelected(NavigationTab.HOME) },
                modifier = Modifier.weight(1f)
            )

            // Search
            NavItem(
                icon = if (selectedTab == NavigationTab.SEARCH) Icons.Filled.Search else Icons.Outlined.Search,
                label = "Search",
                isSelected = selectedTab == NavigationTab.SEARCH,
                testTag = "nav_search",
                onClick = { onTabSelected(NavigationTab.SEARCH) },
                modifier = Modifier.weight(1f)
            )

            // Center Elevated QR Scanner Button (-mt-6 effect)
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .padding(bottom = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .border(3.dp, BackgroundDark, CircleShape)
                        .clip(CircleShape)
                        .background(PhonePePurple)
                        .clickable { onQrScanClick() }
                        .testTag("nav_qr_scan_fab"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "Scan QR Code",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Alerts
            NavItem(
                icon = if (selectedTab == NavigationTab.ALERTS) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                label = "Alerts",
                badgeCount = unreadAlertsCount,
                isSelected = selectedTab == NavigationTab.ALERTS,
                testTag = "nav_alerts",
                onClick = { onTabSelected(NavigationTab.ALERTS) },
                modifier = Modifier.weight(1f)
            )

            // History
            NavItem(
                icon = if (selectedTab == NavigationTab.HISTORY) Icons.Filled.History else Icons.Outlined.History,
                label = "History",
                isSelected = selectedTab == NavigationTab.HISTORY,
                testTag = "nav_history",
                onClick = { onTabSelected(NavigationTab.HISTORY) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeCount: Int = 0
) {
    val tintColor = if (isSelected) Color(0xFFC084FC) else Color(0xFF9CA3AF)

    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 4.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (badgeCount > 0) {
            BadgedBox(
                badge = {
                    Badge(
                        containerColor = AccentRed,
                        contentColor = Color.White
                    ) {
                        Text(text = badgeCount.toString(), fontSize = 9.sp)
                    }
                }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = tintColor,
                    modifier = Modifier.size(22.dp)
                )
            }
        } else {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tintColor,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            fontSize = 10.sp,
            color = tintColor
        )
    }
}
