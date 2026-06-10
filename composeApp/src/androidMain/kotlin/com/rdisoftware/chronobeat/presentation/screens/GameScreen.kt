package com.rdisoftware.chronobeat.presentation.screens

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rdisoftware.chronobeat.domain.enums.TeamColor
import com.rdisoftware.chronobeat.domain.models.Team
import com.rdisoftware.chronobeat.domain.models.Track
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
import com.rdisoftware.chronobeat.presentation.dimensions.GameLocalDimensions
import com.rdisoftware.chronobeat.presentation.dimensions.LocalBaseDimensions
import com.rdisoftware.chronobeat.presentation.dimensions.PhoneGameDimensions
import com.rdisoftware.chronobeat.presentation.dimensions.TabletGameDimensions
import com.rdisoftware.chronobeat.presentation.dimensions.gameDimens
import com.rdisoftware.chronobeat.shared.resources.Res
import com.rdisoftware.chronobeat.shared.resources.arrow_latest_text
import com.rdisoftware.chronobeat.shared.resources.arrow_oldest_text
import com.rdisoftware.chronobeat.theme.AppColors
import com.rdisoftware.chronobeat.presentation.screens.components.GradientBackground
import com.rdisoftware.chronobeat.presentation.theme.robotoMonoBold
import com.rdisoftware.chronobeat.presentation.theme.robotoMonoLightItalic
import com.rdisoftware.chronobeat.presentation.theme.robotoMonoMedium
import com.rdisoftware.chronobeat.presentation.theme.robotoMonoRegular
import com.rdisoftware.chronobeat.presentation.viewmodels.GamePhase
import com.rdisoftware.chronobeat.presentation.viewmodels.GameSnapshot
import com.rdisoftware.chronobeat.presentation.viewmodels.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.random.Random

@Composable
fun GameScreen(
    viewModel: GameViewModel = koinViewModel(),
    onGameFinishedClicked: () -> Unit,
    onLeaderBoardClicked: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var isInitialStartup by rememberSaveable { mutableStateOf(true) }
    var isReturningFromLeaderboard by rememberSaveable { mutableStateOf(false) }
    var showResultOverlay by rememberSaveable { mutableStateOf(false) }

    var snapshot by remember { mutableStateOf(GameSnapshot()) }

    LaunchedEffect(state.currentTeam, state.timeline, state.currentCardCount, state.isGuessCorrect, showResultOverlay) {
        if(!showResultOverlay) {
            snapshot = GameSnapshot(
                team = state.currentTeam,
                timeline = state.timeline,
                cardCount = state.currentCardCount,
                track = state.currentTrack,
                isCorrect = state.isGuessCorrect
            )
        }
    }

    LaunchedEffect(state.currentPhase) {
        when (state.currentPhase) {
            GamePhase.GAME_OVER -> {
                onGameFinishedClicked()
            }
            GamePhase.SHOW_RESULT -> {
                showResultOverlay = true
            }
            GamePhase.GUESSING -> {
                showResultOverlay = false
            }
            GamePhase.SHOW_NEXT_TEAM_POPUP -> {
                if (isInitialStartup) {
                    isInitialStartup = false
                    viewModel.onPopupAcknowledgePressed()
                } else if (isReturningFromLeaderboard){
                    isReturningFromLeaderboard = false
                    viewModel.onPopupAcknowledgePressed()
                } else {
                    isReturningFromLeaderboard = true
                    onLeaderBoardClicked()
                }
            }
            else -> {}
        }
    }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val dimensions = if (screenWidth >= 600.dp) TabletGameDimensions else PhoneGameDimensions

    CompositionLocalProvider(
        GameLocalDimensions provides dimensions,
        LocalBaseDimensions provides dimensions.base
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
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
                GameHeader(
                    currentTeam = snapshot.team,
                    cardCount = snapshot.cardCount,
                    isAnimating = state.currentPhase == GamePhase.GUESSING
                )

                GameSurface(
                    timeline = snapshot.timeline,
                    currentTrack = snapshot.track,
                    teamColor = snapshot.team?.color,
                    onGuessPressed = { position -> viewModel.onGuessPressed(position) },
                    isGuessingPhase = state.currentPhase == GamePhase.GUESSING
                )
            }

            if (state.currentPhase == GamePhase.LOADING) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            if (state.currentPhase == GamePhase.SHOW_NEXT_TEAM_POPUP) {
                NextTeamPopupOverlay(
                    teamName = state.currentTeam?.name ?: "",
                    onOkClicked = { viewModel.onPopupAcknowledgePressed() }
                )
            }

            if (state.currentPhase == GamePhase.SHOW_RESULT) {
                ResultOverlay(isCorrect = state.isGuessCorrect)
            }

            state.error?.let { error ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(Color.Red.copy(alpha = 0.8f))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun NextTeamPopupOverlay(
    teamName: String,
    onOkClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Color(AppColors.GAME_GRAY),
            modifier = Modifier.padding(32.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "$teamName következik!",
                    fontSize = 24.sp,
                    fontFamily = robotoMonoBold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Button(onClick = onOkClicked) {
                    Text("OK, Mehet!")
                }
            }
        }
    }
}

