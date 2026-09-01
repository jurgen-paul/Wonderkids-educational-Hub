package com.example.ui.components

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.PlayfulBlue
import com.example.ui.theme.PlayfulCoral
import com.example.ui.theme.PlayfulGreen
import com.example.ui.theme.PlayfulIndigo
import com.example.ui.theme.PlayfulOrange
import com.example.ui.theme.PlayfulPink
import com.example.ui.theme.PlayfulTeal

data class AvatarOption(
    val id: String,
    val name: String,
    val emoji: String,
    val bgColor: Color
)

object AvatarHelper {
    val avatars: List<AvatarOption> = listOf(
        AvatarOption("lion", "Leo the Lion", "🦁", PlayfulOrange),
        AvatarOption("astronaut", "Star Voyager", "🚀", PlayfulIndigo),
        AvatarOption("owl", "Professor Hoot", "🦉", PlayfulTeal),
        AvatarOption("fox", "Sparky Fox", "🦊", PlayfulCoral),
        AvatarOption("panda", "Bamboo Panda", "🐼", PlayfulGreen),
        AvatarOption("dino", "Rex the Dino", "🦖", PlayfulGreen),
        AvatarOption("unicorn", "Rainbow Star", "🦄", PlayfulPink),
        AvatarOption("robot", "Beep Bot", "🤖", PlayfulBlue)
    )

    fun getAvatar(id: String): AvatarOption {
        return avatars.find { it.id.equals(id, ignoreCase = true) } ?: avatars.first()
    }
}
