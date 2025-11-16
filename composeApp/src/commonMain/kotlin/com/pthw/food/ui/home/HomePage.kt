package com.pthw.food.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import com.pthw.food.expects.LocalAppLocale
import com.pthw.food.expects.MetaBanner
import com.pthw.food.expects.MetaInterstitial
import com.pthw.food.ui.composable.CoilAsyncImage
import com.pthw.food.ui.composable.RadioSelectionDialog
import com.pthw.food.ui.composable.TitleTextView
import com.pthw.food.domain.model.AppThemeMode
import com.pthw.food.domain.model.FilterType
import com.pthw.food.domain.model.Food
import com.pthw.food.domain.model.Localization
import com.pthw.food.expects.Orientation
import com.pthw.food.expects.PlatformType
import com.pthw.food.expects.getPlatform
import com.pthw.food.ui.theme.ColorPrimary
import com.pthw.food.ui.theme.Dimens
import com.pthw.food.ui.theme.FoodDiAppTheme
import com.pthw.food.ui.theme.Shapes
import com.pthw.food.ui.theme.md_theme_dark_background
import com.pthw.food.utils.ConstantValue
import com.pthw.food.utils.ext.go
import fooddimultiplatform.composeapp.generated.resources.*
import io.github.alexzhirkevich.compottie.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

/**
 * Created by P.T.H.W on 16/07/2024.
 */
@Composable
fun HomePage() {
    val viewModel = koinViewModel<HomePageViewModel>()

    HomePageContent(viewModel.state.value, viewModel::onEvent)

    // interstitial ad
    if (viewModel.state.value.clickCountForAd > ConstantValue.INTERSTITIAL_COUNT) {
        LocalFocusManager.current.clearFocus()
        MetaInterstitial {
            viewModel.onEvent(UiEvent.ResetClickCountAd)
        }
    }
}


@OptIn(ExperimentalMotionApi::class)
@Composable
private fun HomePageContent(
    uiState: UiState,
    onEvent: (UiEvent) -> Unit = {}
) {
    val isDarkMode = AppThemeMode.isDarkMode(uiState.themeCode)
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val isScrolledOnTop by remember {
        derivedStateOf { scrollState.firstVisibleItemScrollOffset == 0 }
    }
    val progress by animateFloatAsState(
        targetValue = if (isScrolledOnTop) 0f else 1f,
        tween(500, easing = LinearOutSlowInEasing), label = ""
    )

    val uriHandler = LocalUriHandler.current
    val focusManager = LocalFocusManager.current
    var showFilterDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showAboutAppDialog by remember { mutableStateOf(false) }
    var showSheet by remember { mutableStateOf(false) }

    CompositionLocalProvider(
        LocalAppLocale provides uiState.localeCode,
    ) {
        key(uiState.localeCode) {
            Scaffold {
                Box(
                    modifier = Modifier
                        .background(if (isDarkMode) md_theme_dark_background else ColorPrimary)
                        .padding(top = it.calculateTopPadding())
                ) {
                    // Main layout
                    MotionLayout(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.surface)
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        start = startConstraintSet,
                        end = endConstraintSet,
                        progress = progress
                    ) {

                        Spacer(
                            modifier = Modifier
                                .layoutId("background")
                                .fillMaxWidth()
                                .fillMaxHeight(if (getPlatform().getCurrentOrientation() == Orientation.Portrait) 0.18f else 0.28f)
                                .background(color = if (isDarkMode) MaterialTheme.colorScheme.background else ColorPrimary)
                        )

                        Icon(
                            modifier = Modifier
                                .layoutId("setting")
                                .clip(CircleShape)
                                .clickable {
                                    showSheet = true
                                    focusManager.clearFocus()
                                }
                                .padding(Dimens.MARGIN_10),
                            painter = painterResource(Res.drawable.ic_round_menu),
                            tint = if (isDarkMode) ColorPrimary else Color.White,
                            contentDescription = ""
                        )

                        Icon(
                            modifier = Modifier
                                .layoutId("filter")
                                .clip(CircleShape)
                                .clickable {
                                    showFilterDialog = true
                                    focusManager.clearFocus()
                                }
                                .padding(Dimens.MARGIN_10),
                            painter = painterResource(Res.drawable.ic_filter_list),
                            tint = if (isDarkMode) ColorPrimary else Color.White,
                            contentDescription = ""
                        )

                        TitleTextView(
                            modifier = Modifier.layoutId("title"),
                            text = stringResource(uiState.pageTitle),
                            color = if (isDarkMode) ColorPrimary else Color.White,
                            fontSize = Dimens.TEXT_HEADING_2
                        )


                        // Item list
                        LazyColumn(
                            modifier = Modifier
                                .layoutId("list")
                                .fillMaxSize(),
                            contentPadding = PaddingValues(
                                top = Dimens.MARGIN_LARGE,
                                bottom = getPlatform().getScreenHeight() / 4
                            ),
                            state = scrollState,
                        ) {
                            items(uiState.foods) { item ->
                                FoodListItemView(localeCode = uiState.localeCode, food = item)
                            }
                        }


                        // searchBox
                        HomeSearchBarView(
                            modifier = Modifier.layoutId("search"),
                            hint = stringResource(Res.string.search),
                            iconClick = {
                                coroutineScope.launch {
                                    scrollState.animateScrollToItem(0)
                                }
                            },
                            onValueChange = {
                                onEvent(UiEvent.SearchFoods(it))
                            }
                        )

                        // Filter dialog
                        FilterDialog(
                            isShow = showFilterDialog,
                            onDismissRequest = {
                                it?.let {
                                    onEvent(UiEvent.FilterFoods(it))
                                }
                                showFilterDialog = false
                            }
                        )

                        // About Dialog
                        AboutAppDialog(showAboutAppDialog) {
                            showAboutAppDialog = false
                        }

                        // Language dialog
                        LanguageChooseDialog(
                            isShow = showLanguageDialog,
                            localeCode = uiState.localeCode
                        ) {
                            it?.let {
                                onEvent(UiEvent.ChangeLanguage(it.code))
                            }
                            showLanguageDialog = false
                        }

                        // Theme choose dialog
                        ThemeModeDialog(
                            isShow = showThemeDialog,
                            themeCode = uiState.themeCode
                        ) {
                            it?.let {
                                onEvent(UiEvent.ChangeThemeMode(it.themeCode))
                            }
                            showThemeDialog = false
                        }

                        // Setting bottom sheet
                        SettingModalSheet(showSheet) {
                            showSheet = false
                            when (it) {
                                1 -> showLanguageDialog = true
                                2 -> showThemeDialog = true
                                3 -> showAboutAppDialog = true
                                4 -> uriHandler.go(ConstantValue.citationUrl)
                                5 -> uriHandler.go(ConstantValue.moreAppUrl)
                            }
                        }

                    }

                    // banner ad
                    MetaBanner(
                        modifier = Modifier.align(Alignment.BottomCenter),
                    )
                }
            }
        }
    }
}

