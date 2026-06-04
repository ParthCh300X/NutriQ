package parth.appdev.nutriq.screens.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import parth.appdev.nutriq.domain.model.Food
import parth.appdev.nutriq.domain.model.RiskLevel
import parth.appdev.nutriq.domain.repository.FoodRepository
import parth.appdev.nutriq.presentation.common.UiState
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val repo: FoodRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<Food>>(UiState.Loading)
    val state: StateFlow<UiState<Food>> = _state.asStateFlow()

    init {
        val barcode: String = checkNotNull(savedStateHandle["barcode"])
        loadResult(barcode)
    }

    private fun loadResult(barcode: String) {
        viewModelScope.launch {
            repo.getHistory().collect { entities ->
                val entity = entities.firstOrNull { it.barcode == barcode }
                if (entity == null) {
                    _state.value = UiState.NotFound
                } else {
                    _state.value = UiState.Success(
                        Food(
                            barcode = entity.barcode,
                            name = entity.name,
                            ingredients = entity.ingredients,
                            riskLevel = try { RiskLevel.valueOf(entity.risk) } catch (e: Exception) { RiskLevel.UNKNOWN },
                            reasons = emptyList()
                        )
                    )
                }
            }
        }
    }
}