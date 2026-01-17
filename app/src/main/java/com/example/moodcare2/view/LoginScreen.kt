package com.example.moodcare2.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moodcare2.MoodCareApplication
import androidx.compose.ui.res.colorResource
import com.example.moodcare2.R
import com.example.moodcare2.view.navigation.Screen
import com.example.moodcare2.viewmodel.AuthState
import com.example.moodcare2.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val application = context.applicationContext as MoodCareApplication
    val viewModel = remember {
        AuthViewModel(application.repository, application.sessionManager)
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        // Menambahkan topBar pembatas agar konsisten dengan layar lain
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White.copy(alpha = 0.6f),
                border = BorderStroke(
                    width = 1.dp,
                    color = colorResource(R.color.PinkSecondary).copy(alpha = 0.2f)
                )
            ) {
                // Spacer kecil hanya untuk memberikan tinggi pada surface pembatas
                Spacer(modifier = Modifier.height(12.dp).windowInsetsTopHeight(WindowInsets.statusBars))
            }
        },
        containerColor = colorResource(R.color.PinkBackground)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Logo dengan Surface yang memiliki Border konsisten
            Box(contentAlignment = Alignment.Center) {
                Surface(
                    modifier = Modifier.size(140.dp),
                    shape = androidx.compose.foundation.shape.CircleShape,
                    color = Color.White.copy(alpha = 0.5f),
                    border = BorderStroke(
                        width = 2.dp,
                        color = colorResource(R.color.PinkSecondary).copy(alpha = 0.2f)
                    )
                ) {}
                Image(
                    painter = painterResource(id = R.drawable.fox),
                    contentDescription = "Fox Logo",
                    modifier = Modifier.size(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Nama Aplikasi menggunakan warna Matcha agar konsisten dengan Home
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.ExtraBold,
                color = colorResource(R.color.Matcha)
            )

            Text(
                text = stringResource(R.string.login_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Card Form dengan Border halus
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = colorResource(R.color.PinkSecondary).copy(alpha = 0.1f)
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; emailError = false },
                        label = { Text(stringResource(R.string.email)) },
                        leadingIcon = { Icon(Icons.Default.Email, null) },
                        isError = emailError,
                        supportingText = { if (emailError) Text(stringResource(R.string.field_empty)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = loginFieldColors()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; passwordError = false },
                        label = { Text(stringResource(R.string.password)) },
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = passwordError,
                        supportingText = { if (passwordError) Text(stringResource(R.string.field_empty)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = loginFieldColors()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (authState is AuthState.Error) {
                Surface(
                    color = colorResource(R.color.PinkDark).copy(alpha = 0.6f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, Color(0xFFDC143C).copy(alpha = 0.2f))
                ) {
                    Text(
                        text = (authState as AuthState.Error).message,
                        color = Color(0xFFDC143C),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Tombol Login
            Button(
                onClick = {
                    emailError = email.isBlank()
                    passwordError = password.isBlank()
                    if (!emailError && !passwordError) viewModel.login(email, password)
                },
                enabled = authState !is AuthState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.PinkSecondary)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                if (authState is AuthState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text(stringResource(R.string.login), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text("Belum punya akun? ", color = Color.Gray)
                TextButton(onClick = {
                    viewModel.resetState()
                    navController.navigate(Screen.Register.route)
                }) {
                    Text(
                        "Daftar di sini",
                        color = colorResource(R.color.PinkSecondary),
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
@Composable
fun loginFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = colorResource(R.color.PinkPrimary),
    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
    focusedLabelColor = colorResource(R.color.PinkPrimary),
    cursorColor = colorResource(R.color.PinkPrimary),
    focusedLeadingIconColor = colorResource(R.color.PinkPrimary),
    focusedTrailingIconColor = colorResource(R.color.PinkPrimary)
)