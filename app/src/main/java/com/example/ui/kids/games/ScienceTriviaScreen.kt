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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Science
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
import com.example.ui.theme.PlayfulPurple
import com.example.ui.theme.PlayfulSky
import com.example.ui.theme.StarGold
import kotlinx.coroutines.delay

data class ScienceQuestion(
    val question: String,
    val iconEmoji: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScienceTriviaScreen(
    child: ChildProfile?,
    onBack: () -> Unit,
    onGameFinished: (category: GameCategory, title: String, score: Int, total: Int, correct: Int, duration: Int, stars: Int) -> Unit
) {
    val questions = remember {
        listOf(
            ScienceQuestion(
                question = "How many hearts does an octopus have?",
                iconEmoji = "🐙",
                options = listOf("1 Heart", "2 Hearts", "3 Hearts", "5 Hearts"),
                correctIndex = 2,
                explanation = "An octopus has 3 hearts! Two pump blood to its gills, and one pumps blood to the rest of its body."
            ),
            ScienceQuestion(
                question = "Which planet is famous for having giant glowing rings?",
                iconEmoji = "🪐",
                options = listOf("Mars", "Saturn", "Venus", "Mercury"),
                correctIndex = 1,
                explanation = "Saturn has the most spectacular ring system in our solar system made of ice and rock chunks!"
            ),
            ScienceQuestion(
                question = "What do bees collect from flowers to make sweet honey?",
                iconEmoji = "🐝",
                options = listOf("Nectar", "Water drops", "Leaves", "Seeds"),
                correctIndex = 0,
                explanation = "Bees collect sugary nectar from flowers and store it in their honeycombs!"
            ),
            ScienceQuestion(
                question = "Which is the fastest land animal in the world?",
                iconEmoji = "🐆",
                options = listOf("Lion", "Cheetah", "Horse", "Kangaroo"),
                correctIndex = 1,
                explanation = "Cheetahs can run up to 70 miles per hour in short explosive bursts!"
            ),
            ScienceQuestion(
                question = "Why do trees need sunlight?",
                iconEmoji = "🌳",
                options = listOf("To stay warm", "Photosynthesis to make food", "To attract birds", "To grow bark"),
                correctIndex = 1,
                explanation = "Leaves capture sunlight to convert water and carbon dioxide into food (photosynthesis)!"
            )
        ).shuffled()
    }

    val totalQuestions = questions.size
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswerChecked by remember { mutableStateOf(false) }
    var correctCount by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }
    val startTime = remember { mutableLongStateOf(System.currentTimeMillis()) }

    val currentQuestion = questions.getOrElse(currentQuestionIndex) { questions.first() }

    fun restartGame() {
        currentQuestionIndex = 0
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
                    Text("🔬 Science & Nature", fontWeight = FontWeight.Bold, color = Color.White)
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("science_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PlayfulPurple
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
            // Header Progress
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question ${currentQuestionIndex + 1}/$totalQuestions",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = PlayfulPurple
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
                progress = { (currentQuestionIndex + 1).toFloat() / totalQuestions.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = PlayfulPurple,
                trackColor = Color(0xFFEDF3EB)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Question Card
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
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8EEF5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = currentQuestion.iconEmoji, fontSize = 42.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = currentQuestion.question,
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
                currentQuestion.options.forEachIndexed { index, option ->
                    val isSelected = selectedOptionIndex == index
                    val isCorrect = index == currentQuestion.correctIndex

                    val bgColor by animateColorAsState(
                        targetValue = when {
                            isAnswerChecked && isCorrect -> Color(0xFFDCF0DF)
                            isAnswerChecked && isSelected && !isCorrect -> Color(0xFFFBE6E2)
                            isSelected -> Color(0xFFEDF3EB)
                            else -> Color.White
                        },
                        label = "optBg"
                    )

                    val borderColor by animateColorAsState(
                        targetValue = when {
                            isAnswerChecked && isCorrect -> PlayfulGreen
                            isAnswerChecked && isSelected && !isCorrect -> PlayfulCoral
                            isSelected -> PlayfulPurple
                            else -> Color(0xFFDCE5DB)
                        },
                        label = "optBorder"
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
                            .testTag("science_option_$index")
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

            // Educational explanation callout
            AnimatedVisibility(visible = isAnswerChecked) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF4D8)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Fact",
                            tint = Color(0xFF8B6A2B),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = currentQuestion.explanation,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF6B501B)
                            )
                        )
                    }
                }
            }

            // Auto-advance logic
            LaunchedEffect(isAnswerChecked) {
                if (isAnswerChecked) {
                    delay(2600)
                    if (currentQuestionIndex < totalQuestions - 1) {
                        currentQuestionIndex++
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
                            GameCategory.SCIENCE,
                            "Science & Nature Expedition",
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
            gameTitle = "Science & Nature",
            score = score,
            totalQuestions = totalQuestions,
            correctAnswers = correctCount,
            starsEarned = stars,
            onPlayAgain = { restartGame() },
            onBackHome = onBack
        )
    }
}
