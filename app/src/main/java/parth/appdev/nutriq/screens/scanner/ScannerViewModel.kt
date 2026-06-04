package parth.appdev.nutriq.screens.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import parth.appdev.nutriq.domain.model.Food
import parth.appdev.nutriq.domain.repository.FoodRepository
import parth.appdev.nutriq.domain.usecase.AnalyzeFoodUseCase
import parth.appdev.nutriq.presentation.common.UiState
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val repo: FoodRepository,
    private val analyzeFoodUseCase: AnalyzeFoodUseCase
) : ViewModel() {

    private val _scanState = MutableStateFlow<UiState<Food>>(UiState.Idle)
    val scanState: StateFlow<UiState<Food>> = _scanState.asStateFlow()

    fun fetchProduct(barcode: String) {
        viewModelScope.launch {
            _scanState.value = UiState.Loading

            try {
                val response = repo.getFood(barcode)

                if (response.isSuccessful) {
                    val product = response.body()?.product

                    if (product == null) {
                        _scanState.value = UiState.NotFound
                        return@launch
                    }

                    val result = analyzeFoodUseCase.execute(
                        barcode = barcode,
                        name = product.product_name,
                        ingredients = product.ingredients_text
                    )

                    repo.saveFood(result)
                    _scanState.value = UiState.Success(result)

                } else {
                    _scanState.value = UiState.Error(
                        "Server error ${response.code()}: ${response.message()}"
                    )
                }

            } catch (e: java.io.IOException) {
                _scanState.value = UiState.Error("No internet connection. Please check your network.")
            } catch (e: Exception) {
                _scanState.value = UiState.Error("Something went wrong. Please try again.")
            }
        }
    }

    fun reset() {
        _scanState.value = UiState.Idle
    }
}