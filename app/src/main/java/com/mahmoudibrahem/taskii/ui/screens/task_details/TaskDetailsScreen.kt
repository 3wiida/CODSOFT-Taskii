package com.mahmoudibrahem.taskii.ui.screens.task_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.mahmoudibrahem.taskii.R
import com.mahmoudibrahem.taskii.model.Task
import com.mahmoudibrahem.taskii.ui.screens.home.CheckListSection
import com.mahmoudibrahem.taskii.ui.theme.AppMainColor
import com.mahmoudibrahem.taskii.ui.theme.AppSecondaryColor
import com.mahmoudibrahem.taskii.ui.theme.SfDisplay
import com.mahmoudibrahem.taskii.ui.theme.SfText

@Composable
fun TaskDetailsScreen(
    navController: NavController,
    viewModel: TaskDetailsViewModel = hiltViewModel(),
    owner: LifecycleOwner = LocalLifecycleOwner.current,
    taskId: Int
) {
    Surface{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            TaskDetailsHeader(
                onBackClicked = { navController.navigateUp() },
                onDeleteClicked = {
                    viewModel.deleteTask()
                    navController.navigateUp()
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            DetailsSection(task = viewModel.task.collectAsState().value)
            Spacer(modifier = Modifier.height(24.dp))
            CheckListSection(
                headerText = "Checklist process",
                checkList = viewModel.checkList,
                onCompleteCheckItem = { checkItem, isCompleted, index ->
                    viewModel.saveTaskProcess(
                        checkItem = checkItem,
                        checkItemIndex = index,
                        isCheckItemCompleted = isCompleted
                    )
                }
            )
        }
    }
    DisposableEffect(key1 = owner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                viewModel.getTaskById(taskId)
                viewModel.getCheckListByTaskId(taskId)
            }
        }
        owner.lifecycle.addObserver(observer)
        onDispose {
            owner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun TaskDetailsHeader(
    onBackClicked: () -> Unit,
    onDeleteClicked: () -> Unit
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
            text = "Task Details",
            fontFamily = SfDisplay,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )
        IconButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = (16).dp),
            onClick = { onDeleteClicked() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.delete_ic),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun DetailsSection(
    task: Task?
) {
    if (task != null) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = task.name,
                fontFamily = SfDisplay,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = task.description,
                fontFamily = SfText,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color(0xFF52465F),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Deadline: ${
                    task.deadline.month.name.lowercase().slice(0..2)
                } ${task.deadline.dayOfMonth} at ${task.deadline.hour}:${task.deadline.minute}",
                color = AppMainColor,
                maxLines = 1,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(
                        color = Color(0xFFFFE8C8),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(4.dp)
            )
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .background(Color(0xFFEAE9F6))
            )
        }
    }
}
