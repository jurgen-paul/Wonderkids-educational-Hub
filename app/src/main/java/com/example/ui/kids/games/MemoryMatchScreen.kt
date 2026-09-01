package com.example.ui.kids.games

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChildProfile
import com.example.data.model.GameCategory
import com.example.ui.components.GameSuccessCelebrationDialog
import com.example.ui.theme.ParentNavy
import com.example.ui.theme.PlayfulBlue
import com.example.ui.theme.PlayfulGreen
import com.example.ui.theme.PlayfulOrange
import com.example.ui.theme.PlayfulPurple
import com.example.ui.theme.PlayfulTeal
import com.example.ui.theme.PlayfulYellow
import com.example.ui.theme.StarGold
import kotlinx.coroutines.delay

data class MemoryCard(
    val id: Int,
    val emoji: String,
    val name: String,
    val isMatched: Boolean = false,
    val isFlipped: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryMatchScreen(
    child: ChildProfile?,
    onBack: () -> Unit,
    onGameFinished: (category: GameCategory, title: String, score: Int, total: Int, correct: Int, duration: Int, stars: Int) -> Unit
) {
    val animalItems = remember {
        listOf(
            "🦁" to "Lion",
            "🐼" to "Panda",
            "🦊" to "Fox",
            "🐘" to "Elephant",
            "🐵" to "Monkey",
            "🦄" to "Unicorn"
        )
    }

    val cards = remember {
        val list = mutableListOf<MemoryCard>()
        var idCounter = 0
        animalItems.forEach { (emoji, name) ->
            list.add(MemoryCard(idCounter++, emoji, name))
            list.add(MemoryCard(idCounter++, emoji, name))
        }
        mutableStateListOf<MemoryCard>().apply { addAll(list.shuffled()) }
    }

    var turnsCount by remember { mutableIntStateOf(0) }
    var matchedPairs by remember { mutableIntStateOf(0) }
    var firstSelectedIndex by remember { mutableStateOf<Int?>(null) }
    var secondSelectedIndex by remember { mutableStateOf<Int?>(null) }
    var isChecking by remember { mutableStateOf(false) }
    var isGameOver by remember { mutableStateOf(false) }
    val startTime = remember { mutableLongStateOf(System.currentTimeMillis()) }

    fun restartGame() {
        cards.clear()
        val list = mutableListOf<MemoryCard>()
        var idCounter = 0
        animalItems.forEach { (emoji, name) ->
            list.add(MemoryCard(idCounter++, emoji, name))
            list.add(MemoryCard(idCounter++, emoji, name))
        }
        cards.addAll(list.shuffled())
        turnsCount = 0
        matchedPairs = 0
        firstSelectedIndex = null
        secondSelectedIndex = null
        isChecking = false
        isGameOver = false
        startTime.longValue = System.currentTimeMillis()
    }

    // Check pair matches
    LaunchedEffect(secondSelectedIndex) {
        val first = firstSelectedIndex
        val second = secondSelectedIndex
        if (first != null && second != null) {
            isChecking = true
            turnsCount++
            delay(700)
            if (cards[first].emoji == cards[second].emoji) {
                // Match!
                cards[first] = cards[first].copy(isMatched = true)
                cards[second] = cards[second].copy(isMatched = true)
                matchedPairs++

                if (matchedPairs == animalItems.size) {
                    delay(300)
                    isGameOver = true
                    val duration = ((System.currentTimeMillis() - startTime.longValue) / 1000).toInt()
                    val score = when {
                        turnsCount <= 8 -> 100
                        turnsCount <= 11 -> 85
                        turnsCount <= 14 -> 70
                        else -> 60
                    }
                    val stars = when {
                        score >= 90 -> 5
                        score >= 80 -> 4
                        score >= 60 -> 3
                        else -> 2
                    }
                    onGameFinished(
                        GameCategory.MEMORY,
                        "Memory Safari Adventure",
                        score,
                        animalItems.size,
                        matchedPairs,
                        duration,
                        stars
                    )
                }
            } else {
                // Not a match, flip back
                cards[first] = cards[first].copy(isFlipped = false)
                cards[second] = cards[second].copy(isFlipped = false)
            }
            firstSelectedIndex = null
            secondSelectedIndex = null
            isChecking = false
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("🧩 Memory Safari", fontWeight = FontWeight.Bold, color = Color.White)
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("memory_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { restartGame() },
                        modifier = Modifier.testTag("memory_restart_button")
                    ) {
                        Icon(Icons.Default.Replay, contentDescription = "Restart", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PlayfulGreen
                )
            )
        },
        containerColor = Color(0xFFFBFCF8)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Stats Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFDCE5DB), RoundedCornerShape(20.dp))
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDCF0DF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🎯", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Pairs Found", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF717970)))
                        Text(
                            "$matchedPairs / ${animalItems.size}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PlayfulGreen
                            )
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFBF4D8)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🔄", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Turns", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF717970)))
                        Text(
                            "$turnsCount",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8B6A2B)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3x4 Grid of Cards
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                itemsIndexed(cards) { index, card ->
                    val isFlipped = card.isFlipped || card.isMatched

                    val rotation by animateFloatAsState(
                        targetValue = if (isFlipped) 180f else 0f,
                        animationSpec = tween(400),
                        label = "cardFlip"
                    )

                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                card.isMatched -> Color(0xFFDCF0DF)
                                isFlipped -> Color.White
                                else -> PlayfulGreen
                            }
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.85f)
                            .graphicsLayer {
                                rotationY = rotation
                                cameraDistance = 12f * density
                            }
                            .border(
                                width = if (card.isMatched) 2.dp else 1.dp,
                                color = if (card.isMatched) PlayfulGreen else Color(0xFFDCE5DB),
                                shape = RoundedCornerShape(18.dp)
                            )
                            .clickable(enabled = !isFlipped && !isChecking) {
                                cards[index] = card.copy(isFlipped = true)
                                if (firstSelectedIndex == null) {
                                    firstSelectedIndex = index
                                } else if (secondSelectedIndex == null) {
                                    secondSelectedIndex = index
                                }
                            }
                            .testTag("memory_card_$index")
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    if (rotation > 90f) {
                                        rotationY = 180f
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isFlipped) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = card.emoji, fontSize = 38.sp)
                                    Text(
                                        text = card.name,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = ParentNavy,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = Icons.Default.HelpOutline,
                                    contentDescription = "Hidden Card",
                                    tint = Color.White.copy(alpha = 0.8f),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (isGameOver) {
        val duration = ((System.currentTimeMillis() - startTime.longValue) / 1000).toInt()
        val score = when {
            turnsCount <= 8 -> 100
            turnsCount <= 11 -> 85
            turnsCount <= 14 -> 70
            else -> 60
        }
        val stars = when {
            score >= 90 -> 5
            score >= 80 -> 4
            score >= 60 -> 3
            else -> 2
        }
        GameSuccessCelebrationDialog(
            gameTitle = "Memory Safari",
            score = score,
            totalQuestions = animalItems.size,
            correctAnswers = matchedPairs,
            starsEarned = stars,
            onPlayAgain = { restartGame() },
            onBackHome = onBack
        )
    }
}
