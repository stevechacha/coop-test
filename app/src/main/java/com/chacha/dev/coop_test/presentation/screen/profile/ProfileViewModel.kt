package com.chacha.dev.coop_test.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chacha.dev.coop_test.domain.common.Resource
import com.chacha.dev.coop_test.domain.model.UserModel
import com.chacha.dev.coop_test.domain.usecases.ObserveUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class ProfileViewModel @Inject constructor(
    observeUser: ObserveUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state

    init {
        viewModelScope.launch {
            observeUser().collect { userRes ->
                when (userRes) {
                    is Resource.Success -> {
                        _state.update { it.copy(user = userRes.data, isLoading = false) }
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(isLoading = false, error = userRes.message) }
                    }
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }
}

