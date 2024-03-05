package com.mahmoudibrahem.taskii.ui.screens.user_naming

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudibrahem.taskii.R
import com.mahmoudibrahem.taskii.ui.theme.AppMainColor
import com.mahmoudibrahem.taskii.ui.theme.SfDisplay
import com.mahmoudibrahem.taskii.ui.theme.TextFieldColor

@Composable
fun UserNamingScreen(
    viewModel: UserNamingViewModel = hiltViewModel(),
    onNavigateToHomeScreen: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    UserNamingScreenContent(
        uiState = uiState,
        onValueChanged = viewModel::onNameValueChanged,
        onConfirmBtnClicked = {
            viewModel.onConfirmBtnClicked()
            onNavigateToHomeScreen()
        }
    )
}

@Composable
fun UserNamingScreenContent(
    uiState: UserNamingUIState,
    onValueChanged: (String) -> Unit,
    onConfirmBtnClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(
                top = 40.dp,
                start = 24.dp,
                end = 24.dp,
                bottom = 24.dp
            )
            .scrollable(state = rememberScrollableState { it }, orientation = Orientation.Vertical)
    ) {
        Column(modifier = Modifier.align(Alignment.TopStart)) {
            ScreenHeader()
            NameEntrySection(username = uiState.username, onValueChanged = onValueChanged)
        }
        ConfirmSection(
            modifier = Modifier.align(Alignment.BottomCenter),
            username = uiState.username,
            onConfirmBtnClicked = onConfirmBtnClicked
        )
    }
}

@Composable
private fun ScreenHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .size(120.dp),
            painter = painterResource(id = R.drawable.taskii_icon),
            contentDescription = null,
            alignment = Alignment.TopStart
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp),
            text = stringResource(R.string.welcome_to_taskii),
            fontFamily = SfDisplay,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = Color.Black,
            textAlign = TextAlign.Start
        )
        Text(
            modifier = Modifier.padding(bottom = 48.dp),
            text = stringResource(R.string.naming_screen_body),
            fontFamily = SfDisplay,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = Color(0xFF52465F),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun NameEntrySection(
    modifier: Modifier = Modifier,
    username: String,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(16.dp)),
        value = username,
        onValueChange = onValueChanged,
        textStyle = TextStyle(fontFamily = SfDisplay),
        placeholder = {
            Text(
                text = stringResource(R.string.tap_to_enter_your_name),
                fontFamily = SfDisplay
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedBorderColor = TextFieldColor,
            unfocusedBorderColor = TextFieldColor,
            focusedPlaceholderColor = Color.Gray,
            unfocusedPlaceholderColor = Color.Gray,
            focusedContainerColor = TextFieldColor,
            unfocusedContainerColor = TextFieldColor
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
    )
}

@Composable
private fun ConfirmSection(
    username: String,
    modifier: Modifier = Modifier,
    onConfirmBtnClicked: () -> Unit
) {
    val buttonColor = animateColorAsState(
        targetValue = if (username.length > 2) AppMainColor else Color.LightGray,
        animationSpec = tween(1000, easing = LinearEasing),
        label = ""
    )
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor.value),
        enabled = username.length > 3,
        onClick = onConfirmBtnClicked
    ) {
        Text(
            text = stringResource(R.string.confirm),
            fontFamily = SfDisplay,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun UserNamingScreenPreview() {
    UserNamingScreen()
}