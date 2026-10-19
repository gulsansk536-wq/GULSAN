package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.ActiveModal
import com.example.ui.NavigationTab
import com.example.ui.PhonePeViewModel
import com.example.ui.components.PhonePeBottomNavBar
import com.example.ui.screens.*
import com.example.ui.sheets.*
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                PhonePeApp()
            }
        }
    }
}

@Composable
fun PhonePeApp(
    viewModel: PhonePeViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val activeModal by viewModel.activeModal.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val historyFilter by viewModel.historyFilter.collectAsStateWithLifecycle()
    val scannerFlashlightOn by viewModel.scannerFlashlightOn.collectAsStateWithLifecycle()
    val isProcessingPayment by viewModel.isProcessingPayment.collectAsStateWithLifecycle()

    var isScannerOpen by remember { mutableStateOf(false) }

    val unreadAlertsCount = state.notifications.count { !it.isRead }

    // Predictive back handler
    BackHandler(enabled = isScannerOpen || activeModal !is ActiveModal.None || currentTab != NavigationTab.HOME) {
        when {
            activeModal !is ActiveModal.None -> viewModel.closeModal()
            isScannerOpen -> isScannerOpen = false
            currentTab != NavigationTab.HOME -> viewModel.setTab(NavigationTab.HOME)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark),
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            if (!isScannerOpen) {
                PhonePeBottomNavBar(
                    selectedTab = currentTab,
                    unreadAlertsCount = unreadAlertsCount,
                    onTabSelected = { tab ->
                        viewModel.setTab(tab)
                        if (tab == NavigationTab.ALERTS) {
                            viewModel.markAlertsRead()
                        }
                    },
                    onQrScanClick = {
                        isScannerOpen = true
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BackgroundDark)
        ) {
            if (isScannerOpen) {
                ScannerScreen(
                    flashlightOn = scannerFlashlightOn,
                    onToggleFlashlight = { viewModel.toggleFlashlight() },
                    onOpenModal = { modal ->
                        isScannerOpen = false
                        viewModel.openModal(modal)
                    },
                    onCloseScanner = { isScannerOpen = false }
                )
            } else {
                when (currentTab) {
                    NavigationTab.HOME -> {
                        HomeScreen(
                            state = state,
                            onOpenModal = { viewModel.openModal(it) },
                            onNavigateToAlerts = { viewModel.setTab(NavigationTab.ALERTS) },
                            onNavigateToHistory = { viewModel.setTab(NavigationTab.HISTORY) },
                            onQrScanClick = { isScannerOpen = true }
                        )
                    }
                    NavigationTab.SEARCH -> {
                        SearchScreen(
                            state = state,
                            searchQuery = searchQuery,
                            onQueryChange = { viewModel.setSearchQuery(it) },
                            onOpenModal = { viewModel.openModal(it) }
                        )
                    }
                    NavigationTab.ALERTS -> {
                        AlertsScreen(
                            state = state,
                            onMarkAllRead = { viewModel.markAlertsRead() }
                        )
                    }
                    NavigationTab.HISTORY -> {
                        HistoryScreen(
                            state = state,
                            filter = historyFilter,
                            onFilterChange = { viewModel.setHistoryFilter(it) },
                            onTransactionClick = { tx ->
                                viewModel.openModal(ActiveModal.TransactionDetails(tx))
                            }
                        )
                    }
                }
            }

            // Modal BottomSheets and Dialogs
            when (val modal = activeModal) {
                is ActiveModal.Transfer -> {
                    TransferMoneySheet(
                        state = state,
                        prefillName = modal.prefillRecipient,
                        prefillPhoneOrUpi = modal.prefillPhoneOrUpi,
                        isProcessing = isProcessingPayment,
                        onDismiss = { viewModel.closeModal() },
                        onSendMoney = { name, upiOrPhone, amount, note, sourceBank ->
                            viewModel.performSendMoney(name, upiOrPhone, amount, note, sourceBank)
                        }
                    )
                }
                is ActiveModal.CheckBalance -> {
                    CheckBalanceSheet(
                        state = state,
                        onDismiss = { viewModel.closeModal() }
                    )
                }
                is ActiveModal.Wallet -> {
                    WalletSheet(
                        state = state,
                        isProcessing = isProcessingPayment,
                        onDismiss = { viewModel.closeModal() },
                        onTopupWallet = { amount ->
                            viewModel.performWalletTopup(amount)
                        }
                    )
                }
                is ActiveModal.MobileRecharge -> {
                    MobileRechargeSheet(
                        isProcessing = isProcessingPayment,
                        onDismiss = { viewModel.closeModal() },
                        onProceedRecharge = { phone, operator, amount ->
                            viewModel.performRecharge(phone, operator, amount)
                        }
                    )
                }
                is ActiveModal.BillPayment -> {
                    BillPaymentSheet(
                        category = modal.category,
                        isProcessing = isProcessingPayment,
                        onDismiss = { viewModel.closeModal() },
                        onProceedPayment = { biller, consumerNo, amount, category ->
                            viewModel.performBillPayment(biller, consumerNo, amount, category)
                        }
                    )
                }
                is ActiveModal.MyQrCode -> {
                    MyQrDialog(
                        profile = state.userProfile,
                        onDismiss = { viewModel.closeModal() }
                    )
                }
                is ActiveModal.PaymentSuccess -> {
                    PaymentSuccessDialog(
                        transaction = modal.transaction,
                        onViewReceipt = { tx ->
                            viewModel.openModal(ActiveModal.TransactionDetails(tx))
                        },
                        onDismiss = { viewModel.closeModal() }
                    )
                }
                is ActiveModal.TransactionDetails -> {
                    TransactionDetailsSheet(
                        transaction = modal.transaction,
                        onRepeatPayment = { tx ->
                            viewModel.openModal(
                                ActiveModal.Transfer(
                                    prefillRecipient = tx.title.removePrefix("Paid to "),
                                    prefillPhoneOrUpi = tx.subtitle
                                )
                            )
                        },
                        onDismiss = { viewModel.closeModal() }
                    )
                }
                is ActiveModal.AddressPicker -> {
                    AddressPickerSheet(
                        currentTitle = state.userProfile.addressTitle,
                        currentDetail = state.userProfile.addressDetail,
                        onSaveAddress = { title, detail ->
                            viewModel.updateAddress(title, detail)
                        },
                        onDismiss = { viewModel.closeModal() }
                    )
                }
                is ActiveModal.ReferAndEarn -> {
                    ReferAndEarnSheet(
                        onDismiss = { viewModel.closeModal() }
                    )
                }
                is ActiveModal.HelpSupport -> {
                    HelpSupportSheet(
                        onDismiss = { viewModel.closeModal() }
                    )
                }
                is ActiveModal.BankTransfer -> {
                    TransferMoneySheet(
                        state = state,
                        prefillName = "",
                        prefillPhoneOrUpi = "",
                        isProcessing = isProcessingPayment,
                        onDismiss = { viewModel.closeModal() },
                        onSendMoney = { name, upiOrPhone, amount, note, sourceBank ->
                            viewModel.performSendMoney(name, upiOrPhone, amount, note, sourceBank)
                        }
                    )
                }
                is ActiveModal.WebPortal -> {
                    WebViewScreen(
                        initialUrl = modal.url,
                        onClose = { viewModel.closeModal() }
                    )
                }
                ActiveModal.None -> { /* No modal */ }
            }
        }
    }
}
