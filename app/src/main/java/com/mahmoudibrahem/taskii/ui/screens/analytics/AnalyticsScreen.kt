package com.mahmoudibrahem.taskii.ui.screens.analytics

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.mahmoudibrahem.taskii.model.Task
import com.mahmoudibrahem.taskii.navigation.screens.HomeScreens
import com.mahmoudibrahem.taskii.ui.screens.home.AppBottomBar
import com.mahmoudibrahem.taskii.ui.theme.AppGreenColor
import com.mahmoudibrahem.taskii.ui.theme.AppMainColor
import com.mahmoudibrahem.taskii.ui.theme.AppSecondaryColor
import com.mahmoudibrahem.taskii.ui.theme.SfDisplay
import com.mahmoudibrahem.taskii.ui.theme.SfText
import com.mahmoudibrahem.taskii.util.shadow
import kotlin.math.cos
import kotlin.math.sin

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = hiltViewModel(),
    navController: NavController,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    Scaffold(
        bottomBar = {
            AppBottomBar(
                selectedScreen = 1,
                onAddClicked = {
                    navController.navigate(HomeScreens.CreateTask.route) {
                        launchSingleTop = true
                    }
                },
                onHomeClicked = {
                    navController.navigate(HomeScreens.Home.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.Start
        ) {
            var selectedIndex by remember { mutableIntStateOf(0) }

            Text(
                text = "Dashboard",
                fontFamily = SfDisplay,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
            Spacer(modifier = Modifier.height(32.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {

                item {
                    AnalyticsCircle {
                        return@AnalyticsCircle if ((viewModel.completedTasksList.size + viewModel.unCompletedTasksList.size) != 0) {
                            viewModel.completedTasksList.size / (viewModel.completedTasksList.size + viewModel.unCompletedTasksList.size).toFloat()
                        } else {
                            1f
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Text(
                        text = "Project activity",
                        fontFamily = SfDisplay,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        itemsIndexed(items = listOf("Done", "To do")) { index, item ->
                            ClickableText(
                                text = AnnotatedString(
                                    text = item,
                                    spanStyle = SpanStyle(
                                        color = animateColorAsState(
                                            targetValue = if (index == selectedIndex) AppSecondaryColor else Color.LightGray,
                                            label = ""
                                        ).value,
                                        fontFamily = SfDisplay,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                ),
                                onClick = {
                                    {}
                                    selectedIndex = index
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (selectedIndex == 0) {
                    items(count = viewModel.completedTasksList.size) { index ->
                        DashboardTaskItem(
                            task = viewModel.completedTasksList[index]
                        ) {
                            navController.navigate(
                                route = HomeScreens.TaskDetails.route.replace(
                                    "{task_id}",
                                    it.id.toString()
                                )
                            )
                        }
                    }
                } else {
                    items(count = viewModel.unCompletedTasksList.size) { index ->
                        DashboardTaskItem(task = viewModel.unCompletedTasksList[index]) {
                            navController.navigate(
                                route = HomeScreens.TaskDetails.route.replace(
                                    "{task_id}",
                                    it.id.toString()
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                viewModel.getCompletedTasks()
                viewModel.getUnCompletedTasks()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

}

@Composable
fun AnalyticsCircle(
    doneProgressStateProvider: () -> Float = { 0.97f },
) {
    val sweepAngle1 = remember { Animatable(initialValue = 0f) }
    val sweepAngle2 = remember { Animatable(initialValue = 0f) }
    val donePercentage = remember { Animatable(initialValue = 0f) }
    val todoPercentage = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(key1 = null) {
        sweepAngle1.animateTo(
            targetValue = (doneProgressStateProvider() * 360),
            animationSpec = tween(durationMillis = 1000)
        )
    }
    LaunchedEffect(key1 = null) {
        sweepAngle2.animateTo(
            targetValue = (1 - doneProgressStateProvider()) * 360,
            animationSpec = tween(durationMillis = 1000)
        )
    }
    LaunchedEffect(key1 = null) {
        donePercentage.animateTo(
            targetValue = (doneProgressStateProvider() * 100),
            animationSpec = tween(1000)
        )
    }
    LaunchedEffect(key1 = null) {
        todoPercentage.animateTo(
            targetValue = ((1 - doneProgressStateProvider()) * 100),
            animationSpec = tween(1000)
        )
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Canvas(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(200.dp)
                    .height(200.dp)
                    .graphicsLayer {
                        compositingStrategy = CompositingStrategy.Offscreen
                    }
                    .drawWithContent {
                        drawContent()
                        if (doneProgressStateProvider() > 0f && doneProgressStateProvider() < 1.0f) {
                            drawLine(
                                color = Color.Black,
                                start = Offset(x = 100.dp.toPx(), y = 0.dp.toPx()),
                                end = Offset(x = 100.dp.toPx(), y = 100.dp.toPx()),
                                strokeWidth = 20f,
                                blendMode = BlendMode.Clear
                            )
                        }
                        drawCircle(
                            color = Color.Black,
                            radius = 70f,
                            blendMode = BlendMode.Clear
                        )
                    }
            ) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = 320f
                val startAngle1 = -90f
                val startAngle2 = (doneProgressStateProvider() * 360) - 90f

                drawArc(
                    color = AppGreenColor,
                    startAngle = startAngle1,
                    sweepAngle = sweepAngle1.value,
                    useCenter = true,
                )

                drawArc(
                    color = AppMainColor,
                    startAngle = startAngle2,
                    sweepAngle = sweepAngle2.value,
                    useCenter = true
                )

                val offsetAngle = startAngle1 + sweepAngle1.value
                val offsetRadians = Math.toRadians(offsetAngle.toDouble())
                val xOffset = center.x + (radius * cos(offsetRadians)).toFloat()
                val yOffset = center.y + (radius * sin(offsetRadians)).toFloat()

                if (doneProgressStateProvider() > 0f && doneProgressStateProvider() < 1.0f) {
                    drawLine(
                        color = Color.Red,
                        start = center,
                        end = Offset(xOffset, yOffset),
                        strokeWidth = 20f,
                        blendMode = BlendMode.Clear
                    )
                }

            }
            Column {
                Text(
                    text = "${donePercentage.value.toInt()}%",
                    modifier = Modifier
                        .width(50.dp)
                        .background(color = AppGreenColor, shape = RoundedCornerShape(4.dp))
                        .padding(4.dp),
                    color = Color.White,
                    fontFamily = SfDisplay,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "${todoPercentage.value.toInt()}%",
                    modifier = Modifier
                        .width(50.dp)
                        .background(color = AppMainColor, shape = RoundedCornerShape(4.dp))
                        .padding(4.dp),
                    color = Color.White,
                    fontFamily = SfDisplay,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }

        }

        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Canvas(modifier = Modifier.size(12.dp)) {
                    drawCircle(color = AppGreenColor)
                }
                Text(
                    text = "Done",
                    fontFamily = SfDisplay,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Canvas(modifier = Modifier.size(12.dp)) {
                    drawCircle(color = AppMainColor)
                }
                Text(
                    text = "To do",
                    fontFamily = SfDisplay,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0x66DFDFDF))
        )
    }
}

@Composable
fun DashboardTaskItem(
    task: Task,
    onTaskClicked: (Task) -> Unit
) {
    val progressCount = remember { Animatable(initialValue = 0f) }
    val progressArc = remember { Animatable(initialValue = 0f) }

    LaunchedEffect(key1 = null) {
        progressCount.animateTo(targetValue = task.progress * 100, animationSpec = tween(1500))
    }
    LaunchedEffect(key1 = null) {
        progressArc.animateTo(targetValue = task.progress * 360f, animationSpec = tween(1500))
    }
    Row(
        modifier = Modifier
            .shadow(
                color=Color.Gray.copy(alpha = 0.1f),
                borderRadius = 8.dp,
                blurRadius = 64.dp
            )
            .padding(vertical = 8.dp)

            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFFFFF))
            .padding(vertical = 12.dp, horizontal = 12.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onTaskClicked(task)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.weight(3f)
        ) {
            Text(
                text = task.name,
                fontFamily = SfDisplay,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = task.description,
                fontFamily = SfText,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Color(0xFF52465F),
                modifier = Modifier.padding(bottom = 10.dp, end = 10.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = "Deadline: ${
                    task.deadline.month.name.lowercase().slice(0..2)
                } ${task.deadline.dayOfMonth} at ${task.deadline.hour}:${task.deadline.minute}",
                color = AppMainColor,
                maxLines = 1,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(
                        color = Color(0xFFFFE8C8),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(4.dp)
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(72.dp)
        ) {
            Text(
                text = "${progressCount.value.toInt()}%",
                fontFamily = SfDisplay,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = AppGreenColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = 3.dp, y = 2.dp),
                textAlign = TextAlign.Center
            )
            Canvas(modifier = Modifier.size(48.dp)) {
                drawArc(
                    color = Color(0x33CCCCCC),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    size = Size(width = 150f, height = 150f),
                    style = Stroke(width = 10f, cap = StrokeCap.Round)
                )
                drawArc(
                    color = AppGreenColor,
                    startAngle = -90f,
                    sweepAngle = progressArc.value,
                    useCenter = false,
                    size = Size(width = 150f, height = 150f),
                    style = Stroke(width = 10f, cap = StrokeCap.Round)
                )
            }
        }


    }
}