package com.rdisoftware.chronobeat.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.HomeScreen
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.LoginPopup
import com.rdisoftware.chronobeat.shared.resources.*
import com.rdisoftware.chronobeat.ui.enums.ButtonSize
import com.rdisoftware.chronobeat.ui.screens.components.BottomText
import com.rdisoftware.chronobeat.ui.screens.components.GradientBackground
import com.rdisoftware.chronobeat.ui.screens.components.GradientButton
import com.rdisoftware.chronobeat.ui.theme.kdamThmorProRegular
import com.rdisoftware.chronobeat.ui.theme.robotoMonoRegular
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen() {
    var showPopup by remember { mutableStateOf(value = true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GradientBackground()

        if (showPopup) {
            LoginPopup(
                onDismissRequest = { showPopup = false } //TODO: Move popup visibility to VM
            )
        }

        SettingsButton(onClick = {}) //TODO: Settings on click action

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1.5f))

            MainTitle()

            Spacer(modifier = Modifier.weight(2f))

            Column(
                modifier = Modifier
                    .padding(top = 32.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                GradientButton(
                    text = stringResource(Res.string.local_game),
                    enabled = true,
                    size = ButtonSize.LARGE,
                    testTag = HomeScreen.LOCAL_GAME_BUTTON,
                    resourceId = true,
                    onClick = {} //TODO: Local game mode on click action
                )

                GradientButton(
                    text = stringResource(Res.string.online_game),
                    enabled = false,
                    size = ButtonSize.LARGE,
                    testTag = HomeScreen.ONLINE_GAME_BUTTON,
                    resourceId = true,
                    onClick = {} //TODO: Online game mode on click action - not in current scope
                )
            }

            Spacer(modifier = Modifier.weight(4f))

            BottomText(
                text = stringResource(Res.string.powered_by),
                name = stringResource(Res.string.bottom_app_name)
            )
        }
    }
}

@Composable
fun BoxScope.SettingsButton(
    onClick: () -> Unit
) {
    IconButton(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(top = 16.dp, end = 8.dp)
            .testTag(HomeScreen.SETTINGS_BUTTON)
            .semantics {
                testTagsAsResourceId = true
                role = Role.Button
            }
    ) {
        Icon(
            imageVector = Icons.Outlined.Settings,
            contentDescription = stringResource(Res.string.settings),
            tint = Color.White,
            modifier = Modifier
                .size(34.dp)
        )
    }
}

@Composable
fun MainTitle() {
    Text(
        text = stringResource(Res.string.title),
        fontSize = 48.sp,
        color = Color.White,
        fontFamily = kdamThmorProRegular,
        modifier = Modifier
            .padding(top = 24.dp)
            .testTag(HomeScreen.CHRONOBEAT_TITLE)
            .semantics {
                testTagsAsResourceId = true
            }
    )
}

@Composable
fun LoginPopup(
    onDismissRequest: () -> Unit
) {
    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismissRequest,
        properties = PopupProperties(
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
            .widthIn(400.dp)
            .fillMaxWidth(0.80f)
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(5))
            .border(
                width = 2.dp,
                color = Color.White,
                shape = RoundedCornerShape(5)
            ),
        ) {
            Box(
                modifier = Modifier
                .clip(RoundedCornerShape(5))){

            GradientBackground()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PopupTitle(
                text = stringResource(Res.string.sing_in_to_spotify),
                testTag = LoginPopup.LOGIN_POPUP_TITLE,
                true
            )

            Spacer(modifier = Modifier.height(48.dp))

            GradientButton(
                text = stringResource(Res.string.sign_in),
                enabled = true,
                size = ButtonSize.LARGE,
                testTag = LoginPopup.SIGN_IN_BUTTON,
                resourceId = true,
                onClick = {} //TODO: Sign in on click action
            )

            Spacer(modifier = Modifier.height(48.dp))

            ErrorText()
        }
            }
    }
    }
}

@Composable
fun PopupTitle(
    text: String,
    testTag: String,
    resourceId: Boolean
) {
    Text(
        text = text,
        fontSize = 28.sp,
        lineHeight = 40.sp,
        textAlign = TextAlign.Center,
        color = Color.White,
        fontFamily = kdamThmorProRegular,
        modifier = Modifier
            .testTag(testTag)
            .semantics {
                testTagsAsResourceId = resourceId
            }
    )
}

@Composable
fun ErrorText() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Error,
            contentDescription = stringResource(Res.string.content_disc_error),
            tint = Color.Red,
            modifier = Modifier
                .testTag(LoginPopup.ERROR_ICON)
                .semantics {
                    testTagsAsResourceId = true
                }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = stringResource(Res.string.error_message), // TODO: Create dynamic text
            color = Color.Red,
            fontFamily = robotoMonoRegular,
            fontSize = 14.sp,
            modifier = Modifier
                .testTag(LoginPopup.ERROR_TEXT)
                .semantics {
                    testTagsAsResourceId = true
                }
        )
    }
}