package com.rdisoftware.chronobeat.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.GameSummaryScreen
import com.rdisoftware.chronobeat.presentation.dimensions.GameSummaryLocalDimensions
import com.rdisoftware.chronobeat.presentation.dimensions.LocalBaseDimensions
import com.rdisoftware.chronobeat.presentation.dimensions.PhoneGameSumDimensions
import com.rdisoftware.chronobeat.presentation.dimensions.TabletGameSumDimensions
import com.rdisoftware.chronobeat.presentation.dimensions.gameSumDimens
import com.rdisoftware.chronobeat.shared.resources.Res
import com.rdisoftware.chronobeat.shared.resources.content_disc_trophy_logo
import com.rdisoftware.chronobeat.shared.resources.game_summary_title
import com.rdisoftware.chronobeat.shared.resources.home
import com.rdisoftware.chronobeat.shared.resources.play_again
import com.rdisoftware.chronobeat.shared.resources.trophy
import com.rdisoftware.chronobeat.shared.resources.won_the_game
import com.rdisoftware.chronobeat.presentation.enums.ButtonSize
import com.rdisoftware.chronobeat.presentation.screens.components.GradientBackground
import com.rdisoftware.chronobeat.presentation.screens.components.GradientButton
import com.rdisoftware.chronobeat.presentation.screens.components.LogoText
import com.rdisoftware.chronobeat.presentation.screens.components.ScreenTitle
import com.rdisoftware.chronobeat.presentation.theme.kdamThmorProRegular
import com.rdisoftware.chronobeat.presentation.viewmodels.GameSummaryViewModel
import com.rdisoftware.chronobeat.theme.AppColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GameSummaryScreen(
    viewModel: GameSummaryViewModel = koinViewModel(),
    onHomeClicked: () -> Unit,
    onPlayAgainClicked: () -> Unit,
    onShowLeaderBoardClicked: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val dimensions = if (screenWidth >= 600.dp) TabletGameSumDimensions
    else PhoneGameSumDimensions

    CompositionLocalProvider(
        GameSummaryLocalDimensions provides dimensions,
        LocalBaseDimensions provides dimensions.base
    ) {

        GradientBackground()

        LogoText()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = gameSumDimens.base.maxContentWidth),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ScreenTitle(
                text = stringResource(Res.string.game_summary_title),
                testTag = GameSummaryScreen.GAME_SUMMARY_TITLE,
                resourceId = true
            )

            TrophyImage()

        DisplayWinner(
            winnerTeam = state.gameWinner?.name ?: ""
        )

            Column(
                modifier = Modifier
                    .widthIn(max = gameSumDimens.columnWidth)
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(gameSumDimens.spaceByPadding)

            ) {
                GradientButton(
                    text = stringResource(Res.string.home),
                    enabled = true,
                    size = ButtonSize.SMALL,
                    testTag = GameSummaryScreen.HOME_BUTTON,
                    resourceId = true,
                    onClick = {
                        onHomeClicked()
                    }
                )

                GradientButton(
                    text = stringResource(Res.string.play_again),
                    enabled = true,
                    size = ButtonSize.SMALL,
                    testTag = GameSummaryScreen.PLAY_AGAIN_BUTTON,
                    resourceId = true,
                    onClick = {
                        onPlayAgainClicked()
                    }
                )

                GradientButton(
                    text = "SHOW LEADERBOARD",
                    enabled = true,
                    size = ButtonSize.SMALL,
                    testTag = GameSummaryScreen.PLAY_AGAIN_BUTTON,
                    resourceId = true,
                    onClick = {
                        onShowLeaderBoardClicked()
                    }
                )
            }
        }
    }
}

@Composable
fun TrophyImage() {
    Image(
        modifier = Modifier
            .testTag(GameSummaryScreen.WIN_IMAGE)
            .semantics {
                testTagsAsResourceId = true
                role = Role.Image
            },
        painter = painterResource(Res.drawable.trophy),
        contentDescription = stringResource(Res.string.content_disc_trophy_logo)
    )
}

@Composable
fun DisplayWinner(winnerTeam: String) {
    Text(
        modifier = Modifier
            .padding(bottom = 6.dp)
            .testTag(GameSummaryScreen.TEAM_NAME_TEXT)
            .semantics {
                testTagsAsResourceId = true
            },
        text = winnerTeam,
        fontSize = gameSumDimens.winnerTextFontSize,
        color = Color(AppColors.WHITE),
        fontFamily = kdamThmorProRegular
    )

    Text(
        modifier = Modifier
            .padding(bottom = 24.dp)
            .testTag(GameSummaryScreen.WIN_TEXT)
            .semantics {
                testTagsAsResourceId = true
            },
        text = stringResource(Res.string.won_the_game),
        fontSize = 36.sp,
        color = Color(AppColors.WHITE),
        fontFamily = kdamThmorProRegular
    )
}