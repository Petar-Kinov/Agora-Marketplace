package com.example.agora.domain.messaging.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.agora.data.messaging.repository.ChatsRepository

@Suppress("UNCHECKED_CAST")
class ChatsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatsViewModel::class.java)) {
            return ChatsViewModel(
                chatsRepository = ChatsRepository()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