@Composable
fun ResultOverlay(isCorrect: Boolean?) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)),
        contentAlignment = Alignment.Center
    ) {
        val (text, color) = if (isCorrect == true) {
            "Helyes!" to Color.Green
        } else {
            "Helytelen!" to Color.Red
        }
        Text(
            text = text,
            fontSize = 48.sp,
            fontFamily = robotoMonoBold,
            color = color,
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                .padding(24.dp)
        )
    }
}

@Composable
fun GameHeader(
    currentTeam: Team?,
    cardCount: Int,
    isAnimating: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth()
            .widthIn(max = gameDimens.base.maxContentWidth),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TeamInformation(
            currentTeam = currentTeam,
            cardCount = cardCount
        )
        AnimatedSoundWaves(
            isAnimating = isAnimating
        )
    }
}

@Composable
fun TeamInformation(
    currentTeam: Team?,
    cardCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.87f)
            .fillMaxHeight(0.07f)
            .border(
                width = 2.dp,
                color = Color(AppColors.WHITE),
                shape = RoundedCornerShape(30)
            )
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        currentTeam?.color?.color ?: Color(AppColors.WHITE),
                        Color(AppColors.BLACK)
                    )
                ),
                shape = RoundedCornerShape(30)
            )
            .padding(start = 16.dp)
            .testTag(GAME_HEADER)
            .semantics {
                testTagsAsResourceId = true
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GameText(
            text = currentTeam?.name ?: "",
            fontSize = gameDimens.currentTeamNameFontSize,
            fontFamily = robotoMonoBold,
            lineHeight = 40.sp,
            color = Color(AppColors.WHITE),
            testTag = TEAM_NAME_TEXT
        )
        NumberCard(cardCount = cardCount.toString())
    }
}

@Composable
fun NumberCard(
    cardCount: String
) {
    Box(
        modifier = Modifier
            .width(gameDimens.cardCountBoxSize)
            .fillMaxHeight(1f)
            .clip(RoundedCornerShape(30))
            .background(Color.Transparent)
            .border(2.dp, Color(AppColors.WHITE).copy(alpha = 0.7f), RoundedCornerShape(30)),
        contentAlignment = Alignment.Center
    ) {
        GameText(
            text = cardCount,
            fontSize = gameDimens.cardCountNumberFontSize,
            fontFamily = robotoMonoRegular,
            lineHeight = 40.sp,
            color = Color(AppColors.WHITE),
            testTag = CARD_COUNT_TEXT
        )
    }
}

@Composable
fun AnimatedSoundWaves(
    isAnimating: Boolean,
    modifier: Modifier = Modifier,
    barColor: Color = Color(AppColors.WHITE)
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
            .semantics {
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
fun GameSurface(
    timeline: List<Track>,
    currentTrack: Track?,
    teamColor: TeamColor?,
    onGuessPressed: (Int) -> Unit,
    isGuessingPhase: Boolean
) {
    val itemCount = timeline.size * 2 + 1
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(itemCount) { index ->
            if (index % 2 == 0) {
                val position = index / 2
                GuessButton(
                    isEnabled = currentTrack != null && isGuessingPhase,
                    onClick = { onGuessPressed(position) }
                )
            } else {
                val trackIndex = index / 2
                GameCard(track = timeline[trackIndex], teamColor = teamColor)
            }
        }
    }
}

@Composable
fun GuessButton(
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(gameDimens.guessButtonOuterCircle)
            .clip(CircleShape)
            .clickable(enabled = isEnabled, onClick = onClick)
            .testTag(GUESS_BUTTON)
            .semantics {
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
                    color = Color(AppColors.GAME_GRAY).copy(alpha = 0.6f)
                )
        )
        Box(
            modifier = Modifier
                .size(gameDimens.guessButtonInnerCircle)
                .background(
                    shape = CircleShape,
                    color = Color(AppColors.GAME_GRAY)
                )
        )
    }
}

