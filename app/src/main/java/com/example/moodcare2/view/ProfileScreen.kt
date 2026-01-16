package com.example.moodcare2.view

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.moodcare2.MoodCareApplication
import androidx.compose.ui.res.colorResource
import com.example.moodcare2.R
import com.example.moodcare2.view.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val application = context.applicationContext as MoodCareApplication
    val sessionManager = application.sessionManager

    var showLogoutDialog by remember { mutableStateOf(false) }
    var refreshTrigger by remember { mutableStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val nama = remember(refreshTrigger) { sessionManager.getNama() ?: "User" }
    val email = remember(refreshTrigger) { sessionManager.getEmail() ?: "" }
    val fotoProfil = remember(refreshTrigger) { sessionManager.getFotoProfile() }
    val userId = remember(refreshTrigger) { sessionManager.getUserId() }

    LaunchedEffect(navController.currentBackStackEntry) {
        val updated = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.get<Boolean>("profile_updated")

        if (updated == true) {
            refreshTrigger++
            navController.currentBackStackEntry?.savedStateHandle?.set("profile_updated", false)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "${stringResource(R.string.profile)} Saya",
                        fontWeight = FontWeight.Bold,
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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = colorResource(R.color.PinkBackground)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Surface(
                            modifier = Modifier.size(110.dp),
                            shape = CircleShape,
                            color = colorResource(R.color.PinkLight).copy(alpha = 0.4f),
                            border = BorderStroke(3.dp, Color.White)
                        ) {
                            key(fotoProfil, refreshTrigger) {
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
                                        contentDescription = "Profile Picture",
                                        modifier = Modifier.clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = null,
                                        modifier = Modifier.padding(25.dp),
                                        tint = colorResource(R.color.PinkPrimary)
                                    )
                                }
                            }
                        }

                        Surface(
                            color = colorResource(R.color.PinkSecondary),
                            shape = CircleShape,
                            modifier = Modifier.size(28.dp),
                            border = BorderStroke(2.dp, Color.White)
                        ) {
                            Icon(Icons.Default.Check, null, modifier = Modifier.padding(6.dp), tint = Color.White)
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        nama,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = colorResource(R.color.PinkDark)
                    )

                    Text(
                        email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray.copy(alpha = 0.8f)
                    )

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = { navController.navigate(Screen.EditProfile.route) },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.PinkSecondary)
                        )
                    ) {
                        Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Edit Profil", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.2f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Informasi Akun",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.PinkDark)
                    )

                    ProfileInfoItem(Icons.Default.Person, "Nama", nama)
                    Divider(color = Color.LightGray.copy(alpha = 0.3f))
                    ProfileInfoItem(Icons.Default.Email, "Email", email)
                    Divider(color = Color.LightGray.copy(alpha = 0.3f))
                    ProfileInfoItem(Icons.Default.Badge, "User ID", userId.toString())
                }
            }

            TextButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFDC143C))
            ) {
                Icon(Icons.Default.Logout, null)
                Spacer(Modifier.width(8.dp))
                Text("Keluar dari Akun", fontWeight = FontWeight.Bold)
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "${stringResource(R.string.app_name)} v1.0.0",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray.copy(alpha = 0.6f)
                )
                Text(
                    "Made with ❤️ for your mental health",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray.copy(alpha = 0.5f)
                )
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White,
            icon = {
                Surface(color = Color(0xFFFFEBEE), shape = CircleShape) {
                    Icon(Icons.Default.PowerSettingsNew, null, modifier = Modifier.padding(12.dp), tint = Color(0xFFDC143C))
                }
            },
            title = { Text("Konfirmasi Keluar", fontWeight = FontWeight.Bold) },
            text = { Text("Apakah Anda yakin ingin keluar dari aplikasi MoodCare?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("👋 Berhasil keluar")
                            delay(800)
                            sessionManager.clearSession()
                            navController.navigate(Screen.Login.route) { popUpTo(0) { inclusive = true } }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC143C)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Keluar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Batal", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun ProfileInfoItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Surface(
                color = colorResource(R.color.PinkPrimary).copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    icon,
                    null,
                    tint = colorResource(R.color.PinkPrimary),
                    modifier = Modifier.padding(6.dp).size(20.dp)
                )
            }
            Text(label, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        }
        Text(
            value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(R.color.PinkDark)
        )
    }
}