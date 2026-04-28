package com.rdisoftware.chronobeat.ui.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rdisoftware.chronobeat.shared.resources.Res
import com.rdisoftware.chronobeat.shared.resources.content_disc_trophy_logo
import com.rdisoftware.chronobeat.shared.resources.game_summary_title
import com.rdisoftware.chronobeat.shared.resources.home
import com.rdisoftware.chronobeat.shared.resources.play_again
import com.rdisoftware.chronobeat.shared.resources.trophy
import com.rdisoftware.chronobeat.ui.enums.ButtonSize
import com.rdisoftware.chronobeat.ui.screens.components.GradientBackground
import com.rdisoftware.chronobeat.ui.screens.components.GradientButton
import com.rdisoftware.chronobeat.ui.screens.components.LogoText
import com.rdisoftware.chronobeat.ui.theme.kdamThmorProRegular
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.GameSummaryScreen
import com.rdisoftware.chronobeat.shared.resources.team_name
import com.rdisoftware.chronobeat.shared.resources.won_the_game
import com.rdisoftware.chronobeat.ui.screens.components.ScreenTitle

@Composable
@Preview
fun GameSummaryScreen() {

    GradientBackground()

    LogoText()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenTitle(
            text = stringResource(Res.string.game_summary_title),
            testTag = GameSummaryScreen.GAME_SUMMARY_TITLE,
            resourceId = true
        )

        TrophyImage()

        DisplayWinner(
            winnerTeam = stringResource(Res.string.team_name)
        )

        GradientButton(
            text = stringResource(Res.string.home),
            enabled = true,
            size = ButtonSize.SMALL,
            testTag = GameSummaryScreen.HOME_BUTTON,
            resourceId = true,
            onClick = {} //TODO: Create "Start game" on click action
        )

        Spacer(modifier = Modifier.padding(12.dp))

        GradientButton(
            text = stringResource(Res.string.play_again),
            enabled = true,
            size = ButtonSize.SMALL,
            testTag = GameSummaryScreen.PLAY_AGAIN_BUTTON,
            resourceId = true,
            onClick = {} //TODO: Create "Start game" on click action
        )
    }
}

@Composable
fun TrophyImage(){
    Image(
        modifier = Modifier
            .testTag(GameSummaryScreen.WIN_IMAGE)
            .semantics{
                testTagsAsResourceId = true
                role = Role.Image
            },
        painter = painterResource(Res.drawable.trophy),
        contentDescription = stringResource(Res.string.content_disc_trophy_logo)
    )
}

@Composable
fun DisplayWinner(winnerTeam: String){
    Text(
        modifier = Modifier
            .padding(bottom = 6.dp)
            .testTag(GameSummaryScreen.TEAM_NAME_TEXT)
            .semantics{
            testTagsAsResourceId = true
        },
        text = winnerTeam,
        fontSize = 36.sp,
        color = Color.White,
        fontFamily = kdamThmorProRegular
    )

    Text(
        modifier = Modifier
            .padding(bottom = 24.dp)
            .testTag(GameSummaryScreen.WIN_TEXT)
            .semantics{
                testTagsAsResourceId = true
            },
        text = stringResource(Res.string.won_the_game),
        fontSize = 36.sp,
        color = Color.White,
        fontFamily = kdamThmorProRegular
    )
}