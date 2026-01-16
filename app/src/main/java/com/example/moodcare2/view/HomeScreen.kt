package com.example.moodcare2.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.moodcare2.MoodCareApplication
import com.example.moodcare2.R
import com.example.moodcare2.view.navigation.Screen
import com.example.moodcare2.viewmodel.CreateMoodViewModel
import com.example.moodcare2.viewmodel.MoodState
import com.example.moodcare2.utils.getMoodLabel
import com.example.moodcare2.utils.getMoodEmoji
import androidx.compose.ui.res.colorResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val application = context.applicationContext as MoodCareApplication
    val sessionManager = application.sessionManager
    val viewModel = remember { CreateMoodViewModel(application.repository) }
    val moodState by viewModel.moodState.collectAsState()
    var refreshTrigger by remember { mutableStateOf(0) }
    val fotoProfil = remember(refreshTrigger) { sessionManager.getFotoProfile() }

    LaunchedEffect(navController.currentBackStackEntry) {
        val updated = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.get<Boolean>("profile_updated")

        if (updated == true) {
            refreshTrigger++
            navController.currentBackStackEntry?.savedStateHandle?.set("profile_updated", false)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getAllMoods()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.fox),
                            contentDescription = "Logo",
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = colorResource(R.color.PinkSecondary)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                        key(fotoProfil, refreshTrigger) {
                            Surface(
                                modifier = Modifier.size(38.dp),
                                shape = CircleShape,
                                border = BorderStroke(2.dp, colorResource(R.color.PinkPrimary).copy(alpha = 0.5f)),
                                color = Color.White
                            ) {
                                if (!fotoProfil.isNullOrBlank()) {
                                    Image(
                                        painter = rememberAsyncImagePainter(
                                            model = ImageRequest.Builder(context)
                                                .data(fotoProfil)
                                                .memoryCachePolicy(CachePolicy.DISABLED)
                                                .diskCachePolicy(CachePolicy.DISABLED)
                                                .crossfade(true)
                                                .build()
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier.clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = null,
                                        tint = colorResource(R.color.PinkPrimary),
                                        modifier = Modifier.padding(6.dp)
                                    )
                                }
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 10.dp,
                modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                val navItems = listOf(
                    Triple(Icons.Default.Add, "Tambah", Screen.AddMood.route),
                    Triple(Icons.Default.History, stringResource(R.string.history), Screen.MoodHistory.route),
                    Triple(Icons.Default.Home, stringResource(R.string.home), ""),
                    Triple(Icons.Default.BarChart, stringResource(R.string.graph), Screen.MoodGraph.route)
                )

                navItems.forEach { (icon, label, route) ->
                    val isSelected = label == stringResource(R.string.home)
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal) },
                        selected = isSelected,
                        onClick = { if (!isSelected) navController.navigate(route) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colorResource(R.color.PinkPrimary),
                            selectedTextColor = colorResource(R.color.PinkPrimary),
                            unselectedIconColor = Color.Gray.copy(alpha = 0.6f),
                            indicatorColor = colorResource(R.color.PinkLight).copy(alpha = 0.2f)
                        )
                    )
                }
            }
        },
        containerColor = colorResource(R.color.PinkBackground)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Halo, ${sessionManager.getNama()}! ✨",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.PinkDark)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        colorResource(R.color.PinkPrimary),
                                        colorResource(R.color.PinkSecondary)
                                    )
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f),
                                modifier = Modifier.size(64.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.fox),
                                    contentDescription = null,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                            Column {
                                Text(
                                    "Bagaimana perasaanmu?",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    "Yuk, catat mood kamu hari ini untuk melacak kesehatan mentalmu.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Mood Terbaru",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.PinkDark)
                    )
                    TextButton(onClick = { navController.navigate(Screen.MoodHistory.route) }) {
                        Text("Lihat Semua", color = colorResource(R.color.PinkPrimary), style = MaterialTheme.typography.labelLarge)
                    }
                }
            }

            when (moodState) {
                is MoodState.Loading -> {
                    item {
                        Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = colorResource(R.color.PinkPrimary), strokeWidth = 3.dp)
                        }
                    }
                }
                is MoodState.Success -> {
                    val moods = (moodState as MoodState.Success).moods.take(3)
                    if (moods.isEmpty()) {
                        item { EmptyStateCard() }
                    } else {
                        items(moods) { mood ->
                            MoodItemCard(
                                mood = mood,
                                onClick = { navController.navigate("${Screen.DetailMood.route}/${mood.mood_id}") }
                            )
                        }
                    }
                }
                is MoodState.Error -> {
                    item { ErrorCard((moodState as MoodState.Error).message) }
                }
                else -> {}
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun MoodItemCard(mood: com.example.moodcare2.data.model.Mood, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                color = colorResource(R.color.PinkLight).copy(alpha = 0.2f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        getMoodEmoji(mood.mood_level),
                        style = MaterialTheme.typography.headlineMedium
                    )
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
                Text(
                    mood.tanggal,
                    style = MaterialTheme.typography.labelSmall,
                    color = colorResource(R.color.PinkPrimary).copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.LightGray
            )
        }
    }
}

@Composable
fun EmptyStateCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.5f)),
        border = BorderStroke(1.dp, colorResource(R.color.PinkLight))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.CloudQueue,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = colorResource(R.color.PinkLight)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Belum ada catatan hari ini",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray
            )
        }
    }
}
@Composable
fun ErrorCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFE4E1)
        )
    ) {
        Text(
            message,
            modifier = Modifier.padding(16.dp),
            color = Color(0xFFDC143C)
        )
    }
}