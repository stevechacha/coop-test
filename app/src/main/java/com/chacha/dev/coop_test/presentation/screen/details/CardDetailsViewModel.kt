package com.chacha.dev.coop_test.presentation.screen.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chacha.dev.coop_test.domain.model.CardModel
import com.chacha.dev.coop_test.domain.model.TransactionModel
import com.chacha.dev.coop_test.domain.model.WalletModel
import com.chacha.dev.coop_test.domain.usecases.ObserveCardUseCase
import com.chacha.dev.coop_test.domain.usecases.ObserveTransactionsUseCase
import com.chacha.dev.coop_test.domain.usecases.ToggleCardStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class CardDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val observeCard: ObserveCardUseCase,
    private val observeTransactions: ObserveTransactionsUseCase,
    private val toggleCardStatus: ToggleCardStatusUseCase
) : ViewModel() {

    private val cardId: String = checkNotNull(savedStateHandle["id"])
    private val _state = MutableStateFlow(CardDetailsUiState())
    val state: StateFlow<CardDetailsUiState> = _state

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                observeCard(cardId).filterNotNull(),
                observeTransactions(cardId)
            ) { card, transactions ->
                card to transactions
            }.collect { (card, tx) ->
                val selectedWallet = _state.value.selectedWallet ?: card.wallets.firstOrNull()
                _state.update {
                    it.copy(
                        card = card,
                        selectedWallet = selectedWallet,
                        transactions = tx.filterByWallet(selectedWallet),
                        allTransactions = tx,
                        isLoading = false,
                        error = null
                    )
                }
            }
        }
    }

    fun onSelectWallet(wallet: WalletModel) {
        _state.update { current ->
            current.copy(
                selectedWallet = wallet,
                transactions = current.allTransactions.filterByWallet(wallet)
            )
        }
    }

    fun onToggleBlock() {
        viewModelScope.launch {
            try {
                toggleCardStatus(cardId)
            } catch (e: Exception) {
                _state.update { it.copy(error = e.localizedMessage) }
            }
        }
    }

    /**
        Explicit trigger to refresh UI state when screen is opened.
        Currently just resets loading/error; data flows are already active.
     */
    fun loadCardDetails(requestedId: String) {
        if (requestedId != cardId) return
        _state.update { it.copy(isLoading = true, error = null) }
        // The observers will update state when flows emit.
    }
}

private fun List<TransactionModel>.filterByWallet(wallet: WalletModel?): List<TransactionModel> =
    if (wallet == null) this else filter { it.currency == wallet.currency }