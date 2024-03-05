package com.mahmoudibrahem.taskii.ui.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mahmoudibrahem.taskii.R
import com.mahmoudibrahem.taskii.model.Task
import com.mahmoudibrahem.taskii.ui.theme.AppSecondaryColor
import com.mahmoudibrahem.taskii.ui.theme.LightTextColor
import com.mahmoudibrahem.taskii.ui.theme.SfDisplay
import kotlinx.coroutines.delay

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateToTaskDetails: (Task) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    SearchScreenContent(
        uiState = uiState,
        onQueryChanged = viewModel::onQueryChanged,
        onClearClicked = viewModel::onClearClicked,
        onSearchClicked = viewModel::onSearchClicked,
        onBackClicked = onNavigateBack,
        onResultClicked = onNavigateToTaskDetails
    )
    LaunchedEffect(key1 = uiState.query) {
        delay(1000)
        viewModel.searchForTasks(uiState.query)
    }
}

@Composable
private fun SearchScreenContent(
    uiState: SearchScreenUIState,
    onQueryChanged: (String) -> Unit,
    onClearClicked: () -> Unit,
    onSearchClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onResultClicked: (Task) -> Unit
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp, horizontal = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            SearchScreenHeader(onBackClicked = onBackClicked)
            Spacer(modifier = Modifier.height(16.dp))
            SearchWidget(
                searchQuery = uiState.query,
                onQueryChanged = onQueryChanged,
                onClearClicked = onClearClicked,
                onSearchClicked = onSearchClicked
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Results (${uiState.results.size})",
                fontFamily = SfDisplay,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedVisibility(
                visible = uiState.results.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut(animationSpec = tween(durationMillis = 500))
            ) {
                ResultsSection(
                    resultsList = uiState.results,
                    onResultClicked = onResultClicked
                )
            }
            AnimatedVisibility(
                visible = uiState.results.isEmpty() && uiState.showEmptyState,
                enter = fadeIn(animationSpec = tween(delayMillis = 500)),
                exit = fadeOut()
            ){
                EmptyState()
            }
        }
    }
}

@Composable
fun SearchScreenHeader(
    onBackClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
    ) {
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (-16).dp),
            onClick = { onBackClicked() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back_ic),
                contentDescription = null,
                tint = AppSecondaryColor
            )
        }
        Text(
            text = stringResource(R.string.search),
            fontFamily = SfDisplay,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun SearchWidget(
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    onClearClicked: () -> Unit,
    onSearchClicked: () -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { onQueryChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .background(color = Color(0xFFEAE9F6), shape = RoundedCornerShape(16.dp)),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.search_ic),
                contentDescription = null,
                tint = Color(0xFFC2B6CF)
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = searchQuery.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(
                    onClick = { onClearClicked() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.x_ic),
                        contentDescription = null,
                        tint = AppSecondaryColor
                    )
                }
            }
        },
        textStyle = TextStyle(
            color = Color.Black,
            fontFamily = SfDisplay,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Start
        ),
        placeholder = {
            Text(
                text = stringResource(R.string.search_placeholder),
                color = Color(0xFFC2B6CF),
                fontFamily = SfDisplay,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearchClicked() })
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResultsSection(
    resultsList: List<Task> = emptyList(),
    onResultClicked: (Task) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(resultsList.size) { index ->
            Row(
                modifier = Modifier
                    .animateItemPlacement()
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onResultClicked(resultsList[index])
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(id = R.drawable.search_result_icon),
                    contentDescription = "Result Icon"
                )
                Text(
                    text = resultsList[index].name,
                    color = Color(0xFF52465F),
                    fontFamily = SfDisplay,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun SearchEmptyState(
    resultsList: List<Task> = emptyList(),
    isShowEmptyState: Boolean
) {
    AnimatedVisibility(
        visible = (resultsList.isEmpty() && isShowEmptyState),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.empty_search),
                contentDescription = null,
                modifier = Modifier.size(240.dp)
            )
            Text(
                text = stringResource(R.string.no_have_result_please_try_again),
                color = Color(0xFFC2B6CF),
                fontFamily = SfDisplay,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.search_empty_anim)
            )
            LottieAnimation(
                modifier = Modifier.size(250.dp),
                composition = composition,
                iterations = LottieConstants.IterateForever
            )
            Text(
                text = "No results found with your search query",
                fontFamily = SfDisplay,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
                text = "Try again with another query",
                fontFamily = SfDisplay,
                fontSize = 16.sp,
                color = LightTextColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SearchScreenPreview() {
    SearchScreen()
}