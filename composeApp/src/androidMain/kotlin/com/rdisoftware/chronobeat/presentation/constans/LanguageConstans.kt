package com.rdisoftware.chronobeat.presentation.constans

import com.rdisoftware.chronobeat.presentation.constants.AccessibilityIds.SettingsScreen
import com.rdisoftware.chronobeat.shared.resources.Res
import com.rdisoftware.chronobeat.shared.resources.english
import com.rdisoftware.chronobeat.shared.resources.german
import com.rdisoftware.chronobeat.shared.resources.hungarian
import org.jetbrains.compose.resources.StringResource

data class LanguageUi(
    val label: StringResource,
    val testTag: String
)

object LanguageConstants {

    val all = listOf(
        LanguageUi(
            label = Res.string.hungarian,
            testTag = SettingsScreen.HUNGARIAN_TEXT
        ),
        LanguageUi(
            label = Res.string.english,
            testTag = SettingsScreen.ENGLISH_TEXT
        ),
        LanguageUi(
            label = Res.string.german,
            testTag = SettingsScreen.GERMAN_TEXT
        )
    )
}
