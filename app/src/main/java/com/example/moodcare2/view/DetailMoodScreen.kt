package com.example.moodcare2.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moodcare2.MoodCareApplication
import com.example.moodcare2.R
import com.example.moodcare2.data.model.Mood
import com.example.moodcare2.view.navigation.Screen
import com.example.moodcare2.viewmodel.EditMoodViewModel
import com.example.moodcare2.viewmodel.CreateMoodViewModel
import androidx.compose.ui.res.colorResource
import com.example.moodcare2.viewmodel.MoodState
import com.example.moodcare2.utils.getMoodColor
import com.example.moodcare2.utils.getMoodLabel
import com.example.moodcare2.utils.getMoodEmoji

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMoodScreen(navController: NavController, moodId: Int) {
    val context = LocalContext.current
    val application = context.applicationContext as MoodCareApplication
    val editViewModel = remember { EditMoodViewModel(application.repository) }
    val createViewModel = remember { CreateMoodViewModel(application.repository) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var mood by remember { mutableStateOf<Mood?>(null) }

    val moodState by editViewModel.moodState.collectAsState()
    val deleteState by createViewModel.moodState.collectAsState()

    LaunchedEffect(moodId) {
        editViewModel.getMoodById(moodId)
    }

    LaunchedEffect(moodState) {
        if (moodState is MoodState.SingleMoodSuccess) {
            mood = (moodState as MoodState.SingleMoodSuccess).mood
        }
    }

    LaunchedEffect(deleteState) {
        if (deleteState is MoodState.OperationSuccess) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detail Mood", fontWeight = FontWeight.ExtraBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("${Screen.EditMood.route}/$moodId") }) {
                        Icon(Icons.Default.Edit, null)
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.DeleteOutline, null, tint = Color(0xFFDC143C))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = colorResource(R.color.PinkSecondary),
                    navigationIconContentColor = colorResource(R.color.PinkSecondary),
                    actionIconContentColor = colorResource(R.color.PinkSecondary)
                )
            )
        },
        containerColor = colorResource(R.color.PinkBackground)
    ) { paddingValues ->
        when (moodState) {
            is MoodState.Loading -> {
                Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colorResource(R.color.PinkPrimary))
                }
            }
            is MoodState.Error -> {
                Box(Modifier.fillMaxSize().padding(paddingValues).padding(20.dp), contentAlignment = Alignment.Center) {
                    ErrorCard((moodState as MoodState.Error).message)
                }
            }
            else -> {
                mood?.let { currentMood ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(32.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                getMoodColor(currentMood.mood_level),
                                                getMoodColor(currentMood.mood_level).copy(alpha = 0.6f)
                                            )
                                        )
                                    )
                                    .padding(vertical = 40.dp, horizontal = 24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Surface(
                                        modifier = Modifier.size(100.dp),
                                        shape = CircleShape,
                                        color = Color.White.copy(alpha = 0.2f)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(getMoodEmoji(currentMood.mood_level), style = MaterialTheme.typography.displayLarge)
                                        }
                                    }
                                    Spacer(Modifier.height(16.dp))
                                    Text(
                                        getMoodLabel(currentMood.mood_level),
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                    Surface(
                                        modifier = Modifier.padding(top = 12.dp),
                                        shape = RoundedCornerShape(50.dp),
                                        color = Color.Black.copy(alpha = 0.1f)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.Event, null, modifier = Modifier.size(14.dp), tint = Color.White)
                                            Spacer(Modifier.width(6.dp))
                                            Text(currentMood.tanggal, style = MaterialTheme.typography.labelLarge, color = Color.White)
                                        }
                                    }
                                }
                            }
                        }

                        Text("Catatan Hari Ini", fontWeight = FontWeight.Bold, color = colorResource(R.color.PinkDark), style = MaterialTheme.typography.titleMedium)

                        if (!currentMood.description.isNullOrBlank()) {
                            DetailCard(
                                icon = Icons.Default.ChatBubbleOutline,
                                title = "Deskripsi",
                                content = currentMood.description,
                                accentColor = Color(0xFF6C63FF)
                            )
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.weight(1f)) {
                                if (!currentMood.hal_bersyukur.isNullOrBlank()) {
                                    DetailCard(Icons.Default.AutoAwesome, "Syukur", currentMood.hal_bersyukur, Color(0xFF26A69A))
                                }
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                if (!currentMood.hal_sedih.isNullOrBlank()) {
                                    DetailCard(Icons.Default.SentimentVeryDissatisfied, "Sedih", currentMood.hal_sedih, Color(0xFFFF6B9D))
                                }
                            }
                        }

                        if (!currentMood.hal_perbaikan.isNullOrBlank()) {
                            DetailCard(
                                icon = Icons.Default.FlashOn,
                                title = "Rencana Perbaikan",
                                content = currentMood.hal_perbaikan,
                                accentColor = Color(0xFFFFA726)
                            )
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.5f)),
                            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
                        ) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Metadata", style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontWeight = FontWeight.Bold)
                                InfoRow("Dibuat pada", currentMood.created_at ?: "-")
                                InfoRow("Terakhir update", currentMood.updated_at ?: "-")
                            }
                        }

                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            shape = RoundedCornerShape(28.dp),
            icon = { Icon(Icons.Default.DeleteForever, null, tint = Color(0xFFDC143C), modifier = Modifier.size(32.dp)) },
            title = { Text("Hapus Catatan?") },
            text = { Text("Data yang sudah dihapus tidak dapat dikembalikan. Apakah Anda yakin?") },
            confirmButton = {
                Button(
                    onClick = { showDeleteDialog = false; createViewModel.deleteMood(moodId) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC143C)),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Hapus Sekarang") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Batal", color = Color.Gray) }
            }
        )
    }
}

@Composable
fun DetailCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    content: String,
    accentColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = accentColor, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = accentColor)
            }
            Spacer(Modifier.height(10.dp))
            Text(content, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray, lineHeight = androidx.compose.ui.unit.TextUnit.Unspecified)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.DarkGray)
    }
}