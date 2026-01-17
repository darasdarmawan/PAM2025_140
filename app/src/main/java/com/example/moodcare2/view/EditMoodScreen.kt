package com.example.moodcare2.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.res.colorResource
import com.example.moodcare2.R
import com.example.moodcare2.viewmodel.EditMoodViewModel
import com.example.moodcare2.viewmodel.MoodState
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMoodScreen(navController: NavController, moodId: Int) {
    val context = LocalContext.current
    val application = context.applicationContext as MoodCareApplication
    val viewModel = remember { EditMoodViewModel(application.repository) }

    var selectedMood by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var halBersyukur by remember { mutableStateOf("") }
    var halSedih by remember { mutableStateOf("") }
    var halPerbaikan by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var isDataLoaded by remember { mutableStateOf(false) }

    val moodState by viewModel.moodState.collectAsState()

    LaunchedEffect(moodId) {
        viewModel.getMoodById(moodId)
    }

    LaunchedEffect(moodState) {
        when (val state = moodState) {
            is MoodState.SingleMoodSuccess -> {
                if (!isDataLoaded) {
                    val mood = state.mood
                    selectedMood = mood.mood_level.toString()
                    description = mood.description ?: ""
                    halBersyukur = mood.hal_bersyukur ?: ""
                    halSedih = mood.hal_sedih ?: ""
                    halPerbaikan = mood.hal_perbaikan ?: ""
                    tanggal = mood.tanggal
                    isDataLoaded = true
                }
            }
            is MoodState.OperationSuccess -> {
                navController.popBackStack()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(R.color.PinkDark).copy(alpha = 0.6f),
                border = BorderStroke(
                    width = 1.dp,
                    color = colorResource(R.color.PinkSecondary).copy(alpha = 0.2f)
                )
            )
            {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Perbarui Catatan",
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.titleLarge,
                            color = colorResource(R.color.Matcha)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, stringResource(R.string.back))
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        },
        containerColor = colorResource(R.color.PinkBackground)
    ) { paddingValues ->
        if (!isDataLoaded && moodState is MoodState.Loading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = colorResource(R.color.PinkPrimary))
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = colorResource(R.color.PinkLight).copy(alpha = 0.2f),
                            shape = CircleShape,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = colorResource(R.color.PinkSecondary),
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Tanggal Catatan", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            Text(
                                tanggal,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.PinkDark)
                            )
                        }
                    }
                }

                Text(
                    "Bagaimana perasaanmu sekarang?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.PinkDark)
                )

                MoodSelector(
                    selectedMood = selectedMood,
                    onMoodSelected = { selectedMood = it }
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

                        EditMoodTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = stringResource(R.string.description),
                            placeholder = "Apa yang terjadi hari ini?",
                            icon = "📝"
                        )

                        EditMoodTextField(
                            value = halBersyukur,
                            onValueChange = { halBersyukur = it },
                            label = stringResource(R.string.hal_bersyukur),
                            placeholder = "Aku bersyukur atas...",
                            icon = "🙏"
                        )

                        EditMoodTextField(
                            value = halSedih,
                            onValueChange = { halSedih = it },
                            label = stringResource(R.string.hal_sedih),
                            placeholder = "Hari ini terasa berat karena...",
                            icon = "😔"
                        )

                        EditMoodTextField(
                            value = halPerbaikan,
                            onValueChange = { halPerbaikan = it },
                            label = stringResource(R.string.hal_perbaikan),
                            placeholder = "Besok aku ingin lebih...",
                            icon = "💪"
                        )
                    }
                }

                if (moodState is MoodState.Error) {
                    ErrorCard((moodState as MoodState.Error).message)
                }

                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = selectedMood.isNotEmpty() && moodState !is MoodState.Loading,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.PinkSecondary)),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    if (moodState is MoodState.Loading && isDataLoaded) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Icon(Icons.Default.CheckCircle, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Simpan Perubahan", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(20.dp))
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            shape = RoundedCornerShape(28.dp),
            containerColor = Color.White,
            icon = { Icon(Icons.Default.Info, null, tint = colorResource(R.color.PinkPrimary)) },
            title = { Text("Konfirmasi") },
            text = { Text(stringResource(R.string.tanya2)) },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        viewModel.updateMood(moodId, selectedMood, description, halBersyukur, halSedih, halPerbaikan, tanggal)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.PinkSecondary))
                ) { Text(stringResource(R.string.yes)) }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text(stringResource(R.string.cancel), color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun EditMoodTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    icon: String
) {
    Column {
        Text(
            "$icon $label",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.PinkDark),
            modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            minLines = 2,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(R.color.PinkPrimary),
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.4f),
                cursorColor = colorResource(R.color.PinkPrimary)
            )
        )
    }
}