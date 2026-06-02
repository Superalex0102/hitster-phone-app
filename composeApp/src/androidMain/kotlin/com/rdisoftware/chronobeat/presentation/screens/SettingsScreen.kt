package com.rdisoftware.chronobeat.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds
import com.rdisoftware.chronobeat.presentation.dimensions.HomeLocalDimensions
import com.rdisoftware.chronobeat.presentation.dimensions.LocalBaseDimensions
import com.rdisoftware.chronobeat.presentation.dimensions.PhoneHomeDimensions
import com.rdisoftware.chronobeat.presentation.dimensions.TabletHomeDimensions
import com.rdisoftware.chronobeat.presentation.dimensions.homeDimens
import com.rdisoftware.chronobeat.presentation.enums.ButtonSize
import com.rdisoftware.chronobeat.presentation.screens.components.BottomText
import com.rdisoftware.chronobeat.presentation.screens.components.GradientBackground
import com.rdisoftware.chronobeat.presentation.screens.components.GradientButton
import com.rdisoftware.chronobeat.presentation.screens.components.ScreenTitle
import com.rdisoftware.chronobeat.presentation.theme.kdamThmorProRegular
import com.rdisoftware.chronobeat.presentation.theme.robotoMonoBold
import com.rdisoftware.chronobeat.presentation.theme.robotoMonoRegular
import com.rdisoftware.chronobeat.shared.resources.Res
import com.rdisoftware.chronobeat.shared.resources.bottom_app_name
import com.rdisoftware.chronobeat.shared.resources.powered_by
import com.rdisoftware.chronobeat.shared.resources.title
import com.rdisoftware.chronobeat.theme.AppColors
import org.jetbrains.compose.resources.stringResource

@Preview
@Composable
fun SettingsScreen() {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val dimensions = if (screenWidth >= 600.dp) TabletHomeDimensions
        else PhoneHomeDimensions
        CompositionLocalProvider(
            HomeLocalDimensions provides dimensions,
            LocalBaseDimensions provides dimensions.base
        ) {
            GradientBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = homeDimens.base.maxContentWidth),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

            ) {
                ScreenTitle(text = "Settings", testTag = "Settings", resourceId = true)
                Spacer(modifier = Modifier.padding(bottom = 50.dp))

                Row(
                    modifier = Modifier
                    .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                    Text(
                        text = "Dark Mode",
                        fontSize = 32.sp,
                        color = Color(AppColors.WHITE),
                        fontFamily = robotoMonoBold

                    )

                    Checkbox(
                        true,
                        onCheckedChange = null,
                        enabled = true
                    )
                }
                Row(modifier = Modifier
                    .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                    Text(
                        text = "Notification",
                        fontSize = 32.sp,
                        color = Color(AppColors.WHITE),
                        fontFamily = robotoMonoBold
                    )

                    Checkbox(
                        true,
                        onCheckedChange = null,
                        enabled = true
                    )
                }
                Text(
                    text = "Supported languages",
                    fontSize = 32.sp,
                    color = Color(AppColors.WHITE),
                    fontFamily = robotoMonoBold
                )

                Column(
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 50.dp),
                ) {
                    Text(
                        text = "Magyar",
                        fontSize = 24.sp,
                        color = Color(AppColors.WHITE),
                        fontFamily = robotoMonoRegular
                    )

                    Text(
                        text = "English",
                        fontSize = 24.sp,
                        color = Color(AppColors.WHITE),
                        fontFamily = robotoMonoRegular)

                    Text(text = "Deutsch",
                        fontSize = 24.sp,
                        color = Color(AppColors.WHITE),
                        fontFamily = robotoMonoRegular
                    )
                }

                GradientButton(
                    text = "Save",
                    enabled = true,
                    size = ButtonSize.SMALL,
                    testTag = "asd",
                    resourceId = true,
                    onClick = { //TODO: Implement onClick save
                    }
                )
                BottomText(
                    text = stringResource(Res.string.powered_by),
                    name = stringResource(Res.string.bottom_app_name)
                )
            }

        }
    }
}