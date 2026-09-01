package com.example.ui.kids.games

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChildProfile
import com.example.data.model.GameCategory
import com.example.ui.components.GameSuccessCelebrationDialog
import com.example.ui.theme.ParentNavy
import com.example.ui.theme.PlayfulBlue
import com.example.ui.theme.PlayfulCoral
import com.example.ui.theme.PlayfulGreen
import com.example.ui.theme.PlayfulOrange
import com.example.ui.theme.PlayfulPurple
import com.example.ui.theme.PlayfulSky
import com.example.ui.theme.PlayfulTeal
import com.example.ui.theme.PlayfulYellow
import com.example.ui.theme.StarGold
import kotlinx.coroutines.delay

data class WordChallenge(
    val word: String,
    val emoji: String,
    val categoryHint: String,
    val funFact: String
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun WordExplorerScreen(
    child: ChildProfile?,
    onBack: () -> Unit,
    onGameFinished: (category: GameCategory, title: String, score: Int, total: Int, correct: Int, duration: Int, stars: Int) -> Unit
) {
    val wordBank = remember {
        listOf(
            WordChallenge("LION", "🦁", "King of the Jungle", "Lions live in family groups called prides!"),
            WordChallenge("TIGER", "🐯", "Striped Big Cat", "Every tiger has a unique pattern of stripes!"),
            WordChallenge("PANDA", "🐼", "Bamboo Lover", "Pandas eat up to 26 to 84 pounds of bamboo every day!"),
            WordChallenge("EAGLE", "🦅", "Sky Hunter", "Eagles have super sharp eyesight to spot fish from high above!"),
            WordChallenge("WHALE", "🐋", "Ocean Giant", "Blue whales are the largest animals ever known to live on Earth!"),
            WordChallenge("ZEBRA", "🦓", "Savannah Runner", "Zebras stripes help confuse predators in the wild!"),
            WordChallenge("KOALA", "🐨", "Eucalyptus Friend", "Koalas sleep up to 18-20 hours every day!"),
            WordChallenge("APPLE", "🍎", "Sweet Fruit", "Apples float in water because 25% of their volume is air!")
        ).shuffled().take(5)
    }

    val totalQuestions = wordBank.size
    var currentWordIndex by remember { mutableIntStateOf(0) }
    val currentChallenge = wordBank.getOrElse(currentWordIndex) { wordBank.first() }

    val enteredLetters = remember { mutableStateListOf<Char>() }
    val availableLetters = remember(currentChallenge) {
        val extraLetters = listOf('A', 'E', 'I', 'O', 'U', 'S', 'R', 'T', 'N', 'L')
            .filter { !currentChallenge.word.contains(it) }
            .shuffled()
            .take(2)
        (currentChallenge.word.toList() + extraLetters).shuffled()
    }
    val usedIndices = remember { mutableStateListOf<Int>() }

    var correctCount by remember { mutableIntStateOf(0) }
    var isWordCompleted by remember { mutableStateOf(false) }
    var isGameOver by remember { mutableStateOf(false) }
    var showHint by remember { mutableStateOf(false) }
    val startTime = remember { mutableLongStateOf(System.currentTimeMillis()) }

    fun resetWordState() {
        enteredLetters.clear()
        usedIndices.clear()
        isWordCompleted = false
        showHint = false
    }

    fun restartAll() {
        currentWordIndex = 0
        correctCount = 0
        isGameOver = false
        startTime.longValue = System.currentTimeMillis()
        resetWordState()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📚 Word Explorer", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("word_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PlayfulTeal
                )
            )
        },
        containerColor = Color(0xFFFBFCF8)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Level progress
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Word ${currentWordIndex + 1} of $totalQuestions",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = PlayfulTeal
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "⭐ $correctCount Solved",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B6A2B)
                    )
                )
            }

            LinearProgressIndicator(
                progress = { (currentWordIndex + 1).toFloat() / totalQuestions.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = PlayfulTeal,
                trackColor = Color(0xFFEDF3EB)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Clue Card with Large Emoji
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFDCE5DB), RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEDF3EB)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = currentChallenge.emoji, fontSize = 54.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = currentChallenge.categoryHint,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PlayfulTeal
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Spell the word by tapping the letters below!",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF717970)),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Target Letter Slots
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                for (i in 0 until currentChallenge.word.length) {
                    val letter = enteredLetters.getOrNull(i)
                    val isCurrentTarget = enteredLetters.size == i

                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                when {
                                    letter != null -> PlayfulTeal
                                    isCurrentTarget -> Color(0xFFE0ECE4)
                                    else -> Color(0xFFEDF3EB)
                                }
                            )
                            .border(
                                width = if (isCurrentTarget) 2.dp else 1.dp,
                                color = if (isCurrentTarget) PlayfulTeal else Color(0xFFDCE5DB),
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = letter?.toString() ?: "",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = if (letter != null) Color.White else PlayfulTeal
                            )
                        )
                    }
                }
            }

            // Word completed success feedback
            AnimatedVisibility(visible = isWordCompleted) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFDCF0DF)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Correct",
                            tint = PlayfulGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Super! ${currentChallenge.funFact}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF235527)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Available Letter Bubble Buttons
            Text(
                text = "Letter Choices:",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = ParentNavy
                ),
                modifier = Modifier.padding(bottom = 10.dp)
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                availableLetters.forEachIndexed { index, char ->
                    val isUsed = usedIndices.contains(index)

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isUsed) Color(0xFFE2ECE0) else Color.White)
                            .border(
                                width = 1.5.dp,
                                color = if (isUsed) Color.Transparent else Color(0xFFDCE5DB),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable(enabled = !isUsed && !isWordCompleted) {
                                if (enteredLetters.size < currentChallenge.word.length) {
                                    enteredLetters.add(char)
                                    usedIndices.add(index)

                                    // Check if full word entered
                                    if (enteredLetters.size == currentChallenge.word.length) {
                                        val spelled = enteredLetters.joinToString("")
                                        if (spelled.equals(currentChallenge.word, ignoreCase = true)) {
                                            isWordCompleted = true
                                            correctCount++
                                        } else {
                                            // Reset if incorrect
                                            enteredLetters.clear()
                                            usedIndices.clear()
                                        }
                                    }
                                }
                            }
                            .testTag("letter_button_${char}_$index"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char.toString(),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = if (isUsed) Color.LightGray else ParentNavy
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Reset current word button
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { resetWordState() },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEDF3EB)),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Clear", tint = ParentNavy, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Clear Word", color = ParentNavy, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { showHint = !showHint },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBF4D8)),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = "Hint", tint = Color(0xFF8B6A2B), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Hint", color = Color(0xFF8B6A2B), fontWeight = FontWeight.Bold)
                }
            }

            if (showHint) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Hint: Starts with letter '${currentChallenge.word.first()}' and ends with '${currentChallenge.word.last()}'",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF8B6A2B),
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            // Advance automatically when word complete
            LaunchedEffect(isWordCompleted) {
                if (isWordCompleted) {
                    delay(2000)
                    if (currentWordIndex < totalQuestions - 1) {
                        currentWordIndex++
                        resetWordState()
                    } else {
                        isGameOver = true
                        val duration = ((System.currentTimeMillis() - startTime.longValue) / 1000).toInt()
                        val score = (correctCount * 100) / totalQuestions
                        val stars = when {
                            score == 100 -> 5
                            score >= 80 -> 4
                            score >= 60 -> 3
                            score >= 40 -> 2
                            else -> 1
                        }
                        onGameFinished(
                            GameCategory.SPELLING,
                            "Word Jungle Quest",
                            score,
                            totalQuestions,
                            correctCount,
                            duration,
                            stars
                        )
                    }
                }
            }
        }
    }

    if (isGameOver) {
        val duration = ((System.currentTimeMillis() - startTime.longValue) / 1000).toInt()
        val score = (correctCount * 100) / totalQuestions
        val stars = when {
            score == 100 -> 5
            score >= 80 -> 4
            score >= 60 -> 3
            score >= 40 -> 2
            else -> 1
        }
        GameSuccessCelebrationDialog(
            gameTitle = "Word Explorer",
            score = score,
            totalQuestions = totalQuestions,
            correctAnswers = correctCount,
            starsEarned = stars,
            onPlayAgain = { restartAll() },
            onBackHome = onBack
        )
    }
}
