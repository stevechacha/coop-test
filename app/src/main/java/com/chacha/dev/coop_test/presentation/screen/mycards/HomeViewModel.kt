package com.chacha.dev.coop_test.presentation.screen.mycards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chacha.dev.coop_test.domain.common.Resource
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


@HiltViewModel
class HomeViewModel @Inject constructor(
    observeCards: ObserveCardsUseCase,
    observeUser: ObserveUserUseCase,
    private val refreshData: RefreshDataUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MyCardsUiState(isLoading = true))
    val state: StateFlow<MyCardsUiState> = _state

    init {
        viewModelScope.launch {
            combine(
                observeCards(),
                observeUser()
            ) { cardsRes, userRes ->
                val cards = when (cardsRes) {
                    is Resource.Success -> cardsRes.data ?: emptyList()
                    is Resource.Error -> {
                        _state.update { it.copy(error = cardsRes.message) }
                        emptyList()
                    }
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                        emptyList()
                    }
                }
                val user = when (userRes) {
                    is Resource.Success -> userRes.data
                    is Resource.Error -> {
                        if (_state.value.error == null) {
                            _state.update { it.copy(error = userRes.message) }
                        }
                        null
                    }
                    is Resource.Loading -> null
                }
                user to cards
            }
                .collect { (user, cards) ->
                    val filtered = if (user != null) {
                        cards.filter { it.userId == user.id }
                    } else {
                        cards
                    }
                    _state.update {
                        it.copy(
                            cards = filtered,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = refreshData()) {
                is Resource.Success -> _state.update { it.copy(isLoading = false, error = null) }
                is Resource.Error -> _state.update {
                    it.copy(
                        error = result.message ?: "Failed to load cards",
                        isLoading = false
                    )
                }
                is Resource.Loading -> _state.update {
                    it.copy(isLoading = true)
                }
            }
        }
    }
}