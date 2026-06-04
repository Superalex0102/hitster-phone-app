package com.rdisoftware.chronobeat.presentation.screens


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.HomeScreen
import com.rdisoftware.chronobeat.presentation.enums.ButtonSize
import com.rdisoftware.chronobeat.presentation.screens.components.BottomText
import com.rdisoftware.chronobeat.presentation.screens.components.GradientBackground
import com.rdisoftware.chronobeat.presentation.screens.components.GradientButton
import com.rdisoftware.chronobeat.presentation.theme.kdamThmorProRegular
import com.rdisoftware.chronobeat.shared.resources.Res
import com.rdisoftware.chronobeat.shared.resources.*
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.LoginPopup
import com.rdisoftware.chronobeat.presentation.dimensions.HomeLocalDimensions
import com.rdisoftware.chronobeat.presentation.dimensions.LocalBaseDimensions
import com.rdisoftware.chronobeat.presentation.dimensions.PhoneHomeDimensions
import com.rdisoftware.chronobeat.presentation.dimensions.TabletHomeDimensions
import com.rdisoftware.chronobeat.presentation.dimensions.homeDimens
import com.rdisoftware.chronobeat.presentation.theme.robotoMonoRegular
import com.rdisoftware.chronobeat.presentation.viewmodels.HomeEvent
import com.rdisoftware.chronobeat.presentation.viewmodels.HomeUiState
import com.rdisoftware.chronobeat.presentation.viewmodels.HomeViewModel
import com.rdisoftware.chronobeat.theme.AppColors
import kotlinx.coroutines.coroutineScope
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onLocalGameClicked: (shouldLoadSave: Boolean) -> Unit,
    onSettingsClicked: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    LaunchedEffect(Unit) {
        viewModel.checkSpotifyAuthentication()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val dimensions = if (screenWidth >= 600.dp) TabletHomeDimensions
        else PhoneHomeDimensions
        CompositionLocalProvider(
            HomeLocalDimensions provides dimensions,
            LocalBaseDimensions provides dimensions.base
        ) {

            GradientBackground()

            if (state.showLoginPopup) {
                LoginPopup(
                    state = state,
                    onDismissRequest = {
                        viewModel.onEvent(event = HomeEvent.OnLoginPopupDismiss)
                    }
                )
            }
            if (state.showResumePopup) {
                SimpleResumeGamePopup(
                    onConfirm = { viewModel.onEvent(HomeEvent.OnResumeConfirm(navigate = onLocalGameClicked)) },
                    onDiscard = { viewModel.onEvent(HomeEvent.OnResumeDiscard) })
            }


            SettingsButton(
                onClick = {
                    onSettingsClicked()
                }
            ) //TODO: Settings on click action

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = homeDimens.base.maxContentWidth),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(homeDimens.spacerTopWeight))

                MainTitle()

                Spacer(modifier = Modifier.weight(homeDimens.spacerMiddleWeight))

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(homeDimens.basePadding)
                ) {
                    GradientButton(
                        text = stringResource(Res.string.local_game),
                        enabled = state.isLocalEnabled,
                        size = ButtonSize.LARGE,
                        testTag = HomeScreen.LOCAL_GAME_BUTTON,
                        resourceId = true,
                        onClick = {
                            viewModel.resetGame()
                            onLocalGameClicked(false)
                        }
                    )

                    GradientButton(
                        text = stringResource(Res.string.online_game),
                        enabled = state.isOnlineEnabled,
                        size = ButtonSize.LARGE,
                        testTag = HomeScreen.ONLINE_GAME_BUTTON,
                        resourceId = true,
                        onClick = {
                            viewModel.onEvent(event = HomeEvent.OnOnlineGameClick)
                        }
                        //TODO: Online game mode on click action - not in current scope
                    )
                }

                Spacer(modifier = Modifier.weight(homeDimens.spacerBottomWeight))

                BottomText(
                    text = stringResource(Res.string.powered_by),
                    name = stringResource(Res.string.bottom_app_name)
                )
            }
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
            .padding(top = homeDimens.screenEdgePadding, end = homeDimens.screenEdgePadding)
            .testTag(HomeScreen.SETTINGS_BUTTON)
            .semantics {
                testTagsAsResourceId = true
                role = Role.Button
            }
    ) {
        Icon(
            imageVector = Icons.Outlined.Settings,
            contentDescription = stringResource(Res.string.settings),
            tint = Color(AppColors.WHITE),
            modifier = Modifier
                .size(homeDimens.settingsIconSize)
        )
    }
}

@Composable
fun MainTitle() {
    Text(
        text = stringResource(Res.string.title),
        fontSize = homeDimens.base.titleFontSize,
        color = Color(AppColors.WHITE),
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
    viewModel: HomeViewModel = koinViewModel(),
    state: HomeUiState,
    onDismissRequest: () -> Unit,
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
                .widthIn(homeDimens.popupMaxContentWidth)
                .fillMaxWidth(homeDimens.base.contentWidthFraction)
                .height(IntrinsicSize.Min)
                .clip(RoundedCornerShape(5))
                .border(
                    width = 2.dp,
                    color = Color(AppColors.WHITE),
                    shape = RoundedCornerShape(5)
                ),
        ) {

            GradientBackground()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(homeDimens.basePadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = homeDimens.basePadding)
            ) {
                PopupTitle(
                    text = stringResource(Res.string.sing_in_to_spotify),
                    testTag = LoginPopup.LOGIN_POPUP_TITLE,
                    resourceId = true
                )

                GradientButton(
                    text = stringResource(Res.string.sign_in),
                    enabled = true,
                    size = ButtonSize.LARGE,
                    testTag = LoginPopup.SIGN_IN_BUTTON,
                    resourceId = true,
                    onClick = { viewModel.onEvent(HomeEvent.OnSignInClick) }
                )

                ErrorText(message = state.loginMessage)
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
        fontSize = homeDimens.popupTitleFontSize,
        lineHeight = homeDimens.popupTitleLineHeight,
        textAlign = TextAlign.Center,
        color = Color(AppColors.WHITE),
        fontFamily = kdamThmorProRegular,
        modifier = Modifier
            .testTag(testTag)
            .semantics {
                testTagsAsResourceId = resourceId
            }
    )
}

@Composable
fun ErrorText(message: StringResource?) {
    if (message == null) return

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Error,
            contentDescription = stringResource(Res.string.content_disc_error),
            tint = Color(AppColors.RED),
            modifier = Modifier
                .testTag(LoginPopup.ERROR_ICON)
                .semantics {
                    testTagsAsResourceId = true
                }
        )

        Text(
            text = stringResource(message), // TODO: Create dynamic text
            color = Color(AppColors.RED),
            fontFamily = robotoMonoRegular,
            fontSize = homeDimens.errorTextFontSize,
            modifier = Modifier
                .testTag(LoginPopup.ERROR_TEXT)
                .semantics {
                    testTagsAsResourceId = true
                }
        )
    }
}

@Composable
fun SimpleResumeGamePopup(
    onConfirm: () -> Unit,
    onDiscard: () -> Unit
) {
    Popup(
        alignment = Alignment.Center,
        properties = PopupProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = Modifier
                .widthIn(min = 280.dp, max = 400.dp)
                .padding(24.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "You left behind a game in progess")

                Text(text = "Do you want to continue?")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDiscard) {
                        Text("New Game!")
                    }
                    TextButton(onClick = onConfirm) {
                        Text("Continue!")
                    }
                }
            }
        }
    }
}