private val startConstraintSet = ConstraintSet {
    val background = createRefFor("background")
    constrain(background) {
        top.linkTo(parent.top)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
    }
    val setting = createRefFor("setting")
    constrain(setting) {
        top.linkTo(parent.top, Dimens.MARGIN_MEDIUM_2)
        start.linkTo(parent.start, Dimens.MARGIN_10)
    }
    val filter = createRefFor("filter")
    constrain(filter) {
        top.linkTo(parent.top, Dimens.MARGIN_MEDIUM_2)
        end.linkTo(parent.end, Dimens.MARGIN_10)
    }
    val title = createRefFor("title")
    constrain(title) {
        alpha = 1f
        scaleY = 1f
        scaleX = 1f
        top.linkTo(background.top)
        start.linkTo(background.start)
        end.linkTo(background.end)
        bottom.linkTo(background.bottom)
    }
    val search = createRefFor("search")
    constrain(search) {
        width = Dimension.fillToConstraints
        start.linkTo(parent.start, Dimens.MARGIN_20)
        end.linkTo(parent.end, Dimens.MARGIN_20)
        top.linkTo(background.bottom)
        bottom.linkTo(background.bottom)
    }
    val list = createRefFor("list")
    constrain(list) {
        height = Dimension.fillToConstraints
        top.linkTo(search.bottom)
        bottom.linkTo(parent.bottom)
    }
}
private val endConstraintSet = ConstraintSet {
    val background = createRefFor("background")
    constrain(background) {
        bottom.linkTo(parent.top)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
    }
    val setting = createRefFor("setting")
    constrain(setting) {
        bottom.linkTo(parent.top)
        end.linkTo(parent.start)
    }
    val filter = createRefFor("filter")
    constrain(filter) {
        bottom.linkTo(parent.top)
        start.linkTo(parent.end)
    }
    val title = createRefFor("title")
    constrain(title) {
        alpha = 0f
        scaleY = 0.6f
        scaleX = 0.6f
        top.linkTo(background.top)
        start.linkTo(background.start)
        end.linkTo(background.end)
        bottom.linkTo(background.bottom)
    }
    val search = createRefFor("search")
    constrain(search) {
        start.linkTo(parent.end, -56.dp)
        top.linkTo(parent.top, Dimens.MARGIN_MEDIUM_2)
    }
    val list = createRefFor("list")
    constrain(list) {
        height = Dimension.fillToConstraints
        top.linkTo(parent.top)
        bottom.linkTo(parent.bottom)
    }
}

