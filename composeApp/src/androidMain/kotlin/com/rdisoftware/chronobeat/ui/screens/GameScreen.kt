package com.rdisoftware.chronobeat.ui.screens

import android.R.attr.maxHeight
import android.R.attr.maxWidth
import androidx.compose.animation.Animatable
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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rdisoftware.chronobeat.ui.screens.components.GradientBackground
import com.rdisoftware.chronobeat.ui.theme.robotoMonoBold
import com.rdisoftware.chronobeat.ui.theme.robotoMonoLightItalic
import com.rdisoftware.chronobeat.ui.theme.robotoMonoMedium
import com.rdisoftware.chronobeat.ui.theme.robotoMonoRegular
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.random.Random

private val teamName = "Team 1"
private val cardCount = 5
data class Song(
    val artist: String,
    val year: String,
    val title: String
)

private val song1 = Song(
    artist = "Guns N' Roses",
    year = "1987",
    title = "Sweet Child O' Mine"
)

private val song2 = Song(
    artist = "Dzsudló",
    year = "2021",
    title = "Para"
)

private val song3 = Song(
    artist = "RÜFÜS DU SOL",
    year = "2021",
    title = "Make It Happen"
)
val songs = listOf(song1, song2, song3)



@Preview
@Composable
fun GameScreen() {
    Box() {
        GradientBackground()

        Column(

        ) {
            Text(
                text = "Oldest",
                color = Color.White,
                modifier = Modifier
                    .rotate(90f)
            )

            Icon(
                imageVector = Icons.Filled.ArrowDownward,
                contentDescription = "Arrow",
                tint = Color.White
            )
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
                TeamHeader()

                AnimatedSoundWaves(
                    isAnimating = true
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
fun TeamHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .fillMaxHeight(0.07f)
            .border(
                width = 2.dp,
                color = Color.White,
                shape = RoundedCornerShape(30)
            )
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF48A0B7),
                        Color.Black
                    )
                ),
                shape = RoundedCornerShape(30)
            )
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = teamName,
            fontSize = 32.sp,
            fontFamily = robotoMonoBold,
            color = Color.White
        )

        Text(
            text = "A $cardCount",
            fontSize = 24.sp,
            fontFamily = robotoMonoRegular,
            color = Color.White
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
                    color = Color(0xFFD9D9D9).copy(alpha = 0.6f))
        )

        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    shape = CircleShape,
                    color = Color(0xFFD9D9D9))
        )
    }
}

@Composable
fun GameCard(
    song: Song
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .border(
                width = 3.dp,
                color = Color(0xFF48A0B7),
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
                fontFamily = robotoMonoMedium
            )

            Text(
                text = song.year,
                fontSize = 64.sp,
                fontFamily = robotoMonoBold
            )
            Text(
                text = song.title,
                fontSize = 20.sp,
                fontFamily = robotoMonoLightItalic
            )

        }
    }
}

//@Composable
//fun GuessButton(
//    onClick: () -> Unit
//) {
//    Canvas(
//        modifier = Modifier
//            .size(64.dp)
//            .clickable(onClick = onClick)
//    ) {
//        drawCircle(
//            color = Color(0xFFD9D9D9).copy(alpha = 0.6f),
//            radius = size.minDimension / 2
//        )
//
//        drawCircle(
//            color = Color(0xFFD9D9D9),
//            radius = size.minDimension / 4
//        )
//    }
//}