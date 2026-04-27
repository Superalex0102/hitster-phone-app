import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.TeamSelectionScreen
import com.rdisoftware.chronobeat.ui.screens.components.BottomText
import com.rdisoftware.chronobeat.shared.resources.*
import com.rdisoftware.chronobeat.ui.enums.ButtonSize
import com.rdisoftware.chronobeat.ui.screens.components.GradientBackground
import com.rdisoftware.chronobeat.ui.screens.components.GradientButton
import com.rdisoftware.chronobeat.ui.screens.components.TopLeftLogoText
import com.rdisoftware.chronobeat.ui.theme.kdamThmorProRegular
import com.rdisoftware.chronobeat.ui.theme.robotoMonoRegular
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.TextStyle

object TeamColorList{
    val teamColors = listOf(
        Color(0xFF48A0B7),
        Color(0xFFDA9F36),
        Color(0xFFD42D59),
        Color(0xFF6E1667))
}
@Composable
@Preview
fun TeamSelectionScreen() {

    val nameState = rememberTextFieldState()
    val teams = remember { mutableStateListOf<String>() }


    GradientBackground()
    TopLeftLogoText()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MainTitle()
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TeamInputField(
                state = nameState,
                onAddTeam = {handleAddTeam(nameState,teams)} , // TODO: Change "Add team" click action
            )
        }
         InfoText()

        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)){

        TeamList(teams, modifier = Modifier.fillMaxSize(),TeamColorList.teamColors)
        }

        GradientButton(
            text = stringResource(Res.string.start),
            enabled = true,
            size = ButtonSize.SMALL,
            testTag = TeamSelectionScreen.START_GAME_BUTTON,
            onClick = {} //TODO: Create "Start game" on click action
        )
        Spacer(modifier = Modifier.weight(0.15f))

        BottomText(stringResource(Res.string.powered_by), stringResource(Res.string.bottom_app_name))
    }
}
@Composable
fun MainTitle() {
    Text(
        text = stringResource(Res.string.team_selection_title),
        fontSize = 48.sp,
        lineHeight = 70.sp,
        textAlign = TextAlign.Center,
        color = Color.White,
        fontFamily = kdamThmorProRegular,
        modifier = Modifier
            .padding(top = 80.dp, bottom = 24.dp)
            .testTag(TeamSelectionScreen.TEAM_SELECTION_TITLE)
    )

}

@Composable
fun InfoText() {

    Row(
        modifier = Modifier.padding(top = 8.dp, bottom = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Outlined.Info,
            contentDescription = "Info",
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(Res.string.ts_info_text),
            color = Color.White,
            fontFamily = robotoMonoRegular,
            fontSize = 12.sp,
            modifier = Modifier.testTag(TeamSelectionScreen.INFO_TEXT)
        )
    }
}

@Composable
fun TeamList(teams: MutableList<String>, modifier: Modifier,colors: List<Color>) {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            itemsIndexed(teams) { index,team ->
                val color = colors[index % colors.size]
                TeamRow(teamName = team,color = color)
            }
        }
    }

@Composable
fun TeamRow(teamName : String,color: Color){ // TODO: Add unique test tag for each team
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier
            .fillMaxWidth().padding(bottom = 8.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(30))
            .border(1.5.dp, Color.White, RoundedCornerShape(30))
            .background(color.copy(alpha = 1f))
            .innerShadow(
            shape = RoundedCornerShape(30),
            shadow = Shadow(
                radius = 10.dp,
                spread =2.dp,
                color = Color.White.copy(0.8f),
                offset = DpOffset.Zero
            )
        ),
            contentAlignment = Alignment.CenterStart){
            DisplayTeamNames(teamName)
        }
    }
}



fun handleAddTeam(nameState: TextFieldState, teams: MutableList<String>){

    val text = nameState.text.toString()
    if (text.isNotBlank()) {
        teams.add(text)
        nameState.edit {
            replace(0, length,"")
        }

    }
}
@Composable
fun DisplayTeamNames(teamName: String){
    Row(modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically){
        Text(
            text = teamName,
            fontSize = 24.sp,
            textAlign = TextAlign.Start,
            color = Color.White,
            fontFamily = robotoMonoRegular,
            modifier = Modifier.padding(start = 16.dp).weight(1f)
        )


        Row {
            IconButton(onClick = { }) { // TODO:  Create "Edit team name" on click action
                Icon(
                    Icons.Outlined.Edit,
                    contentDescription = "Edit",
                    tint = Color.White,
                    modifier = Modifier.testTag(TeamSelectionScreen.EDIT_ICON_BUTTON)
                )
            }

            IconButton(onClick = { }) { // TODO:  Create "Delete team name" on click action
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.testTag(TeamSelectionScreen.DELETE_ICON_BUTTON)
                )
            }
        }
    }
}

@Composable
fun TeamInputField(
    state: TextFieldState,
    onAddTeam: () -> Unit,
) {

    val gradientBrush = Brush.horizontalGradient(colors = listOf(Color.Gray, Color.DarkGray,Color.DarkGray,Color.Black))
    BasicTextField(
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(brush = gradientBrush)
            .border(3.dp, Color.White.copy(alpha = 1f), RoundedCornerShape(30.dp))
            .testTag(TeamSelectionScreen.TEAM_INPUT_FIELD),
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        lineLimits = TextFieldLineLimits.SingleLine,
        cursorBrush = SolidColor(Color.White),
        decorator = {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f).padding(start = 30.dp)){
                    if(state.text.isEmpty()){
                        Text(
                            text = stringResource(Res.string.ts_input_placeholder),
                            fontFamily = robotoMonoRegular,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 24.sp,
                            modifier = Modifier.testTag(TeamSelectionScreen.TEAM_INPUT_PLACEHOLDER_TEXT)
                        )
                    }
                    it()
                }

                IconButton(
                    enabled = state.text.isNotBlank(),
                    onClick = onAddTeam,
                    modifier = Modifier.testTag(TeamSelectionScreen.ADD_ICON_BUTTON).size(64.dp)
                ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .border(
                            width = 3.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .background(Color.Black.copy( alpha = 0.2f),

                            shape = CircleShape
                        ),
                        contentAlignment = Alignment.Center)
                    {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            tint = Color.White,
                            contentDescription = "Add team",
                            modifier = Modifier.fillMaxSize()

                        )
                    }
                }
            }

        }
    )
}
