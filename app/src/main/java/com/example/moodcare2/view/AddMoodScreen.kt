package com.example.moodcare2.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moodcare2.MoodCareApplication
import com.example.moodcare2.R
import com.example.moodcare2.viewmodel.CreateMoodViewModel
import com.example.moodcare2.viewmodel.MoodState
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMoodScreen(navController: NavController) {
    val context = LocalContext.current
    val application = context.applicationContext as MoodCareApplication
    val viewModel = remember { CreateMoodViewModel(application.repository) }

    var selectedMood by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var halBersyukur by remember { mutableStateOf("") }
    var halSedih by remember { mutableStateOf("") }
    var halPerbaikan by remember { mutableStateOf("") }
    var showConfirmDialog by remember { mutableStateOf(false) }

    val moodState by viewModel.moodState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(moodState) {
        if (moodState is MoodState.OperationSuccess) {
            snackbarHostState.showSnackbar(
                message = "✨ Mood berhasil disimpan!",
                duration = SnackbarDuration.Short
            )
            delay(800)
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Catat Mood",
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = colorResource(R.color.PinkSecondary),
                    navigationIconContentColor = colorResource(R.color.PinkSecondary)
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = colorResource(R.color.PinkSecondary),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(16.dp)
                )
            }
        },
        containerColor = colorResource(R.color.PinkBackground)
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Halo! Bagaimana perasaanmu?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.PinkDark)
                    )
                    Spacer(Modifier.height(16.dp))

                    MoodSelector(
                        selectedMood = selectedMood,
                        onMoodSelected = { selectedMood = it }
                    )
                }
            }

            item {
                Text(
                    "Tuangkan pikiranmu...",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }

            item {
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

                        AddMoodTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = stringResource(R.string.description),
                            placeholder = "Apa yang terjadi hari ini?",
                            icon = "📝"
                        )

                        AddMoodTextField(
                            value = halBersyukur,
                            onValueChange = { halBersyukur = it },
                            label = stringResource(R.string.hal_bersyukur),
                            placeholder = "Aku bersyukur atas...",
                            icon = "🙏"
                        )

                        AddMoodTextField(
                            value = halSedih,
                            onValueChange = { halSedih = it },
                            label = stringResource(R.string.hal_sedih),
                            placeholder = "Hari ini terasa berat karena...",
                            icon = "😔"
                        )

                        AddMoodTextField(
                            value = halPerbaikan,
                            onValueChange = { halPerbaikan = it },
                            label = stringResource(R.string.hal_perbaikan),
                            placeholder = "Besok aku ingin lebih...",
                            icon = "💪"
                        )
                    }
                }
            }

            if (moodState is MoodState.Error) {
                item {
                    Surface(
                        color = Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            (moodState as MoodState.Error).message,
                            modifier = Modifier.padding(12.dp),
                            color = Color(0xFFDC143C),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = selectedMood.isNotEmpty() && moodState !is MoodState.Loading,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.PinkSecondary)),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    if (moodState is MoodState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Icon(Icons.Default.Check, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Simpan Catatan Mood", fontWeight = FontWeight.Bold)
                    }
                }
            }

            item { Spacer(Modifier.height(20.dp)) }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            shape = RoundedCornerShape(28.dp),
            title = { Text("Simpan Catatan Mood?") },
            text = { Text(stringResource(R.string.tanya1)) },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        viewModel.createMood(selectedMood, description, halBersyukur, halSedih, halPerbaikan, today)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.PinkSecondary))
                ) { Text("Ya, Simpan") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) { Text("Batal", color = Color.Gray) }
            }
        )
    }
}

@Composable
fun MoodSelector(selectedMood: String, onMoodSelected: (String) -> Unit) {
    val moods = listOf(
        "Sangat Baik" to "😄",
        stringResource(R.string.senang) to "😊",
        stringResource(R.string.netral) to "😐",
        stringResource(R.string.buruk) to "😔",
        stringResource(R.string.sangat_buruk) to "😢"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        moods.forEach { (mood, emoji) ->
            val isSelected = selectedMood == mood
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onMoodSelected(mood) }
                    .padding(4.dp)
            ) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    color = if (isSelected) colorResource(R.color.PinkSecondary) else Color.White,
                    border = if (!isSelected) BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)) else null,
                    shadowElevation = if (isSelected) 6.dp else 0.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(emoji, style = MaterialTheme.typography.headlineSmall)
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    mood.split(" ").last(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) colorResource(R.color.PinkSecondary) else Color.Gray,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun AddMoodTextField(value: String, onValueChange: (String) -> Unit, label: String, placeholder: String, icon: String) {
    Column {
        Text(
            "$icon $label",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.PinkDark),
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.LightGray, style = MaterialTheme.typography.bodySmall) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.PinkPrimary),
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.4f)
            )
        )
    }
}