@Composable
private fun FoodListItemView(
    localeCode: String,
    food: Food
) {
    Card(
        modifier = Modifier.padding(
            start = Dimens.MARGIN_20, end = Dimens.MARGIN_20,
            bottom = Dimens.MARGIN_MEDIUM_2
        ),
        shape = Shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens.MARGIN_XSMALL
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.MARGIN_LARGE, horizontal = Dimens.MARGIN_MEDIUM),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CoilAsyncImage(
                    image = food.imgOne,
                    modifier = Modifier
                        .size(Dimens.IMAGE_CARD_SIZE)
                        .clip(Shapes.medium)
                        .background(color = Color.White),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(Dimens.MARGIN_MEDIUM_2))
                Text(
                    text = food.getFoodOne(localeCode),
                    fontSize = Dimens.TEXT_REGULAR,
                    textAlign = TextAlign.Center
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(Dimens.MARGIN_SMALL))
                Text(text = "Vs", fontSize = Dimens.TEXT_XLARGE)
                Spacer(modifier = Modifier.height(Dimens.MARGIN_10))
                TitleTextView(
                    text = food.getFoodDie(localeCode),
                    color = ColorPrimary,
                    fontSize = Dimens.TEXT_REGULAR_2
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CoilAsyncImage(
                    image = food.imgTwo,
                    modifier = Modifier
                        .size(Dimens.IMAGE_CARD_SIZE)
                        .clip(Shapes.medium)
                        .background(color = Color.White),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(Dimens.MARGIN_MEDIUM_2))
                Text(
                    text = food.getFoodTwo(localeCode),
                    fontSize = Dimens.TEXT_REGULAR,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun HomeSearchBarView(
    modifier: Modifier,
    hint: String,
    iconClick: () -> Unit,
    onValueChange: (String) -> Unit
) {

    var textSearchBox by rememberSaveable { mutableStateOf("") }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens.MARGIN_XSMALL
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(Shapes.medium)
                .padding(horizontal = Dimens.MARGIN_MEDIUM)

        ) {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        iconClick()
                    }
                    .padding(Dimens.MARGIN_MEDIUM),
                painter = painterResource(Res.drawable.ic_search_normal),
                contentDescription = ""
            )

            TextField(
                value = textSearchBox,
                onValueChange = {
                    textSearchBox = it
                    onValueChange(it)
                },
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = Dimens.TEXT_REGULAR
                ),
                placeholder = {
                    Text(
                        text = hint,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        fontSize = Dimens.TEXT_REGULAR
                    )
                },
                trailingIcon = {
                    AnimatedVisibility(textSearchBox.length > 1) {
                        Icon(
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    textSearchBox = ""
                                    onValueChange("")
                                }
                                .padding(Dimens.MARGIN_MEDIUM),
                            painter = painterResource(Res.drawable.ic_delete_search),
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingModalSheet(
    isShow: Boolean,
    onDismiss: (id: Int) -> Unit
) {
    if (!isShow) return
    val modalBottomSheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = { onDismiss(999) },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        TitleTextView(
            modifier = Modifier.padding(horizontal = Dimens.MARGIN_20),
            text = stringResource(Res.string.setting),
            color = ColorPrimary
        )

        Spacer(modifier = Modifier.height(Dimens.MARGIN_MEDIUM_2))

        // manipulate setting items
        val settings = ConstantValue.settingList.toMutableList()
        if (getPlatform().type == PlatformType.iOS) {
            settings.removeAll { it.id == 3 }
        }

        settings.forEachIndexed { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDismiss(item.id) }
                    .padding(horizontal = Dimens.MARGIN_20),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.width(40.dp),
                    painter = painterResource(item.icon),
                    contentDescription = "",
                )
                Spacer(modifier = Modifier.width(Dimens.MARGIN_MEDIUM))
                Text(
                    text = stringResource(item.title),
                    modifier = Modifier.padding(16.dp),
                )
            }

            if (index == ConstantValue.settingList.lastIndex) {
                Spacer(modifier = Modifier.height(Dimens.MARGIN_XXXLARGE))
            } else {
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun FilterDialog(
    isShow: Boolean,
    onDismissRequest: (item: FilterType?) -> Unit,
) {
    if (!isShow) return
    Dialog(onDismissRequest = { onDismissRequest(null) }) {
        val composition by rememberLottieComposition {
            LottieCompositionSpec.JsonString(
                Res.readBytes("files/filter_loading.json").decodeToString()
            )
        }
        val progress by animateLottieCompositionAsState(
            composition = composition,
            restartOnPlay = true,
            iterations = Compottie.IterateForever,
        )


        Card(shape = Shapes.medium) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier.height(150.dp),
                    painter = rememberLottiePainter(
                        composition = composition,
                        progress = { progress },
                    ),
                    contentDescription = "Lottie animation"
                )

                ConstantValue.filterList.forEachIndexed { index, item ->
                    Column(
                        modifier = Modifier
                            .clickable {
                                onDismissRequest(item)
                            }
                            .padding(horizontal = Dimens.MARGIN_LARGE)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(Dimens.MARGIN_MEDIUM_2))
                            Text(
                                text = stringResource(item.title),
                                modifier = Modifier.padding(16.dp),
                            )
                        }
                        if (index == ConstantValue.filterList.lastIndex) {
                            Spacer(modifier = Modifier.height(Dimens.MARGIN_MEDIUM))
                        } else {
                            HorizontalDivider()
                        }
                    }
                }

            }
        }
    }
}


