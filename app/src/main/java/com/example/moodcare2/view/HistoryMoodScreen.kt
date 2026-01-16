package com.example.moodcare2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moodcare2.MoodCareApplication
import com.example.moodcare2.R
import com.example.moodcare2.view.navigation.Screen
import androidx.compose.ui.res.colorResource
import com.example.moodcare2.viewmodel.CreateMoodViewModel
import com.example.moodcare2.viewmodel.MoodState
import com.example.moodcare2.utils.getMoodLabel
import com.example.moodcare2.utils.getMoodEmoji
import java.text.SimpleDateFormat
import java.util.*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodHistoryScreen(navController: NavController) {
    val context = LocalContext.current
    val application = context.applicationContext as MoodCareApplication
    val viewModel = remember { CreateMoodViewModel(application.repository) }

    var searchDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    val moodState by viewModel.moodState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAllMoods()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.history),
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, stringResource(R.string.back))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = colorResource(R.color.PinkSecondary),
                    navigationIconContentColor = colorResource(R.color.PinkSecondary)
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Screen.AddMood.route) },
                containerColor = colorResource(R.color.PinkSecondary),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text("Tambah Mood")
            }
        },
        containerColor = colorResource(R.color.PinkBackground)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        stringResource(R.string.search_by_date),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.PinkDark)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { showDatePicker = true }
                        ) {
                            OutlinedTextField(
                                value = searchDate,
                                onValueChange = { },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Pilih Tanggal", color = Color.LightGray) },
                                readOnly = true,
                                enabled = false, // Agar klik ditangkap oleh Box
                                shape = RoundedCornerShape(14.dp),
                                leadingIcon = {
                                    Icon(Icons.Default.CalendarMonth, null, tint = colorResource(R.color.PinkPrimary))
                                },
                                trailingIcon = {
                                    if (searchDate.isNotEmpty()) {
                                        IconButton(onClick = {
                                            searchDate = ""
                                            viewModel.getAllMoods() // Reset pencarian
                                        }) {
                                            Icon(Icons.Default.Cancel, null, tint = Color.Gray)
                                        }
                                    }
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = Color.Black,
                                    disabledBorderColor = Color.LightGray.copy(alpha = 0.4f),
                                    disabledPlaceholderColor = Color.LightGray,
                                    disabledLeadingIconColor = colorResource(R.color.PinkPrimary),
                                    disabledContainerColor = colorResource(R.color.PinkBackground).copy(alpha = 0.3f)
                                )
                            )
                        }

                        IconButton(
                            onClick = {
                                if (searchDate.isNotEmpty()) viewModel.searchMoodByDate(searchDate)
                            },
                            enabled = searchDate.isNotEmpty(),
                            modifier = Modifier
                                .size(54.dp)
                                .background(
                                    color = if (searchDate.isNotEmpty()) colorResource(R.color.PinkSecondary)
                                    else Color.LightGray.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(14.dp)
                                )
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Filter",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
            when (moodState) {
                is MoodState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = colorResource(R.color.PinkPrimary))
                    }
                }
                is MoodState.Success -> {
                    val moods = (moodState as MoodState.Success).moods
                    if (moods.isEmpty()) {
                        EmptyHistoryView(searchDate)
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 100.dp, top = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            item {
                                Text(
                                    "Ditemukan ${moods.size} catatan",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                            items(moods) { mood ->
                                MoodHistoryCard(
                                    mood = mood,
                                    onClick = { navController.navigate("${Screen.DetailMood.route}/${mood.mood_id}") }
                                )
                            }
                        }
                    }
                }
                is MoodState.Error -> {
                    ErrorCard((moodState as MoodState.Error).message)
                }
                else -> {}
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Date(millis)
                        searchDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
                    }
                    showDatePicker = false
                }) { Text("Pilih", color = colorResource(R.color.PinkSecondary), fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Batal", color = Color.Gray) }
            },
            shape = RoundedCornerShape(28.dp)
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = colorResource(R.color.PinkSecondary),
                    todayDateBorderColor = colorResource(R.color.PinkPrimary),
                    todayContentColor = colorResource(R.color.PinkPrimary)
                )
            )
        }
    }
}

@Composable
fun MoodHistoryCard(mood: com.example.moodcare2.data.model.Mood, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(14.dp),
                color = colorResource(R.color.PinkLight).copy(alpha = 0.2f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(getMoodEmoji(mood.mood_level), style = MaterialTheme.typography.headlineSmall)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    getMoodLabel(mood.mood_level),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.PinkDark)
                )

                if (!mood.description.isNullOrBlank()) {
                    Text(
                        mood.description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        color = Color.Gray
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AccessTime, null, modifier = Modifier.size(12.dp), tint = colorResource(R.color.PinkPrimary))
                    Spacer(Modifier.width(4.dp))
                    Text(mood.tanggal, style = MaterialTheme.typography.labelSmall, color = colorResource(R.color.PinkPrimary).copy(alpha = 0.7f))
                }
            }

            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray.copy(alpha = 0.5f))
        }
    }
}

@Composable
fun EmptyHistoryView(searchDate: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(100.dp),
            shape = CircleShape,
            color = colorResource(R.color.PinkLight).copy(alpha = 0.1f)
        ) {
            Icon(Icons.Default.HistoryToggleOff, null, modifier = Modifier.padding(24.dp), tint = colorResource(R.color.PinkLight))
        }
        Spacer(Modifier.height(20.dp))
        Text("Tidak ada riwayat", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colorResource(R.color.PinkDark))
        Text(
            if (searchDate.isNotEmpty()) "Tidak ada mood tercatat" else "Mulai tuliskan perasaanmu hari ini",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}