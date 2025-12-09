package com.chacha.dev.coop_test.presentation.screen.mycards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chacha.dev.coop_test.domain.model.CardModel
import com.chacha.dev.coop_test.domain.usecases.ObserveCardsUseCase
import com.chacha.dev.coop_test.domain.usecases.ObserveUserUseCase
import com.chacha.dev.coop_test.domain.usecases.RefreshDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MyCardsUiState(
    val cards: List<CardModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeCards: ObserveCardsUseCase,
    observeUser: ObserveUserUseCase,
    private val refreshData: RefreshDataUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MyCardsUiState(isLoading = true))
    val state: StateFlow<MyCardsUiState> = _state

    init {
        refresh()

        viewModelScope.launch {
            combine(
                observeCards(),
                observeUser()
            ) { cards, user -> user to cards }
                .collect { (user, cards) ->
                    val filtered = user?.let { u -> cards.filter { it.userId == u.id } } ?: cards
                    _state.update { it.copy(cards = filtered, isLoading = false, error = null) }
                }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                refreshData()
            } catch (e: Exception) {
                _state.update { it.copy(error = e.localizedMessage) }
            }
        }
    }
}