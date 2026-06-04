package parth.appdev.nutriq.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import parth.appdev.nutriq.data.local.entity.FoodEntity
import parth.appdev.nutriq.domain.repository.FoodRepository
import parth.appdev.nutriq.presentation.common.UiState
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repo: FoodRepository
) : ViewModel() {

    val historyState: StateFlow<UiState<List<FoodEntity>>> =
        repo.getHistory()
            .map { list ->
                if (list.isEmpty()) UiState.NotFound
                else UiState.Success(list)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState.Loading
            )
}