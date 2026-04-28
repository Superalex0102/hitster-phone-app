package com.rdisoftware.chronobeat.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.HomeScreen
import com.rdisoftware.chronobeat.ui.enums.ButtonSize
import com.rdisoftware.chronobeat.ui.screens.components.BottomText
import com.rdisoftware.chronobeat.ui.screens.components.GradientBackground
import com.rdisoftware.chronobeat.ui.screens.components.GradientButton
import com.rdisoftware.chronobeat.ui.theme.kdamThmorProRegular
import com.rdisoftware.chronobeat.shared.resources.Res
import com.rdisoftware.chronobeat.shared.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GradientBackground()

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
            .padding(top= 16.dp, end = 8.dp)
            .testTag(HomeScreen.SETTINGS_BUTTON)
            .semantics{
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
            .semantics{
                testTagsAsResourceId = true
            }
    )
}