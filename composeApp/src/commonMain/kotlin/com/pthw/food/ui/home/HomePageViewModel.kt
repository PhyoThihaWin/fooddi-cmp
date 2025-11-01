package com.pthw.food.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pthw.food.expects.Logger
import com.pthw.food.domain.model.FilterType
import com.pthw.food.domain.model.Food
import com.pthw.food.domain.repository.CacheRepository
import com.pthw.food.domain.repository.FoodRepository
import fooddimultiplatform.composeapp.generated.resources.Res
import fooddimultiplatform.composeapp.generated.resources.app_name
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

data class UiState(
    val themeCode: String,
    val localeCode: String,
    val clickCountForAd: Int = 0,
    val pageTitle: StringResource = Res.string.app_name,
    val foods: List<Food> = emptyList()
)

sealed interface UiEvent {
    data class SearchFoods(val query: String) : UiEvent
    data class FilterFoods(val filterType: FilterType) : UiEvent
    data class ChangeLanguage(val localeCode: String) : UiEvent
    data class ChangeThemeMode(val theme: String) : UiEvent
    data object ResetClickCountAd : UiEvent
}

class HomePageViewModel(
    private val repository: FoodRepository,
    private val cacheRepository: CacheRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    var state = mutableStateOf(
        UiState(
            themeCode = cacheRepository.getThemeModeNormal(),
            localeCode = cacheRepository.getLanguageNormal(),
        )
    )
        private set


    init {
        observeThemeMode()
        observeLanguage()
        observeSearchFoods()
        loadAllFoods()
    }

    fun onEvent(event: UiEvent) {
        when (event) {
            is UiEvent.SearchFoods -> {
                incrementClickCount()
                searchQuery.value = event.query
            }

            is UiEvent.FilterFoods -> {
                incrementClickCount()
                if (event.filterType.type == null) loadAllFoods()
                else loadFoodsByType(event.filterType)
            }

            is UiEvent.ChangeLanguage -> {
                incrementClickCount()
                viewModelScope.launch {
                    cacheRepository.putLanguage(event.localeCode)
                }
            }

            is UiEvent.ChangeThemeMode -> {
                incrementClickCount()
                viewModelScope.launch {
                    cacheRepository.putThemeMode(event.theme)
                }
            }

            is UiEvent.ResetClickCountAd -> updateState(clickCountForAd = 0)
        }
    }

    // ----------------------------------------
    // 🥗 Data Fetchers
    // ----------------------------------------

    private fun loadAllFoods() = viewModelScope.launch {
        val data = repository.getAllFood()
        updateState(foods = data, pageTitle = Res.string.app_name)
    }

    private fun loadFoodsByType(filterType: FilterType) = viewModelScope.launch {
        val data = repository.getFoodByType(filterType.type.toString())
        updateState(foods = data, pageTitle = filterType.title)
    }

    // ----------------------------------------
    // 🧠 Observers
    // ----------------------------------------

    @OptIn(FlowPreview::class)
    private fun observeSearchFoods() = viewModelScope.launch {
        searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .collectLatest { query ->
                val data = repository.getSearchFood(query)
                updateState(foods = data)
            }
    }

    private fun observeLanguage() = viewModelScope.launch {
        cacheRepository.getLanguage().collectLatest { code ->
            updateState(localeCode = code)
        }
    }

    private fun observeThemeMode() = viewModelScope.launch {
        cacheRepository.getThemeMode().collectLatest { theme ->
            updateState(themeCode = theme)

        }
    }

    // ----------------------------------------
    // 📊 Ad Click Count
    // ----------------------------------------

    private fun incrementClickCount() {
        val newCount = state.value.clickCountForAd + 1
        updateState(clickCountForAd = newCount)
    }


    // ----------------------------------------
    // 🧱 Helpers
    // ----------------------------------------

    private fun updateState(
        localeCode: String = state.value.localeCode,
        themeCode: String = state.value.themeCode,
        foods: List<Food> = state.value.foods,
        pageTitle: StringResource = state.value.pageTitle,
        clickCountForAd: Int = state.value.clickCountForAd
    ) {
        Logger.i("foods: ${foods.count()}")
        state.value = state.value.copy(
            pageTitle = pageTitle,
            localeCode = localeCode,
            themeCode = themeCode,
            foods = foods,
            clickCountForAd = clickCountForAd
        )
    }
}
