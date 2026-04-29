package com.rdisoftware.chronobeat.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.GameScreen.ARROW_LATEST_TEXT
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.GameScreen.ARROW_OLDEST_TEXT
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.GameScreen.CARD_COUNT_TEXT
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.GameScreen.GAME_CARD
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.GameScreen.GAME_CARD_ARTIST
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.GameScreen.GAME_CARD_CONTRIBUTOR
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.GameScreen.GAME_CARD_TITLE
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.GameScreen.GAME_CARD_YEAR
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.GameScreen.GAME_HEADER
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.GameScreen.GUESS_BUTTON
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.GameScreen.MUSIC_PLAYER_ICON
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.GameScreen.TEAM_NAME_TEXT
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.GameScreen.TIME_LINE_ARROW
import com.rdisoftware.chronobeat.shared.resources.Res
import com.rdisoftware.chronobeat.shared.resources.arrow_latest_text
import com.rdisoftware.chronobeat.shared.resources.arrow_oldest_text
import com.rdisoftware.chronobeat.theme.AppColors
import com.rdisoftware.chronobeat.ui.model.HeaderModel
import com.rdisoftware.chronobeat.ui.model.SongModel
import com.rdisoftware.chronobeat.ui.preview.MockHeaderData
import com.rdisoftware.chronobeat.ui.preview.MockMusicData.songs
import com.rdisoftware.chronobeat.ui.screens.components.GradientBackground
import com.rdisoftware.chronobeat.ui.theme.robotoMonoBold
import com.rdisoftware.chronobeat.ui.theme.robotoMonoLightItalic
import com.rdisoftware.chronobeat.ui.theme.robotoMonoMedium
import com.rdisoftware.chronobeat.ui.theme.robotoMonoRegular
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.random.Random

@Preview
@Composable
fun GameScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GradientBackground()

        TimeLineArrow()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            GameHeader()

            GameSurface()
        }
    }
}

@Composable
fun GameHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TeamInformation(MockHeaderData.data.first()) //Mock data for testing

        AnimatedSoundWaves(
            isAnimating = MockHeaderData.data.first().isMusicOn //Mock data for testing
        )
    }
}

@Composable
fun TeamInformation(
    model: HeaderModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.87f)
            .fillMaxHeight(0.07f)
            .border(
                width = 2.dp,
                color = Color.White,
                shape = RoundedCornerShape(30)
            )
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF48A0B7), //placeholder - viewModel function will compute this color value
                        Color.Black
                    )
                ),
                shape = RoundedCornerShape(30)
            )
            .padding(start = 16.dp)
            .testTag(GAME_HEADER)
            .semantics{
                testTagsAsResourceId = true
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GameText(
            text = model.teamName,
            fontSize = 32.sp,
            fontFamily = robotoMonoBold,
            color = Color.White,
            testTag = TEAM_NAME_TEXT
        )

        NumberCard(cardCount = "5") //Temporary constant value
    }
}

@Composable
fun NumberCard(
    cardCount: String
) {
    Box(
        modifier = Modifier
            .width(52.dp)
            .fillMaxHeight(1f)
            .clip(RoundedCornerShape(30))
            .background(Color.Transparent)
            .border(2.dp, Color.White.copy(alpha = 0.7f), RoundedCornerShape(30)),
        contentAlignment = Alignment.Center
    ) {
        GameText(
            text = cardCount,
            fontSize = 24.sp,
            fontFamily = robotoMonoRegular,
            color = Color.White,
            testTag = CARD_COUNT_TEXT
        )
    }
}

@Composable
fun AnimatedSoundWaves(
    isAnimating: Boolean,
    modifier: Modifier = Modifier,
    barColor: Color = Color.White
) {
    val barWidth = 3.dp
    val defaultHeight = 4.dp

    val minHeight = 6.dp
    val minMaxHeightDp = 20
    val maxMaxHeightDp = 40
    val minDelayMs = 0
    val maxDelayMs = 300

    Row(
        modifier = modifier
            .height(30.dp)
            .testTag(MUSIC_PLAYER_ICON)
            .semantics{
                testTagsAsResourceId = true
            },
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until 6) {
            val heightAnimatable = remember { Animatable(minHeight.value) }

            LaunchedEffect(isAnimating) {
                if (isAnimating) {
                    delay(Random.nextInt(minDelayMs, maxDelayMs).toLong())

                    while (isActive) {
                        val randomMaxHeight = Random.nextInt(minMaxHeightDp, maxMaxHeightDp)
                        heightAnimatable.animateTo(
                            targetValue = randomMaxHeight.toFloat(),
                            animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
                        )
                        heightAnimatable.animateTo(
                            targetValue = minHeight.value,
                            animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
                        )
                    }
                } else {
                    heightAnimatable.animateTo(
                        targetValue = defaultHeight.value,
                        animationSpec = tween(durationMillis = 300, easing = FastOutLinearInEasing)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(barWidth)
                    .height(heightAnimatable.value.dp)
                    .clip(CircleShape)
                    .background(barColor)
            )
        }
    }
}

@Composable
fun GameSurface() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //TEMPORARY TEST SOLUTION
        for (i in 0..songs.size) {

            item {
                GuessButton {
                    //TODO: OnClick action
                }
            }

            if (i < songs.size) {
                item {
                    GameCard(model = songs[i])
                }
            }
        }
    }
}

