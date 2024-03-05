package com.mahmoudibrahem.taskii.ui.screens.onboarding

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.mahmoudibrahem.taskii.R
import com.mahmoudibrahem.taskii.pojo.OnboardingPage
import com.mahmoudibrahem.taskii.ui.theme.SfDisplay
import com.mahmoudibrahem.taskii.ui.theme.AppMainColor
import com.mahmoudibrahem.taskii.ui.theme.AppSecondaryColor
import com.mahmoudibrahem.taskii.ui.theme.SfText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onNavigateToNameScreen: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    OnboardingScreenContent(
        pagerState = rememberPagerState { uiState.pages.size },
        uiState = uiState,
        onSkipBtnClicked = onNavigateToNameScreen,
        onStartBtnClicked = onNavigateToNameScreen
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreenContent(
    pagerState: PagerState,
    uiState: OnboardingUIState,
    scope: CoroutineScope = rememberCoroutineScope(),
    onSkipBtnClicked: () -> Unit,
    onStartBtnClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 24.dp,
                start = 28.dp,
                end = 28.dp,
                bottom = 0.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OnboardingHeader(
            currentPage = pagerState.currentPage,
            onSkipBtnClicked = onSkipBtnClicked,
            onBackBtnClicked = {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            }
        )

        OnboardingPager(
            pages = uiState.pages,
            pagerState = pagerState
        )

        HorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount = uiState.pages.size,
            activeColor = AppSecondaryColor,
            inactiveColor = Color.LightGray,
        )

        OnboardingFooter(
            currentPage = pagerState.currentPage,
            onStartBtnClicked = onStartBtnClicked
        )

    }
}

@Composable
fun OnboardingHeader(
    currentPage: Int,
    onBackBtnClicked: () -> Unit,
    onSkipBtnClicked: () -> Unit,
) {
    val skipButtonScale by animateFloatAsState(
        targetValue = if (currentPage == 2) 0f else 1f,
        animationSpec = tween(500),
        label = ""
    )
    val backButtonScale by animateFloatAsState(
        targetValue = if (currentPage == 0) 0f else 1f,
        animationSpec = tween(500),
        label = ""
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .scale(backButtonScale)
                .background(
                    color = AppMainColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
                .align(Alignment.CenterStart)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onBackBtnClicked()
                }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back_ic),
                contentDescription = stringResource(R.string.back),
                tint = Color.White
            )
        }

        ClickableText(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .scale(skipButtonScale),
            text = AnnotatedString(stringResource(R.string.skip)),
            onClick = { onSkipBtnClicked() },
            style = TextStyle(
                color = AppMainColor,
                fontSize = 20.sp,
                fontFamily = SfDisplay,
                fontWeight = FontWeight.Normal
            )
        )

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingPager(
    pages: List<OnboardingPage>,
    pagerState: PagerState
) {
    HorizontalPager(state = pagerState) { index ->
        OnboardingPage(onboardingPage = pages[index])
    }
}

@Composable
fun OnboardingPage(
    onboardingPage: OnboardingPage
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = onboardingPage.image),
            contentDescription = stringResource(R.string.onboarding_image),
            modifier = Modifier.size(270.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = onboardingPage.headText,
            fontSize = 28.sp,
            fontFamily = SfDisplay,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = onboardingPage.bodyText,
            fontSize = 16.sp,
            fontFamily = SfText,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun CircularProgressIndicator(
    currentPage: Int,
    circleIndicatorScale: Float,
    arcProgress: Float
) {

    Box(
        modifier = Modifier
            .size(80.dp)
            .scale(circleIndicatorScale)
            .drawBehind {
                drawArc(
                    color = Color.LightGray,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 5f, cap = StrokeCap.Round)
                )
                drawArc(
                    color = AppMainColor,
                    startAngle = -90f,
                    sweepAngle = arcProgress,
                    useCenter = false,
                    style = Stroke(width = 5f, cap = StrokeCap.Round)
                )
            }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .background(
                    AppMainColor,
                    CircleShape
                )
                .size(48.dp)
                .wrapContentSize(align = Alignment.Center),
            text = (currentPage + 1).toString(),
            color = Color.White,
            fontFamily = SfDisplay,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OnboardingFooter(
    currentPage: Int,
    onStartBtnClicked: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

        val transition = updateTransition(targetState = currentPage, label = "")

        val buttonScale = transition.animateFloat(
            label = "",
            transitionSpec = {
                tween(
                    durationMillis = 250,
                    delayMillis = if (this.targetState == 2) 750 else 0
                )
            }
        ) { currentPage ->
            if (currentPage == 2) 1f else 0f
        }

        val arcProgress = transition.animateFloat(
            label = "",
            transitionSpec = {
                tween(durationMillis = 500)
            }
        ) { currentPage ->
            (360f * (currentPage + 1) / 3)
        }

        val circularIndicatorScale = transition.animateFloat(
            label = "",
            transitionSpec = {
                tween(
                    durationMillis = 250,
                    delayMillis = if (this.targetState == 2) 500 else 250
                )
            }
        ) { currentPage ->
            if (currentPage == 2) 0f else 1f
        }

        CircularProgressIndicator(
            currentPage = currentPage,
            circleIndicatorScale = circularIndicatorScale.value,
            arcProgress = arcProgress.value
        )

        Button(
            onClick = {
                /*navController.navigate(route = OnboardingScreens.NamingUserScreen.route) {
                    popUpTo(OnboardingScreens.OnboardingScreen.route) {
                        inclusive = true
                    }
                }*/
                onStartBtnClicked()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .scale(buttonScale.value),
            colors = ButtonDefaults.buttonColors(backgroundColor = AppMainColor)
        ) {
            Text(
                text = stringResource(R.string.get_started),
                color = Color.White,
                fontFamily = SfDisplay,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}