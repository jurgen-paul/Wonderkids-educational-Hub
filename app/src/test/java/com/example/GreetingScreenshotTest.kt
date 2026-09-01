package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.data.model.ChildProfile
import com.example.ui.kids.KidsHomeScreen
import com.example.ui.theme.WonderKidsTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [34])
class GreetingScreenshotTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun kidsHomeScreen_screenshot() {
        val sampleChild = ChildProfile(
            id = 1,
            name = "Leo",
            age = 6,
            avatarId = "lion",
            gradeLevel = "Kindergarten",
            totalStars = 48,
            currentStreakDays = 4,
            todayPlayedMinutes = 18,
            dailyGoalMinutes = 25,
            isCurrentActive = true
        )

        composeTestRule.setContent {
            WonderKidsTheme {
                KidsHomeScreen(
                    activeChild = sampleChild,
                    allChildren = listOf(sampleChild),
                    badges = emptyList(),
                    onSelectChild = {},
                    onNavigateToGame = {},
                    onNavigateToBadges = {},
                    onOpenParentZone = {}
                )
            }
        }

        composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/kids_home.png")
    }
}
