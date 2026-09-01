package com.example.ui.kids.games

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RocketLaunch
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
import androidx.compose.ui.graphics.Brush
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
import com.example.ui.theme.PlayfulIndigo
import com.example.ui.theme.PlayfulOrange
import com.example.ui.theme.PlayfulSky
import com.example.ui.theme.PlayfulYellow
import com.example.ui.theme.PrimaryKids
import com.example.ui.theme.StarGold
import kotlinx.coroutines.delay
import kotlin.random.Random

data class MathQuestion(
    val prompt: String,
    val visualItems: String? = null,
    val visualCount: Int = 0,
    val options: List<Int>,
    val correctAnswer: Int
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MathGalaxyScreen(
    child: ChildProfile?,
    onBack: () -> Unit,
    onGameFinished: (category: GameCategory, title: String, score: Int, total: Int, correct: Int, duration: Int, stars: Int) -> Unit
) {
    val age = child?.age ?: 7
    val totalQuestions = 5
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var correctCount by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var isAnswerChecked by remember { mutableStateOf(false) }
    var isGameOver by remember { mutableStateOf(false) }
    val startTime = remember { mutableLongStateOf(System.currentTimeMillis()) }

    fun generateQuestionsForAge(age: Int): List<MathQuestion> {
        val list = mutableListOf<MathQuestion>()
        repeat(totalQuestions) { index ->
            when {
                age <= 5 -> {
                    // Toddler counting and small additions
                    if (index % 2 == 0) {
                        val count = Random.nextInt(2, 7)
                        val emojis = listOf("⭐", "🍎", "🚀", "🐱", "🎈", "🐸").random()
                        val correct = count
                        val wrong1 = (count + 1).coerceAtMost(9)
                        val wrong2 = (count - 1).coerceAtLeast(1)
                        val options = listOf(correct, wrong1, wrong2).shuffled()
                        list.add(
                            MathQuestion(
                                prompt = "Count the $emojis in space!",
                                visualItems = emojis,
                                visualCount = count,
                                options = options,
                                correctAnswer = correct
                            )
                        )
                    } else {
                        val a = Random.nextInt(1, 4)
                        val b = Random.nextInt(1, 4)
                        val correct = a + b
                        val options = listOf(correct, correct + 1, (correct - 1).coerceAtLeast(1)).shuffled()
                        list.add(
                            MathQuestion(
                                prompt = "$a + $b = ?",
                                visualItems = "🚀",
                                visualCount = 0,
                                options = options,
                                correctAnswer = correct
                            )
                        )
                    }
                }
                age in 6..8 -> {
                    // Addition and Subtraction
                    val isPlus = Random.nextBoolean()
                    if (isPlus) {
                        val a = Random.nextInt(3, 15)
                        val b = Random.nextInt(2, 12)
                        val correct = a + b
                        val options = listOf(correct, correct + Random.nextInt(1, 4), (correct - Random.nextInt(1, 4)).coerceAtLeast(1)).distinct()
                        val padded = (options + listOf(correct + 5, correct - 2)).distinct().take(3).shuffled()
                        list.add(
                            MathQuestion(
                                prompt = "$a + $b = ?",
                                options = padded,
                                correctAnswer = correct
                            )
                        )
                    } else {
                        val a = Random.nextInt(8, 20)
                        val b = Random.nextInt(2, a)
                        val correct = a - b
                        val options = listOf(correct, correct + Random.nextInt(1, 3), (correct - 1).coerceAtLeast(0)).distinct().take(3).shuffled()
                        list.add(
                            MathQuestion(
                                prompt = "$a - $b = ?",
                                options = options,
                                correctAnswer = correct
                            )
                        )
                    }
                }
                else -> {
                    // Multiplication, Division, mixed
                    val op = listOf("x", "+", "-").random()
                    when (op) {
                        "x" -> {
                            val a = Random.nextInt(3, 10)
                            val b = Random.nextInt(2, 9)
                            val correct = a * b
                            val options = listOf(correct, correct + a, correct - b).distinct().take(3).shuffled()
                            list.add(
                                MathQuestion(
                                    prompt = "$a × $b = ?",
                                    options = options,
                                    correctAnswer = correct
                                )
                            )
                        }
                        "+" -> {
                            val a = Random.nextInt(15, 55)
                            val b = Random.nextInt(12, 45)
                            val correct = a + b
                            val options = listOf(correct, correct + 10, correct - 5).shuffled()
                            list.add(
                                MathQuestion(
                                    prompt = "$a + $b = ?",
                                    options = options,
                                    correctAnswer = correct
                                )
                            )
                        }
                        else -> {
                            val a = Random.nextInt(25, 60)
                            val b = Random.nextInt(10, 24)
                            val correct = a - b
                            val options = listOf(correct, correct + 2, correct - 3).shuffled()
                            list.add(
                                MathQuestion(
                                    prompt = "$a - $b = ?",
                                    options = options,
                                    correctAnswer = correct
                                )
                            )
                        }
                    }
                }
            }
        }
        return list
    }

    val questions = remember(age) { generateQuestionsForAge(age) }
    val currentQuestion = questions.getOrElse(currentQuestionIndex) { questions.first() }

    fun restartGame() {
        currentQuestionIndex = 0
        correctCount = 0
        selectedOption = null
        isAnswerChecked = false
        isGameOver = false
        startTime.longValue = System.currentTimeMillis()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🚀 Math Galaxy", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("math_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PlayfulIndigo
                )
            )
        },
        containerColor = Color(0xFF0F172A)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Rocket Progress Tracker
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mission ${currentQuestionIndex + 1}/$totalQuestions",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = PlayfulSky
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⭐ $correctCount Correct", color = StarGold, fontWeight = FontWeight.Bold)
                }
            }

            LinearProgressIndicator(
                progress = { (currentQuestionIndex + 1).toFloat() / totalQuestions.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = PlayfulYellow,
                trackColor = Color(0xFF334155),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Main Question Card (Cosmic Portal)
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E293B)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.dp, PlayfulIndigo.copy(alpha = 0.6f), RoundedCornerShape(28.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(PlayfulIndigo, PlayfulSky))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.RocketLaunch,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = currentQuestion.prompt,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    )

                    // Visual Items (if toddler counting)
                    if (currentQuestion.visualItems != null && currentQuestion.visualCount > 0) {
                        Spacer(modifier = Modifier.height(16.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.Center,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF0F172A))
                                .padding(16.dp)
                        ) {
                            repeat(currentQuestion.visualCount) {
                                Text(
                                    text = currentQuestion.visualItems,
                                    fontSize = 32.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Tap the correct answer:",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PlayfulSky
                ),
                modifier = Modifier.padding(bottom = 14.dp)
            )

            // Multiple Choice Options
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                currentQuestion.options.forEach { option ->
                    val isSelected = selectedOption == option
                    val isCorrect = option == currentQuestion.correctAnswer

                    val bgColor by animateColorAsState(
                        targetValue = when {
                            isAnswerChecked && isCorrect -> PlayfulGreen
                            isAnswerChecked && isSelected && !isCorrect -> PlayfulCoral
                            isSelected -> PlayfulIndigo
                            else -> Color(0xFF1E293B)
                        },
                        label = "btnColor"
                    )

                    Card(
                        onClick = {
                            if (!isAnswerChecked) {
                                selectedOption = option
                                isAnswerChecked = true
                                if (isCorrect) {
                                    correctCount++
                                }
                            }
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = bgColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) PlayfulYellow else Color(0xFF334155),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .testTag("math_option_$option")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "$option",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    fontSize = 24.sp
                                )
                            )

                            if (isAnswerChecked) {
                                if (isCorrect) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Correct",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                } else if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Incorrect",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
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
                    delay(1200)
                    if (currentQuestionIndex < totalQuestions - 1) {
                        currentQuestionIndex++
                        selectedOption = null
                        isAnswerChecked = false
                    } else {
                        // Game Over!
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
                            GameCategory.MATH,
                            "Math Galaxy Adventure",
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
            gameTitle = "Math Galaxy",
            score = score,
            totalQuestions = totalQuestions,
            correctAnswers = correctCount,
            starsEarned = stars,
            onPlayAgain = { restartGame() },
            onBackHome = onBack
        )
    }
}
