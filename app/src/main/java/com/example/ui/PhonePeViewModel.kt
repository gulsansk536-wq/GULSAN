package com.example.ui

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppState
import com.example.data.PhonePeRepository
import com.example.model.Transaction
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ActiveModal {
    object None : ActiveModal()
    data class Transfer(val prefillRecipient: String = "", val prefillPhoneOrUpi: String = "") : ActiveModal()
    object BankTransfer : ActiveModal()
    object CheckBalance : ActiveModal()
    object Wallet : ActiveModal()
    object MobileRecharge : ActiveModal()
    data class BillPayment(val category: String = "Electricity") : ActiveModal()
    object MyQrCode : ActiveModal()
    object AddressPicker : ActiveModal()
    object ReferAndEarn : ActiveModal()
    object HelpSupport : ActiveModal()
    data class WebPortal(val url: String = "file:///android_asset/phonepe_web.html") : ActiveModal()
    data class TransactionDetails(val transaction: Transaction) : ActiveModal()
    data class PaymentSuccess(val transaction: Transaction) : ActiveModal()
}

enum class NavigationTab {
    HOME,
    SEARCH,
    ALERTS,
    HISTORY
}

class PhonePeViewModel(application: Application) : AndroidViewModel(application) {
    init {
        PhonePeRepository.initDatabase(application)
    }

    val state: StateFlow<AppState> = PhonePeRepository.state

    private val _currentTab = MutableStateFlow(NavigationTab.HOME)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    private val _activeModal = MutableStateFlow<ActiveModal>(ActiveModal.None)
    val activeModal: StateFlow<ActiveModal> = _activeModal.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _historyFilter = MutableStateFlow("ALL")
    val historyFilter: StateFlow<String> = _historyFilter.asStateFlow()

    private val _scannerFlashlightOn = MutableStateFlow(false)
    val scannerFlashlightOn: StateFlow<Boolean> = _scannerFlashlightOn.asStateFlow()

    private val _isProcessingPayment = MutableStateFlow(false)
    val isProcessingPayment: StateFlow<Boolean> = _isProcessingPayment.asStateFlow()

    fun setTab(tab: NavigationTab) {
        _currentTab.value = tab
    }

    fun openModal(modal: ActiveModal) {
        _activeModal.value = modal
    }

    fun closeModal() {
        _activeModal.value = ActiveModal.None
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setHistoryFilter(filter: String) {
        _historyFilter.value = filter
    }

    fun toggleFlashlight() {
        _scannerFlashlightOn.value = !_scannerFlashlightOn.value
    }

    fun performSendMoney(
        recipientName: String,
        upiOrPhone: String,
        amount: Double,
        note: String,
        sourceBank: String
    ) {
        viewModelScope.launch {
            _isProcessingPayment.value = true
            delay(1200) // Realistic UPI handshake simulation
            val tx = PhonePeRepository.sendMoney(
                recipientName = recipientName,
                recipientUpiOrPhone = upiOrPhone,
                amount = amount,
                note = note,
                sourceBankName = sourceBank
            )
            triggerSuccessFeedback()
            _isProcessingPayment.value = false
            _activeModal.value = ActiveModal.PaymentSuccess(tx)
        }
    }

    fun performRecharge(phone: String, operator: String, amount: Double) {
        viewModelScope.launch {
            _isProcessingPayment.value = true
            delay(1000)
            val tx = PhonePeRepository.rechargeMobile(phone, operator, amount)
            triggerSuccessFeedback()
            _isProcessingPayment.value = false
            _activeModal.value = ActiveModal.PaymentSuccess(tx)
        }
    }

    fun performBillPayment(biller: String, consumerNo: String, amount: Double, category: String) {
        viewModelScope.launch {
            _isProcessingPayment.value = true
            delay(1000)
            val tx = PhonePeRepository.payBill(biller, consumerNo, amount, category)
            triggerSuccessFeedback()
            _isProcessingPayment.value = false
            _activeModal.value = ActiveModal.PaymentSuccess(tx)
        }
    }

    fun performWalletTopup(amount: Double) {
        viewModelScope.launch {
            _isProcessingPayment.value = true
            delay(800)
            val tx = PhonePeRepository.addMoneyToWallet(amount)
            triggerSuccessFeedback()
            _isProcessingPayment.value = false
            _activeModal.value = ActiveModal.PaymentSuccess(tx)
        }
    }

    fun updateAddress(title: String, detail: String) {
        PhonePeRepository.updateAddress(title, detail)
        closeModal()
    }

    fun markAlertsRead() {
        PhonePeRepository.markNotificationsAsRead()
    }

    private fun triggerSuccessFeedback() {
        try {
            // Play upbeat chime tone
            val toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 90)
            toneGen.startTone(ToneGenerator.TONE_PROP_BEEP2, 350)
        } catch (_: Exception) {
            // Audio permission/hardware not present, ignore gracefully
        }

        try {
            val context = getApplication<Application>().applicationContext
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(VibrationEffect.createOneShot(120, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(120)
            }
        } catch (_: Exception) {
            // Fallback gracefully
        }
    }
}
