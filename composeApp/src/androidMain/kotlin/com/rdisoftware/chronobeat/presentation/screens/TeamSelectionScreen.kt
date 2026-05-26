package com.rdisoftware.chronobeat.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.rdisoftware.chronobeat.domain.models.Team
import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.TeamSelectionScreen
import com.rdisoftware.chronobeat.presentation.dimensions.LocalBaseDimensions
import com.rdisoftware.chronobeat.presentation.dimensions.PhoneTeamSelectionDimensions
import com.rdisoftware.chronobeat.presentation.dimensions.TabletTeamSelectionDimensions
import com.rdisoftware.chronobeat.presentation.dimensions.TeamSelectionLocalDimensions
import com.rdisoftware.chronobeat.presentation.dimensions.teamSelDimens
import com.rdisoftware.chronobeat.shared.resources.Res
import com.rdisoftware.chronobeat.shared.resources.bottom_app_name
import com.rdisoftware.chronobeat.shared.resources.content_disc_add_team
import com.rdisoftware.chronobeat.shared.resources.content_disc_delete
import com.rdisoftware.chronobeat.shared.resources.content_disc_edit
import com.rdisoftware.chronobeat.shared.resources.content_disc_info
import com.rdisoftware.chronobeat.shared.resources.powered_by
import com.rdisoftware.chronobeat.shared.resources.start
import com.rdisoftware.chronobeat.shared.resources.team_selection_title
import com.rdisoftware.chronobeat.shared.resources.ts_info_text
import com.rdisoftware.chronobeat.shared.resources.ts_input_placeholder
import com.rdisoftware.chronobeat.presentation.enums.ButtonSize
import com.rdisoftware.chronobeat.presentation.screens.components.BottomText
import com.rdisoftware.chronobeat.presentation.screens.components.GradientBackground
import com.rdisoftware.chronobeat.presentation.screens.components.GradientButton
import com.rdisoftware.chronobeat.presentation.screens.components.LogoText
import com.rdisoftware.chronobeat.presentation.screens.components.ScreenTitle
import com.rdisoftware.chronobeat.presentation.theme.horizontalGradientBrush
import com.rdisoftware.chronobeat.presentation.theme.robotoMonoRegular
import com.rdisoftware.chronobeat.presentation.viewmodels.TeamSelectionViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@OptIn(ExperimentalUuidApi::class)
@Composable
fun TeamSelectionScreen(
    viewModel: TeamSelectionViewModel = koinViewModel(),
    onTeamsSelectedClicked: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val dimensions = if (screenWidth >= 600.dp) TabletTeamSelectionDimensions
    else PhoneTeamSelectionDimensions

    CompositionLocalProvider(
        TeamSelectionLocalDimensions provides dimensions,
        LocalBaseDimensions provides dimensions.base
    ) {
        GradientBackground()

        LogoText()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth()
                .widthIn(max = teamSelDimens.base.maxContentWidth)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenTitle(
                text = stringResource(Res.string.team_selection_title),
                testTag = TeamSelectionScreen.TEAM_SELECTION_TITLE,
                resourceId = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TeamInputField(
                    value = state.inputName,
                    onValueChange = { viewModel.onNameChanged(it) },
                    onAddTeam = {
                        if (state.isEditing) viewModel.confirmEdit()
                        else viewModel.addTeam()
                    }
                )
            }

            InfoText()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                TeamList(
                    teams = state.teams,
                    onDelete = { viewModel.deleteTeam(it) },
                    onEdit = { viewModel.startEdit(it) },
                    modifier = Modifier.fillMaxSize()
                )
            }

            GradientButton(
                text = stringResource(Res.string.start),
                enabled = state.canStartGame,
                size = ButtonSize.SMALL,
                testTag = TeamSelectionScreen.START_GAME_BUTTON,
                resourceId = true,
                onClick = { onTeamsSelectedClicked() }
            )

            Spacer(modifier = Modifier.weight(0.15f))

            BottomText(
                stringResource(Res.string.powered_by),
                stringResource(Res.string.bottom_app_name)
            )
        }
    }
}