@Composable
fun GuessButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .testTag(GUESS_BUTTON)
            .semantics{
                testTagsAsResourceId = true
                role = Role.Button
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    shape = CircleShape,
                    color = AppColors.GameGray.copy(alpha = 0.6f)
                )
        )

        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    shape = CircleShape,
                    color = AppColors.GameGray
                )
        )
    }
}

@Composable
fun GameCard(
    model: SongModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 46.dp, vertical = 32.dp)
            .border(
                width = 3.dp,
                color = Color(0xFF48A0B7), //placeholder - viewModel function will compute this color value
                shape = RoundedCornerShape(12)
            )
            .testTag(GAME_CARD)
            .semantics{
                testTagsAsResourceId = true
            },
        backgroundColor = AppColors.GameGray,
        shape = RoundedCornerShape(12)
    ) {
        GameCardContent(model = model)
    }
}

@Composable
fun GameCardContent(
    model: SongModel
) {
    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameText(
            text = model.artist,
            fontSize = 28.sp,
            fontFamily = robotoMonoMedium,
            color = Color.Black,
            testTag = GAME_CARD_ARTIST
        )

        model.contributor?.let {
            GameText(
                text = it,
                fontSize = 12.sp,
                fontFamily = robotoMonoLightItalic,
                color = Color.Black,
                testTag = GAME_CARD_CONTRIBUTOR
            )
        }

        GameText(
            text = model.year,
            fontSize = 64.sp,
            fontFamily = robotoMonoBold,
            color = Color.Black,
            testTag = GAME_CARD_YEAR
        )

        GameText(
            text = model.title,
            fontSize = 20.sp,
            fontFamily = robotoMonoLightItalic,
            color = Color.Black,
            testTag = GAME_CARD_TITLE
        )
    }
}

@Composable
fun GameText(
    text: String,
    fontSize: TextUnit,
    fontFamily: FontFamily,
    color: Color,
    testTag: String
) {
    Text(
        text = text,
        fontSize = fontSize,
        fontFamily = fontFamily,
        color = color,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .testTag(testTag)
            .semantics{
                testTagsAsResourceId = true
            }
    )
}

@Composable
fun BoxScope.TimeLineArrow() {
    Box(
        modifier = Modifier
            .fillMaxHeight(0.8f)
            .width(80.dp)
            .align(Alignment.CenterStart),
        contentAlignment = Alignment.Center
    ) {
        DownwardArrow()

        ArrowText(
            text = Res.string.arrow_oldest_text,
            alignment = Alignment.TopCenter,
            offset = 32.dp,
            testTag = ARROW_OLDEST_TEXT
        )

        ArrowText(
            text = Res.string.arrow_latest_text,
            alignment = Alignment.BottomCenter,
            offset = (-32).dp,
            testTag = ARROW_LATEST_TEXT
        )
    }
}

@Composable
fun DownwardArrow() {
    val arrowHeadSize = 10.dp
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 34.dp)
            .testTag(TIME_LINE_ARROW)
            .semantics{
                testTagsAsResourceId = true
            }
    ) {
        val strokeWidth = 2.dp.toPx()
        val headPx = arrowHeadSize.toPx()

        drawLine(
            color = Color.White.copy(alpha = 0.7f),
            start = Offset(size.width / 2, size.width / 2),
            end = Offset(size.width / 2, size.height),
            strokeWidth = strokeWidth
        )

        drawLine(
            color = Color.White.copy(alpha = 0.7f),
            start = Offset(size.width / 2, size.height),
            end = Offset(size.width / 2 - headPx, size.height - headPx),
            strokeWidth = strokeWidth
        )

        drawLine(
            color = Color.White.copy(alpha = 0.7f),
            start = Offset(size.width / 2, size.height),
            end = Offset(size.width / 2 + headPx, size.height - headPx),
            strokeWidth = strokeWidth
        )
    }
}

@Composable
fun BoxScope.ArrowText(
    text: StringResource,
    alignment: Alignment,
    offset: Dp,
    testTag: String
) {
    Text(
        text = stringResource(text),
        fontSize = 14.sp,
        fontFamily = robotoMonoBold,
        modifier = Modifier
            .align(alignment)
            .offset(y = offset)
            .rotate(90f)
            .padding(top = 16.dp)
            .testTag(testTag)
            .semantics{
                testTagsAsResourceId = true
            },
        color = Color.White.copy(alpha = 0.7f)
    )
}