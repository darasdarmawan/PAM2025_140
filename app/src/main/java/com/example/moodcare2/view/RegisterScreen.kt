package com.example.moodcare2.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import android.util.Patterns
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
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val application = context.applicationContext as MoodCareApplication
    val viewModel = remember {
        AuthViewModel(application.repository, application.sessionManager)
    }

    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    var namaError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }
    var passwordMismatch by remember { mutableStateOf(false) }

    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Register.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.register), fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = colorResource(R.color.PinkSecondary),
                    navigationIconContentColor = colorResource(R.color.PinkSecondary)
                )
            )
        },
        containerColor = colorResource(R.color.PinkBackground)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Box(contentAlignment = Alignment.Center) {
                Surface(
                    modifier = Modifier.size(120.dp),
                    shape = androidx.compose.foundation.shape.CircleShape,
                    color = colorResource(R.color.PinkPrimary).copy(alpha = 0.1f)
                ) {}
                Image(
                    painter = painterResource(id = R.drawable.fox),
                    contentDescription = "Fox Logo",
                    modifier = Modifier.size(90.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = colorResource(R.color.PinkSecondary)
            )

            Text(
                text = stringResource(R.string.register_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    OutlinedTextField(
                        value = nama,
                        onValueChange = { nama = it; namaError = false },
                        label = { Text(stringResource(R.string.full_name)) },
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        isError = namaError,
                        supportingText = { if (namaError) Text(stringResource(R.string.field_empty)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = fieldColors()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; emailError = false },
                        label = { Text(stringResource(R.string.email)) },
                        leadingIcon = { Icon(Icons.Default.Email, null) },
                        isError = emailError,
                        supportingText = {
                            if (emailError) {
                                Text(if (email.isBlank()) stringResource(R.string.field_empty) else stringResource(R.string.invalid_email))
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = fieldColors()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = false
                            passwordMismatch = false
                        },
                        label = { Text(stringResource(R.string.password)) },
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = password.isNotEmpty() && password.length < 6 || passwordMismatch,
                        supportingText = {
                            if (password.isNotEmpty() && password.length < 6) {
                                Text("Password harus minimal 6 karakter")
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = fieldColors()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            confirmPasswordError = false
                            passwordMismatch = false
                        },
                        label = { Text("Konfirmasi Kata Sandi") },
                        leadingIcon = { Icon(Icons.Default.Lock, null) },
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = confirmPasswordError || passwordMismatch,
                        supportingText = { if (passwordMismatch) Text("Password tidak cocok") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = fieldColors()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (authState is AuthState.Error) {
                Surface(
                    color = Color(0xFFFFEBEE),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Text(
                        text = (authState as AuthState.Error).message,
                        color = Color(0xFFC62828),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Button(
                onClick = {
                    namaError = nama.isBlank()
                    emailError = email.isBlank() || !isValidEmail(email)
                    passwordError = password.length < 6
                    confirmPasswordError = confirmPassword.isBlank()
                    passwordMismatch = password != confirmPassword

                    if (!namaError && !emailError && !passwordError &&
                        !confirmPasswordError && !passwordMismatch) {
                        viewModel.register(nama, email, password)
                    }
                },
                enabled = authState !is AuthState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.PinkSecondary),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                if (authState is AuthState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text(stringResource(R.string.register), style = MaterialTheme.typography.titleMedium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Text("Sudah punya akun? ", color = Color.Gray)
                TextButton(onClick = {
                    viewModel.resetState()
                    navController.popBackStack()
                }) {
                    Text(
                        "Masuk di sini",
                        color = colorResource(R.color.PinkSecondary),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
@Composable
fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = colorResource(R.color.PinkPrimary),
    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
    focusedLabelColor = colorResource(R.color.PinkPrimary),
    cursorColor = colorResource(R.color.PinkPrimary),
    focusedLeadingIconColor = colorResource(R.color.PinkPrimary),
    focusedTrailingIconColor = colorResource(R.color.PinkPrimary)
)

fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}