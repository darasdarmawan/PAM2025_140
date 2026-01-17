package com.example.moodcare2.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.moodcare2.MoodCareApplication
import com.example.moodcare2.R
import com.example.moodcare2.viewmodel.ProfileViewModel
import androidx.compose.ui.res.colorResource
import com.example.moodcare2.viewmodel.ProfileState
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val application = context.applicationContext as MoodCareApplication

    val viewModel = remember(application) {
        ProfileViewModel(application.repository, application.sessionManager)
    }

    val sessionManager = application.sessionManager

    var nama by remember { mutableStateOf(sessionManager.getNama() ?: "") }
    var fotoProfil by remember { mutableStateOf(sessionManager.getFotoProfile() ?: "") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageFile by remember { mutableStateOf<File?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var namaError by remember { mutableStateOf(false) }

    val profileState by viewModel.profileState.collectAsState()

    DisposableEffect(Unit) {
        onDispose { selectedImageFile?.delete() }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            try {
                context.contentResolver.openInputStream(it)?.use { inputStream ->
                    val file = File(context.cacheDir, "profile_temp_${System.currentTimeMillis()}.jpg")
                    file.outputStream().use { output -> inputStream.copyTo(output) }
                    selectedImageFile?.delete()
                    selectedImageFile = file
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    LaunchedEffect(profileState) {
        if (profileState is ProfileState.UpdateSuccess) {
            selectedImageFile?.delete()
            navController.previousBackStackEntry?.savedStateHandle?.set("profile_updated", true)
            navController.popBackStack()
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
                            "Ubah Profil",
                            fontWeight = FontWeight.ExtraBold,
                            style = MaterialTheme.typography.titleLarge,
                            color = colorResource(R.color.Matcha)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            selectedImageFile?.delete()
                            navController.popBackStack()
                        }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                            )
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
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))

            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(
                    modifier = Modifier.size(130.dp),
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 4.dp,
                    border = BorderStroke(4.dp, Color.White)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        when {
                            selectedImageUri != null -> {
                                Image(
                                    painter = rememberAsyncImagePainter(selectedImageUri),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            fotoProfil.isNotBlank() -> {
                                Image(
                                    painter = rememberAsyncImagePainter(fotoProfil),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            else -> {
                                Icon(
                                    Icons.Default.Person,
                                    null,
                                    modifier = Modifier.size(60.dp),
                                    tint = colorResource(R.color.PinkLight)
                                )
                            }
                        }
                    }
                }

                IconButton(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(colorResource(R.color.PinkSecondary))
                ) {
                    Icon(
                        Icons.Default.PhotoCamera,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "🌸 Informasi Publik",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.PinkDark)
                    )

                    OutlinedTextField(
                        value = nama,
                        onValueChange = { nama = it; namaError = false },
                        label = { Text("Nama Lengkap") },
                        placeholder = { Text("Masukkan nama baru") },
                        leadingIcon = { Icon(Icons.Default.Badge, null) },
                        isError = namaError,
                        supportingText = { if (namaError) Text(stringResource(R.string.field_empty)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors = editFieldColors()
                    )

                    OutlinedTextField(
                        value = sessionManager.getEmail() ?: "",
                        onValueChange = { },
                        label = { Text("Alamat Email") },
                        leadingIcon = { Icon(Icons.Default.Email, null) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false,
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledContainerColor = colorResource(R.color.PinkBackground).copy(alpha = 0.5f),
                            disabledBorderColor = Color.LightGray.copy(alpha = 0.3f)
                        )
                    )
                }
            }

            if (profileState is ProfileState.Error) {
                Surface(
                    color = Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        (profileState as ProfileState.Error).message,
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFFDC143C),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Button(
                onClick = {
                    namaError = nama.isBlank()
                    if (!namaError) showConfirmDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = profileState !is ProfileState.Loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.PinkSecondary)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                if (profileState is ProfileState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Icon(Icons.Default.Save, null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(10.dp))
                    Text("Simpan Perubahan", fontWeight = FontWeight.Bold)
                }
            }

            Text(
                "Pastikan informasi yang Anda masukkan sudah benar.",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 20.dp)
            )
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            shape = RoundedCornerShape(28.dp),
            containerColor = Color.White,
            title = { Text("Simpan Profil?", fontWeight = FontWeight.Bold) },
            text = { Text("Informasi profil Anda akan diperbarui sesuai dengan perubahan yang Anda buat.") },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        viewModel.updateProfile(nama, selectedImageFile)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.PinkSecondary))
                ) { Text("Ya, Simpan") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Batal", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun editFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = colorResource(R.color.PinkPrimary),
    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
    focusedLabelColor = colorResource(R.color.PinkPrimary),
    focusedLeadingIconColor = colorResource(R.color.PinkPrimary)
)