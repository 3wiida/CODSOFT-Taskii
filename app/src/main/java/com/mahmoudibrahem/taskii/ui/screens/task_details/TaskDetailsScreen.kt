package com.mahmoudibrahem.taskii.ui.screens.task_details

import androidx.compose.foundation.background
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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.mahmoudibrahem.taskii.R
import com.mahmoudibrahem.taskii.model.CheckItem
import com.mahmoudibrahem.taskii.model.Task
import com.mahmoudibrahem.taskii.ui.screens.home.CheckListItem
import com.mahmoudibrahem.taskii.ui.theme.AppMainColor
import com.mahmoudibrahem.taskii.ui.theme.AppSecondaryColor
import com.mahmoudibrahem.taskii.ui.theme.SfDisplay
import com.mahmoudibrahem.taskii.ui.theme.SfText

@Composable
fun TaskDetailsScreen(
    viewModel: TaskDetailsViewModel = hiltViewModel(),
    owner: LifecycleOwner = LocalLifecycleOwner.current,
    taskId: Int = 1,
    onNavigateBack: () -> Unit = {},
    onNavigateToEditTask: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    TaskDetailsScreenContent(
        uiState = uiState,
        onBackClicked = onNavigateBack,
        onCompleteCheckItem = viewModel::onCompleteTaskItem,
        onDeleteClicked = {
            viewModel.onDeleteTaskClicked()
            onNavigateBack()
        },
        onEditClicked = {
            onNavigateToEditTask(taskId)
        }
    )
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
private fun TaskDetailsScreenContent(
    uiState: TaskDetailsScreenUIState,
    onBackClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onEditClicked: () -> Unit,
    onCompleteCheckItem: (CheckItem, Boolean, Int) -> Unit
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            TaskDetailsHeader(
                onBackClicked = onBackClicked,
                onDeleteClicked = onDeleteClicked,
                onEditClicked = onEditClicked
            )
            Spacer(modifier = Modifier.height(24.dp))
            DetailsSection(task = uiState.task)
            Spacer(modifier = Modifier.height(24.dp))
            CheckListSection(
                headerText = stringResource(R.string.checklist_process),
                checkList = uiState.checkList,
                onCompleteCheckItem = onCompleteCheckItem
            )
        }
    }
}

@Composable
fun TaskDetailsHeader(
    onBackClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onEditClicked: () -> Unit
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

        Row(
            Modifier
                .align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy((-24).dp)
        ) {
            IconButton(
                onClick = onEditClicked
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.edit_ic),
                    contentDescription = null,
                    tint = AppMainColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(
                modifier = Modifier.offset(x = (16).dp),
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
                modifier = Modifier
                    .background(
                        color = Color(0xFFFFE8C8),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(4.dp),
                text = stringResource(
                    R.string.deadline,
                    task.deadline.month.name.lowercase().slice(0..2),
                    task.deadline.dayOfMonth,
                    task.deadline.hour,
                    task.deadline.minute
                ),
                color = AppMainColor,
                maxLines = 1,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
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

@Composable
fun CheckListSection(
    headerText: String,
    checkList: List<CheckItem>,
    onCompleteCheckItem: (CheckItem, Boolean, Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            item {
                Text(
                    text = headerText,
                    fontFamily = SfDisplay,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            items(checkList.size) { index ->
                CheckListItem(
                    item = checkList[index],
                    onCompleteCheckItem = { item, isCompleted ->
                        onCompleteCheckItem(
                            item,
                            isCompleted,
                            index
                        )
                    }
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TaskDetailsScreenPreview() {
    TaskDetailsScreen()
}