@Composable
fun GameCard(
    track: Track,
    teamColor: TeamColor?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(gameDimens.cardMaxWidthFraction)
            .aspectRatio(1.4f)
            .padding(horizontal = 46.dp, vertical = 32.dp)
            .border(
                width = 3.dp,
                color = teamColor?.color ?: Color(AppColors.DEFAULT_BLUE),
                shape = RoundedCornerShape(12)
            )
            .testTag(GAME_CARD)
            .semantics {
                testTagsAsResourceId = true
            },
        backgroundColor = Color(AppColors.GAME_GRAY),
        shape = RoundedCornerShape(12)
    ) {
        GameCardContent(track = track)
    }
}

@Composable
fun GameCardContent(
    track: Track
) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameText(
            text = track.mainArtist,
            fontSize = gameDimens.mainArtistFontSize,
            fontFamily = robotoMonoMedium,
            lineHeight = 40.sp,
            color = Color(AppColors.BLACK),
            testTag = GAME_CARD_ARTIST
        )
        if (track.featArtists.isNotEmpty()) {
            GameText(
                text = track.featArtists.joinToString(", "),
                fontSize = gameDimens.featArtistFontSize,
                fontFamily = robotoMonoLightItalic,
                lineHeight = 40.sp,
                color = Color(AppColors.BLACK),
                testTag = GAME_CARD_CONTRIBUTOR
            )
        }

        GameText(
            text = track.releaseYear.toString(),
            fontSize = gameDimens.releaseYearFontSize,
            fontFamily = robotoMonoBold,
            lineHeight = 40.sp,
            color = Color(AppColors.BLACK),
            testTag = GAME_CARD_YEAR
        )

        GameText(
            text = track.title,
            fontSize = gameDimens.trackTitleFontSize,
            fontFamily = robotoMonoLightItalic,
            lineHeight = 40.sp,
            color = Color(AppColors.BLACK),
            testTag = GAME_CARD_TITLE
        )
    }
}

@Composable
fun GameText(
    text: String,
    fontSize: TextUnit,
    fontFamily: FontFamily,
    lineHeight: TextUnit,
    color: Color,
    testTag: String
) {
    Text(
        text = text,
        fontSize = fontSize,
        fontFamily = fontFamily,
        color = color,
        lineHeight = lineHeight,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .testTag(testTag)
            .semantics {
                testTagsAsResourceId = true
            }
    )
}

@Composable
fun BoxScope.TimeLineArrow() {
    Box(
        modifier = Modifier
            .fillMaxHeight(0.8f)
            .width(gameDimens.arrowWidthPosition)
            .align(Alignment.CenterStart),
        contentAlignment = Alignment.Center
    ) {
        DownwardArrow()
        ArrowText(
            text = Res.string.arrow_oldest_text,
            alignment = Alignment.TopCenter,
            offset = gameDimens.arrowTextOffset,
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
            .semantics {
                testTagsAsResourceId = true
            }
    ) {
        val strokeWidth = 2.dp.toPx()
        val headPx = arrowHeadSize.toPx()
        drawLine(
            color = Color(AppColors.WHITE).copy(alpha = 0.7f),
            start = Offset(size.width / 2, size.width / 2),
            end = Offset(size.width / 2, size.height),
            strokeWidth = strokeWidth
        )

        drawLine(
            color = Color(AppColors.WHITE).copy(alpha = 0.7f),
            start = Offset(size.width / 2, size.height),
            end = Offset(size.width / 2 - headPx, size.height - headPx),
            strokeWidth = strokeWidth
        )

        drawLine(
            color = Color(AppColors.WHITE).copy(alpha = 0.7f),
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
            .semantics {
                testTagsAsResourceId = true
            },
        color = Color(AppColors.WHITE).copy(alpha = 0.7f)
    )
}