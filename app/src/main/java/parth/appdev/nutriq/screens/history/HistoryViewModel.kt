package parth.appdev.nutriq.presentation.screens.history

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import parth.appdev.nutriq.data.repository.FoodRepositoryImpl
import androidx.lifecycle.viewModelScope

@HiltViewModel
class HistoryViewModel @Inject constructor(
    repo: FoodRepositoryImpl
) : ViewModel() {

    val history: StateFlow<List<HistoryItem>> =
        repo.getHistory()
            .map { list ->
                list.map {
                    HistoryItem(
                        name = it.name,
                        risk = it.risk
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
}

data class HistoryItem(
    val name: String,
    val risk: String
)