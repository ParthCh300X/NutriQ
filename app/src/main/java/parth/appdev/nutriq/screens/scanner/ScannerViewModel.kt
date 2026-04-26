package parth.appdev.nutriq.presentation.screens.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import parth.appdev.nutriq.data.repository.FoodRepositoryImpl
import parth.appdev.nutriq.domain.model.Food
import parth.appdev.nutriq.domain.usecase.AnalyzeFoodUseCase

@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val repo: FoodRepositoryImpl
) : ViewModel() {

    private val analyzer = AnalyzeFoodUseCase()

    private val _uiState = MutableStateFlow<Food?>(null)
    val uiState: StateFlow<Food?> = _uiState

    fun fetchProduct(barcode: String) {
        viewModelScope.launch {
            val response = repo.getFood(barcode)
            if (response.isSuccessful) {
                val product = response.body()?.product

                val result = analyzer.execute(
                    product?.product_name,
                    product?.ingredients_text
                )

                _uiState.value = result
                repo.saveFood(result)
            }
        }
    }

    fun reset() {
        _uiState.value = null
    }
}