@Composable
private fun LanguageChooseDialog(
    isShow: Boolean,
    localeCode: String,
    onDismissRequest: (locale: Localization?) -> Unit,
) {
    if (!isShow) return
    RadioSelectionDialog(
        items = Localization.entries,
        onDismissRequest = onDismissRequest
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(56.dp)
                .selectable(
                    selected = (it.code == localeCode),
                    enabled = (it.code != localeCode),
                    onClick = {
                        onDismissRequest(it)
                    },
                    role = Role.RadioButton
                )
                .padding(horizontal = Dimens.MARGIN_20),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = (it.code == localeCode),
                onClick = null
            )
            Text(
                text = stringResource(it.title),
                modifier = Modifier.padding(start = Dimens.MARGIN_MEDIUM_2)
            )
        }
    }
}

@Composable
private fun ThemeModeDialog(
    isShow: Boolean,
    themeCode: String,
    onDismissRequest: (themeMode: AppThemeMode?) -> Unit,
) {
    if (!isShow) return
    RadioSelectionDialog(
        items = ConstantValue.appThemeModes,
        onDismissRequest = onDismissRequest
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(56.dp)
                .selectable(
                    selected = (it.themeCode == themeCode),
                    enabled = (it.themeCode != themeCode),
                    onClick = {
                        onDismissRequest(it)
                    },
                    role = Role.RadioButton
                )
                .padding(horizontal = Dimens.MARGIN_20),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = (it.themeCode == themeCode),
                onClick = null
            )
            Text(
                text = stringResource(it.title),
                modifier = Modifier.padding(start = Dimens.MARGIN_MEDIUM_2)
            )
        }
    }
}

@Composable
fun AboutAppDialog(
    isShow: Boolean,
    onDismissRequest: () -> Unit,
) {
    if (!isShow) return
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.wrapContentHeight(),
            shape = Shapes.medium
        ) {
            CoilAsyncImage(
                modifier = Modifier.fillMaxWidth(),
                image = Res.getUri("drawable/img_fooddi_landscape.png"),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview()
@Composable
private fun HomePagePreview() {
    FoodDiAppTheme {
        Surface {
            HomePageContent(
                uiState = UiState(
                    themeCode = AppThemeMode.LIGHT_MODE,
                    localeCode = Localization.English.code,
                    foods = Food.fakeList()
                )
            )
        }
    }
}

@Preview()
@Composable
private fun HomePagePreviewNight() {
    FoodDiAppTheme(true) {
        Surface {
            HomePageContent(
                uiState = UiState(
                    themeCode = AppThemeMode.DARK_MODE,
                    localeCode = Localization.English.code,
                    foods = Food.fakeList()
                )
            )
        }
    }
}

@Preview()
@Composable
private fun SettingModalSheetPreview() {
    FoodDiAppTheme {
        Surface {
            SettingModalSheet(true) {

            }
        }
    }
}

@Preview()
@Composable
private fun FilterDialogPreview() {
    FoodDiAppTheme {
        Surface {
            FilterDialog(
                isShow = true,
                onDismissRequest = {},
            )
        }
    }
}

@Preview()
@Composable
private fun LanguageChooseDialogPreview() {
    FoodDiAppTheme {
        Surface {
            LanguageChooseDialog(
                isShow = true,
                localeCode = Localization.English.code
            ) {}
        }
    }
}

@Preview()
@Composable
private fun ThemeModeDialogPreview() {
    FoodDiAppTheme {
        Surface {
            ThemeModeDialog(
                isShow = true,
                themeCode = AppThemeMode.LIGHT_MODE,
            ) {}
        }
    }
}

@Preview
@Composable
private fun AboutAppDialogPreview() {
    FoodDiAppTheme {
        Surface {
            AboutAppDialog(true) {
            }
        }
    }
}