@Composable
fun InfoText() {
    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = stringResource(Res.string.content_disc_info),
            tint = Color.White,
            modifier = Modifier
                .testTag(TeamSelectionScreen.INFO_ICON)
                .semantics {
                    testTagsAsResourceId = true
                }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = stringResource(Res.string.ts_info_text), // TODO: Create dynamic text
            color = Color.White,
            fontFamily = robotoMonoRegular,
            fontSize = teamSelDimens.infoMessageFontSize,
            modifier = Modifier
                .testTag(TeamSelectionScreen.INFO_TEXT)
                .semantics {
                    testTagsAsResourceId = true
                }
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
fun TeamList(
    teams: List<Team>,
    onDelete: (Uuid) -> Unit,
    onEdit: (Team) -> Unit,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        itemsIndexed(teams) { _, team ->
            TeamRow(
                team = team,
                onDelete = onDelete,
                onEdit = onEdit
            )
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
fun TeamRow(
    team: Team,
    onDelete: (Uuid) -> Unit,
    onEdit: (Team) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = teamSelDimens.teamRowHorizontalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .height(teamSelDimens.teamRowHeight)
                .clip(RoundedCornerShape(teamSelDimens.teamRowRoundedCornerShape))
                .border(
                    width = teamSelDimens.teamRowBorderWidth,
                    Color.White,
                    shape = RoundedCornerShape(teamSelDimens.teamRowRoundedCornerShape)
                )
                .background(color = team.color.color.copy(alpha = 1f))
                .innerShadow(
                    shape = RoundedCornerShape(teamSelDimens.teamRowRoundedCornerShape),
                    shadow = Shadow(
                        radius = 10.dp,
                        spread = 2.dp,
                        color = Color.White.copy(0.8f),
                        offset = DpOffset.Zero
                    )
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            DisplayTeamNames(
                teamName = team.name,
                onDelete = { onDelete(team.id) },
                onEdit = { onEdit(team) }
            )
        }
    }
}

@Composable
fun DisplayTeamNames(
    teamName: String,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = teamName,
            fontSize = teamSelDimens.teamNameFontSize,
            textAlign = TextAlign.Start,
            color = Color.White,
            fontFamily = robotoMonoRegular,
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        )
        Row {
            IconButton(onClick = onEdit) {
                Icon(
                    Icons.Outlined.Edit,
                    contentDescription = stringResource(Res.string.content_disc_edit),
                    tint = Color.White,
                    modifier = Modifier
                        .testTag(TeamSelectionScreen.EDIT_ICON_BUTTON)
                        .semantics {
                            testTagsAsResourceId = true
                            role = Role.Button
                        }
                        .size(teamSelDimens.editDeleteIconsSize)
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = stringResource(Res.string.content_disc_delete),
                    tint = Color.White,
                    modifier = Modifier
                        .testTag(TeamSelectionScreen.DELETE_ICON_BUTTON)
                        .semantics {
                            testTagsAsResourceId = true
                            role = Role.Button
                        }
                        .size(teamSelDimens.editDeleteIconsSize)
                )
            }
        }
    }
}

@Composable
fun TeamInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onAddTeam: () -> Unit,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(teamSelDimens.teamInputHeight)
            .clip(RoundedCornerShape(teamSelDimens.roundedCornerShape))
            .background(brush = horizontalGradientBrush)
            .border(
                width = 3.dp,
                Color.White.copy(alpha = 1f),
                RoundedCornerShape(teamSelDimens.roundedCornerShape)
            )
            .testTag(TeamSelectionScreen.TEAM_INPUT_FIELD)
            .semantics {
                testTagsAsResourceId = true
            },
        textStyle = TextStyle(color = Color.White, fontSize = teamSelDimens.inputFontSize),
        singleLine = true,
        cursorBrush = SolidColor(Color.White),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 30.dp)
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = stringResource(Res.string.ts_input_placeholder),
                            fontFamily = robotoMonoRegular,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = teamSelDimens.inputFontSize,
                            modifier = Modifier
                                .testTag(TeamSelectionScreen.TEAM_INPUT_PLACEHOLDER_TEXT)
                                .semantics {
                                    testTagsAsResourceId = true
                                }
                        )
                    }
                    innerTextField()
                }

                Box(
                    modifier = Modifier
                        .size(teamSelDimens.addIconBoxSize),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(teamSelDimens.roundedCornerShape))
                            .border(
                                width = 3.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(teamSelDimens.roundedCornerShape),
                            )
                            .background(
                                Color.Black,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            enabled = value.isNotBlank(), // TODO: Error handling: add an error message
                            onClick = onAddTeam,
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag(TeamSelectionScreen.ADD_ICON_BUTTON)
                                .semantics {
                                    testTagsAsResourceId = true
                                    role = Role.Button
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                tint = Color.White,
                                contentDescription = stringResource(Res.string.content_disc_add_team),
                                modifier = Modifier.size(teamSelDimens.addIconSize)
                            )
                        }
                    }
                }
            }
        }
    )
}