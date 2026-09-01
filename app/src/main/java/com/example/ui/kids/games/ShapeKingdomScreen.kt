package com.example.ui.kids.games

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import com.example.ui.theme.PlayfulCoral
import com.example.ui.theme.PlayfulGreen
import com.example.ui.theme.PlayfulOrange
import com.example.ui.theme.PlayfulPink
import com.example.ui.theme.PlayfulSky
import com.example.ui.theme.PlayfulTeal
import com.example.ui.theme.PlayfulYellow
import com.example.ui.theme.StarGold
import kotlinx.coroutines.delay

data class ShapeChallenge(
    val prompt: String,
    val visualDisplay: String,
    val options: List<String>,
    val correctIndex: Int,
    val hint: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShapeKingdomScreen(
    child: ChildProfile?,
    onBack: () -> Unit,
    onGameFinished: (category: GameCategory, title: String, score: Int, total: Int, correct: Int, duration: Int, stars: Int) -> Unit
) {
    val challenges = remember {
        listOf(
            ShapeChallenge(
                prompt = "What shape has 3 sides and 3 corners?",
                visualDisplay = "🔺",
                options = listOf("Square", "Triangle", "Circle", "Rectangle"),
                correctIndex = 1,
                hint = "Tri means three, like a tricycle!"
            ),
            ShapeChallenge(
                prompt = "What color do you get when you mix Red 🔴 + Blue 🔵?",
                visualDisplay = "🔴 + 🔵 = ?",
                options = listOf("Green", "Orange", "Purple", "Yellow"),
                correctIndex = 2,
                hint = "It's the color of yummy grapes and royalty!"
            ),
            ShapeChallenge(
                prompt = "Complete the magical pattern: 🟡 🔷 🟡 🔷 ___",
                visualDisplay = "🟡 🔷 🟡 🔷 [ ? ]",
                options = listOf("🟡 Yellow Circle", "🔷 Blue Diamond", "🔴 Red Square", "⭐ Star"),
                correctIndex = 0,
                hint = "Look at the repeating alternating colors!"
            ),
            ShapeChallenge(
                prompt = "What color do you get when you mix Yellow 🟡 + Blue 🔵?",
                visualDisplay = "🟡 + 🔵 = ?",
                options = listOf("Green", "Pink", "Black", "Brown"),
                correctIndex = 0,
                hint = "It's the color of fresh green leaves and grass!"
            ),
            ShapeChallenge(
                prompt = "Which shape is round with NO sharp corners?",
                visualDisplay = "⭕",
                options = listOf("Diamond", "Triangle", "Circle", "Hexagon"),
                correctIndex = 2,
                hint = "Like a full moon or a pizza pie!"
            )
        ).shuffled()
    }

    val totalQuestions = challenges.size
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswerChecked by remember { mutableStateOf(false) }
    var correctCount by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }
    val startTime = remember { mutableLongStateOf(System.currentTimeMillis()) }

    val currentChallenge = challenges.getOrElse(currentIndex) { challenges.first() }

    fun restartGame() {
        currentIndex = 0
        selectedOptionIndex = null
        isAnswerChecked = false
        correctCount = 0
        isGameOver = false
        startTime.longValue = System.currentTimeMillis()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("🎨 Color & Shapes", fontWeight = FontWeight.Bold, color = Color.White)
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("shapes_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PlayfulOrange
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
                    text = "Puzzle ${currentIndex + 1}/$totalQuestions",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = PlayfulOrange
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "⭐ $correctCount Correct",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B6A2B)
                    )
                )
            }

            LinearProgressIndicator(
                progress = { (currentIndex + 1).toFloat() / totalQuestions.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = PlayfulOrange,
                trackColor = Color(0xFFEDF3EB)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Main Display Card
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
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFFFBF4D8)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentChallenge.visualDisplay,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = currentChallenge.prompt,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = ParentNavy,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Options List
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                currentChallenge.options.forEachIndexed { index, option ->
                    val isSelected = selectedOptionIndex == index
                    val isCorrect = index == currentChallenge.correctIndex

                    val bgColor by animateColorAsState(
                        targetValue = when {
                            isAnswerChecked && isCorrect -> Color(0xFFDCF0DF)
                            isAnswerChecked && isSelected && !isCorrect -> Color(0xFFFBE6E2)
                            isSelected -> Color(0xFFEDF3EB)
                            else -> Color.White
                        },
                        label = "shapeOptBg"
                    )

                    val borderColor by animateColorAsState(
                        targetValue = when {
                            isAnswerChecked && isCorrect -> PlayfulGreen
                            isAnswerChecked && isSelected && !isCorrect -> PlayfulCoral
                            isSelected -> PlayfulOrange
                            else -> Color(0xFFDCE5DB)
                        },
                        label = "shapeBorder"
                    )

                    Card(
                        onClick = {
                            if (!isAnswerChecked) {
                                selectedOptionIndex = index
                                isAnswerChecked = true
                                if (isCorrect) {
                                    correctCount++
                                }
                            }
                        },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = bgColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(width = 1.5.dp, color = borderColor, shape = RoundedCornerShape(18.dp))
                            .testTag("shape_option_$index")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = option,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = ParentNavy
                                )
                            )

                            if (isAnswerChecked) {
                                if (isCorrect) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Correct",
                                        tint = PlayfulGreen,
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Incorrect",
                                        tint = PlayfulCoral,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Auto-advance logic
            LaunchedEffect(isAnswerChecked) {
                if (isAnswerChecked) {
                    delay(1800)
                    if (currentIndex < totalQuestions - 1) {
                        currentIndex++
                        selectedOptionIndex = null
                        isAnswerChecked = false
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
                            GameCategory.SHAPES,
                            "Shape & Color Magic",
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
            gameTitle = "Color & Shapes",
            score = score,
            totalQuestions = totalQuestions,
            correctAnswers = correctCount,
            starsEarned = stars,
            onPlayAgain = { restartGame() },
            onBackHome = onBack
        )
    }
}
