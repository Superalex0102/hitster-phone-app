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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import kotlin.random.Random

@Preview
@Composable
fun GameScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GradientBackground()

        Box(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .width(80.dp)
                .align(Alignment.CenterStart),
            contentAlignment = Alignment.Center
        ) {
            TimeLineArrow()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TeamHeader(MockHeaderData.data.first()) //Mock data for testing

                AnimatedSoundWaves(
                    isAnimating = MockHeaderData.data.first().isMusicOn //Mock data for testing
                )
            }

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
                            GameCard(song = songs[i])
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TeamHeader(
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
            .padding(start = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = model.teamName,
            fontSize = 32.sp,
            fontFamily = robotoMonoBold,
            color = Color.White
        )

        NumberCard(cardCount = "5")
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
        Text(
            text = cardCount,
            color = Color.White,
            fontSize = 24.sp,
            fontFamily = robotoMonoRegular
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
        modifier = modifier.height(30.dp),
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
fun GuessButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    shape = CircleShape,
                    color = Color(0xFFD9D9D9).copy(alpha = 0.6f)
                )
        )

        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    shape = CircleShape,
                    color = Color(0xFFD9D9D9)
                )
        )
    }
}

@Composable
fun GameCard(
    song: SongModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 46.dp, vertical = 32.dp)
            .border(
                width = 3.dp,
                color = Color(0xFF48A0B7), //placeholder - viewModel function will compute this color value
                shape = RoundedCornerShape(12)
            ),
        backgroundColor = Color(0xFFD9D9D9),
        shape = RoundedCornerShape(12)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = song.artist,
                fontSize = 28.sp,
                fontFamily = robotoMonoMedium,
                textAlign = TextAlign.Center
            )

            song.contributor?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    fontFamily = robotoMonoLightItalic,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                text = song.year,
                fontSize = 64.sp,
                fontFamily = robotoMonoBold,
                modifier = Modifier
            )
            Text(
                text = song.title,
                fontSize = 20.sp,
                fontFamily = robotoMonoLightItalic,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun BoxScope.TimeLineArrow() {
    val arrowHeadSize = 10.dp
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 34.dp)
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

    Text(
        text = "Oldest",
        fontSize = 14.sp,
        fontFamily = robotoMonoBold,
        modifier = Modifier
            .align(Alignment.TopCenter)
            .offset(y = 32.dp)
            .rotate(90f)
            .padding(top = 16.dp),
        color = Color.White.copy(alpha = 0.7f)
    )

    Text(
        text = "Newest",
        fontSize = 14.sp,
        fontFamily = robotoMonoBold,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .offset(y = (-32).dp)
            .rotate(90f)
            .padding(top = 16.dp),
        color = Color.White.copy(alpha = 0.7f)
    )
}