package com.rdisoftware.chronobeat.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rdisoftware.chronobeat.presentation.constans.LanguageConstants
import com.rdisoftware.chronobeat.presentation.dimensions.*
import com.rdisoftware.chronobeat.presentation.enums.ButtonSize
import com.rdisoftware.chronobeat.presentation.screens.components.BottomText
import com.rdisoftware.chronobeat.presentation.screens.components.GradientBackground
import com.rdisoftware.chronobeat.presentation.screens.components.GradientButton
import com.rdisoftware.chronobeat.presentation.screens.components.ScreenTitle
import com.rdisoftware.chronobeat.presentation.theme.robotoMonoBold
import com.rdisoftware.chronobeat.presentation.theme.robotoMonoRegular
import com.rdisoftware.chronobeat.shared.resources.*
import org.jetbrains.compose.resources.stringResource
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.SettingsScreen
import com.rdisoftware.chronobeat.theme.AppColors

@Composable
fun SettingsScreen(
    onSaveClicked: () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    var darkMode by remember { mutableStateOf(false) }
    var notificationEnabled by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf(LanguageConstants.all.first()) }

    Box(modifier = Modifier.fillMaxSize()) {

        val dimensions =
            if (screenWidth >= 600.dp) TabletHomeDimensions
            else PhoneHomeDimensions

        CompositionLocalProvider(
            HomeLocalDimensions provides dimensions,
            LocalBaseDimensions provides dimensions.base
        ) {

            GradientBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 12.dp)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ScreenTitle(
                    text = stringResource(Res.string.settings),
                    testTag = SettingsScreen.SETTINGS_TITLE,
                    resourceId = true
                )

                Spacer(Modifier.height(24.dp))

                ToggleSettings(
                    text = stringResource(Res.string.dark_mode),
                    textTestTag = SettingsScreen.DARK_MODE_TEXT,
                    textResourceId = true,
                    checked = darkMode,
                    checkBoxTestTag = SettingsScreen.DARK_MODE_TOGGLE,
                    checkboxResourceId = true,
                    onToggleChange = { darkMode = it }
                )

                CustomDivider()

                ToggleSettings(
                    text = stringResource(Res.string.notifications),
                    textTestTag = SettingsScreen.NOTIFICATIONS_TEXT,
                    textResourceId = true,
                    checked = notificationEnabled,
                    checkBoxTestTag = SettingsScreen.DARK_MODE_TOGGLE,
                    checkboxResourceId = true,
                    onToggleChange = { notificationEnabled = it }
                )

                CustomDivider()

                Text(
                    text = stringResource(Res.string.supported_languages),
                    fontSize = 30.sp,
                    color = Color(AppColors.WHITE),
                    fontFamily = robotoMonoBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 20.dp)
                        .testTag(SettingsScreen.SUPPORTED_LANGUAGES_TEXT)
                        .semantics {
                            testTagsAsResourceId = true
                        }
                )

                CustomDivider()

                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .align(Alignment.CenterHorizontally)
                ) {
                    LanguageConstants.all.forEach { language ->
                        LanguageItem(
                            text = stringResource(language.label),
                            selected = selectedLanguage == language,
                            testTag = language.testTag,
                            resourceId = true,
                            onClick = { selectedLanguage = language }
                        )

                        HorizontalDivider(color = Color(AppColors.WHITE).copy(0.5f))
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                GradientButton(
                    text = stringResource(Res.string.save),
                    enabled = true,
                    size = ButtonSize.SMALL,
                    testTag = SettingsScreen.SAVE_BUTTON,
                    resourceId = true,
                    onClick = { onSaveClicked() }
                )

                Spacer(modifier = Modifier.height(48.dp))

                BottomText(
                    text = stringResource(Res.string.powered_by),
                    name = stringResource(Res.string.bottom_app_name)
                )
            }
        }
    }
}

@Composable
fun CustomToggle(
    checked: Boolean,
    testTag: String,
    resourceId: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .height(36.dp)
            .width(72.dp)
            .background(
                color = if (checked) {
                    Color(AppColors.GAME_GRAY)
                } else {
                    Color(AppColors.BLACK)
                },
                shape = RoundedCornerShape(50)
            )
            .border(
                width = 2.dp,
                color = if (checked)
                    Color(AppColors.WHITE).copy(alpha = 0.8f)
                else
                    Color(AppColors.GAME_GRAY).copy(alpha = 0.6f),
                shape = RoundedCornerShape(50)
            )
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 4.dp, horizontal = 6.dp)
            .testTag(testTag)
            .semantics {
                testTagsAsResourceId = resourceId
                role = Role.Checkbox
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (checked) {
            Arrangement.End
        } else {
            Arrangement.Start
        }
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = if (checked) {
                        Color(AppColors.BLACK)
                    } else {
                        Color(AppColors.CHECK_BOX_GRAY)
                    },
                    shape = androidx.compose.foundation.shape.CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = if (checked) Icons.Outlined.Check else Icons.Outlined.Close,
                contentDescription = if (checked) {
                    stringResource(Res.string.active_toggle)
                } else {
                    stringResource(Res.string.inactive_toggle)
                },
                tint = if (checked) {
                    Color(AppColors.WHITE)
                } else {
                    Color(AppColors.BLACK)
                },
                modifier = Modifier.size(12.dp)
            )

        }
    }
}

@Composable
fun LanguageItem(
    text: String,
    selected: Boolean,
    testTag: String,
    resourceId: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp)
            .testTag(testTag)
            .semantics {
                testTagsAsResourceId = resourceId
                role = Role.Button
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            color = Color(AppColors.WHITE),
            fontFamily = robotoMonoRegular
        )

        Spacer(Modifier.width(12.dp))

        if (selected) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = stringResource(Res.string.selected_element_icon),
                tint = Color(AppColors.WHITE)
            )
        }
    }
}

@Composable
fun CustomDivider() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color(AppColors.WHITE).copy(0.5f)
    )
}

@Composable
fun ToggleSettings(
    text: String,
    textTestTag: String,
    textResourceId: Boolean,
    checked: Boolean,
    checkBoxTestTag: String,
    checkboxResourceId: Boolean,
    onToggleChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 32.sp,
            color = Color(AppColors.WHITE),
            fontFamily = robotoMonoBold,
            modifier = Modifier
                .testTag(textTestTag)
                .semantics {
                    testTagsAsResourceId = textResourceId
                }
        )

        CustomToggle(
            checked = checked,
            testTag = checkBoxTestTag,
            resourceId = checkboxResourceId,
            onCheckedChange = { onToggleChange(it) }
        )
    }
}