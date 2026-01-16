package com.example.moodcare2.view

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.moodcare2.MoodCareApplication
import com.example.moodcare2.R
import com.example.moodcare2.data.model.Mood
import androidx.compose.ui.res.colorResource
import com.example.moodcare2.viewmodel.GraphViewModel
import com.example.moodcare2.viewmodel.GraphState
import com.example.moodcare2.utils.getMoodEmoji
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodGraphScreen(navController: NavController) {
    val context = LocalContext.current
    val application = context.applicationContext as MoodCareApplication
    val viewModel = remember { GraphViewModel(application.repository) }
    val scope = rememberCoroutineScope()

    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var chartView by remember { mutableStateOf<LineChart?>(null) }
    var showDownloadDialog by remember { mutableStateOf(false) }
    var downloadMessage by remember { mutableStateOf("") }
    var hasStoragePermission by remember { mutableStateOf(checkStoragePermission(context)) }

    val graphState by viewModel.graphState.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasStoragePermission = isGranted
        if (!isGranted) {
            downloadMessage = "❌ Izin penyimpanan ditolak. Tidak dapat menyimpan grafik."
            showDownloadDialog = true
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Analisis Mood",
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
        containerColor = colorResource(R.color.PinkBackground)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Surface(
                color = colorResource(R.color.PinkLight).copy(alpha = 0.2f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Insights, null, tint = colorResource(R.color.PinkSecondary))
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "Pantau fluktuasi emosimu melalui grafik perkembangan.",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorResource(R.color.PinkDark)
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("📅 Tentukan Periode", fontWeight = FontWeight.Bold, color = colorResource(R.color.PinkDark))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        DateSelectBox(
                            modifier = Modifier.weight(1f),
                            label = "Mulai",
                            date = startDate,
                            onClick = { showStartDatePicker = true }
                        )
                        DateSelectBox(
                            modifier = Modifier.weight(1f),
                            label = "Sampai",
                            date = endDate,
                            onClick = { showEndDatePicker = true }
                        )
                    }

                    Button(
                        onClick = { if (startDate.isNotEmpty() && endDate.isNotEmpty()) viewModel.searchMoodByRange(startDate, endDate) },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = startDate.isNotEmpty() && endDate.isNotEmpty() && graphState !is GraphState.Loading,
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.PinkSecondary))
                    ) {
                        if (graphState is GraphState.Loading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                        } else {
                            Icon(Icons.Default.Timeline, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Tampilkan Grafik")
                        }
                    }
                }
            }

            when (graphState) {
                is GraphState.Success -> {
                    val moods = (graphState as GraphState.Success).moods
                    if (moods.isEmpty()) {
                        EmptyStateView()
                    } else {
                        Card(
                            modifier = Modifier.fillMaxWidth().height(350.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text("Tren Mood Kamu", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(16.dp))
                                AndroidView(
                                    factory = { ctx -> LineChart(ctx).apply { chartView = this; setupChart(this, moods) } },
                                    modifier = Modifier.fillMaxWidth().weight(1f)
                                )
                            }
                        }

                        MoodStatisticsCard(moods)

                        OutlinedButton(
                            onClick = {
                                if (needsStoragePermission() && !hasStoragePermission) {
                                    permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                } else {
                                    chartView?.let { chart ->
                                        scope.launch {
                                            val bitmap = chartToBitmap(chart)
                                            val result = saveBitmapToMediaStore(context, bitmap, startDate, endDate)
                                            downloadMessage = result
                                            showDownloadDialog = true
                                            if (result.contains("berhasil")) {
                                                saveDownloadHistory(application.sessionManager.getToken(), startDate, endDate, "MoodGraph_${startDate}_to_${endDate}.png")
                                            }
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, colorResource(R.color.PinkSecondary)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(R.color.PinkSecondary))
                        ) {
                            Icon(Icons.Default.FileDownload, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Simpan Grafik ke Galeri")
                        }
                    }
                }
                is GraphState.Idle -> IdleStateView()
                is GraphState.Error -> ErrorCard((graphState as GraphState.Error).message)
                else -> {}
            }
            Spacer(Modifier.height(20.dp))
        }
    }


    if (showStartDatePicker) DatePickerModal(onDismiss = { showStartDatePicker = false }, onDateSelected = { startDate = it; showStartDatePicker = false })
    if (showEndDatePicker) DatePickerModal(onDismiss = { showEndDatePicker = false }, onDateSelected = { endDate = it; showEndDatePicker = false })
    if (showDownloadDialog) {
        AlertDialog(
            onDismissRequest = { showDownloadDialog = false },
            shape = RoundedCornerShape(28.dp),
            confirmButton = { TextButton(onClick = { showDownloadDialog = false }) { Text("OK") } },
            title = { Text(if (downloadMessage.contains("berhasil")) "Berhasil" else "Gagal") },
            text = { Text(downloadMessage) }
        )
    }
}

@Composable
fun DateSelectBox(modifier: Modifier, label: String, date: String, onClick: () -> Unit) {
    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray, modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
        Surface(
            modifier = Modifier.fillMaxWidth().height(56.dp).clickable { onClick() },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
            color = colorResource(R.color.PinkBackground).copy(alpha = 0.2f)
        ) {
            Row(modifier = Modifier.padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarMonth, null, tint = colorResource(R.color.PinkSecondary), modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(date.ifEmpty { "Pilih..." }, style = MaterialTheme.typography.bodyMedium, color = if(date.isEmpty()) Color.LightGray else Color.Black)
            }
        }
    }
}

@Composable
fun IdleStateView() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(modifier = Modifier.size(100.dp), shape = CircleShape, color = Color.White) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.QueryStats, null, modifier = Modifier.size(50.dp), tint = colorResource(R.color.PinkLight))
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("Siap melihat tren mood kamu?", fontWeight = FontWeight.Bold, color = Color.Gray)
        Text("Silakan pilih rentang tanggal di atas", style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
    }
}

@Composable
fun EmptyStateView() {
    Card(modifier = Modifier.fillMaxWidth().height(200.dp), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🤷‍♂️", style = MaterialTheme.typography.displayMedium)
                Text("Tidak ada data", fontWeight = FontWeight.Bold)
                Text("Coba pilih tanggal lain", color = Color.Gray)
            }
        }
    }
}

@Composable
fun MoodStatisticsCard(moods: List<Mood>) {
    val moodCounts = moods.groupingBy { it.mood_level }.eachCount()
    val totalMoods = moods.size
    val avgMoodValue = moods.map { getMoodValue(it.mood_level) }.average()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Statistik Ringkas", fontWeight = FontWeight.Bold, color = colorResource(R.color.PinkDark))
            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatItemV2("Total Catatan", totalMoods.toString(), Icons.Default.Description)
                StatItemV2("Rata-rata", String.format("%.1f", avgMoodValue), Icons.Default.Star)
            }

            Spacer(Modifier.height(20.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.2f))
            Spacer(Modifier.height(20.dp))

            moodCounts.forEach { (mood, count) ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(modifier = Modifier.size(32.dp), shape = CircleShape, color = colorResource(R.color.PinkBackground)) {
                            Box(contentAlignment = Alignment.Center) { Text(getMoodEmoji(mood)) }
                        }
                        Spacer(Modifier.width(12.dp))
                        Text(mood, style = MaterialTheme.typography.bodyMedium)
                    }
                    Text("$count entri", fontWeight = FontWeight.Bold, color = colorResource(R.color.PinkSecondary))
                }
            }
        }
    }
}

@Composable
fun StatItemV2(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, modifier = Modifier.size(36.dp), tint = colorResource(R.color.PinkLight).copy(alpha = 0.5f))
        Spacer(Modifier.width(8.dp))
        Column {
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = colorResource(R.color.PinkSecondary))
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Calendar.getInstance().timeInMillis
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Date(millis)
                        val formatted = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
                        onDateSelected(formatted)
                    }
                },
                colors = ButtonDefaults.textButtonColors(contentColor = colorResource(R.color.PinkSecondary))
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = colorResource(R.color.PinkPrimary))
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                selectedDayContainerColor = colorResource(R.color.PinkSecondary),
                todayContentColor = colorResource(R.color.PinkPrimary),
                todayDateBorderColor = colorResource(R.color.PinkPrimary)
            )
        )
    }
}

fun setupChart(chart: LineChart, moods: List<Mood>) {
    val sortedMoods = moods.sortedBy { it.tanggal }

    val entries = sortedMoods.mapIndexed { index, mood ->
        Entry(index.toFloat(), getMoodValue(mood.mood_level).toFloat())
    }

    val dataSet = LineDataSet(entries, "Mood Level").apply {
        color = android.graphics.Color.rgb(255, 20, 147)
        setCircleColor(android.graphics.Color.rgb(255, 105, 180))
        lineWidth = 3f
        circleRadius = 6f
        setDrawCircleHole(false)
        valueTextSize = 10f
        setDrawFilled(true)
        fillColor = android.graphics.Color.rgb(255, 182, 193)
        fillAlpha = 80
        mode = LineDataSet.Mode.CUBIC_BEZIER
    }

    chart.apply {
        data = LineData(dataSet)
        description.isEnabled = false
        legend.isEnabled = true

        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            granularity = 1f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val index = value.toInt()
                    return if (index in sortedMoods.indices) {
                        sortedMoods[index].tanggal.substring(5)
                    } else ""
                }
            }
        }

        axisLeft.apply {
            axisMinimum = 0f
            axisMaximum = 5f
            granularity = 1f
            setDrawGridLines(true)
        }

        axisRight.isEnabled = false

        setTouchEnabled(true)
        isDragEnabled = true
        setScaleEnabled(true)
        setPinchZoom(true)

        animateX(1000)
        invalidate()
    }
}

fun getMoodValue(moodLevel: String): Int {
    return when (moodLevel.lowercase()) {
        "sangat buruk" -> 1
        "buruk" -> 2
        "netral" -> 3
        "baik" -> 4
        "sangat baik" -> 5
        else -> 3
    }
}

fun chartToBitmap(chart: LineChart): Bitmap {
    val bitmap = Bitmap.createBitmap(
        chart.width,
        chart.height,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    chart.draw(canvas)
    return bitmap
}

fun needsStoragePermission(): Boolean {
    return Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
}

fun checkStoragePermission(context: Context): Boolean {
    return if (needsStoragePermission()) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        true
    }
}

fun saveBitmapToMediaStore(
    context: Context,
    bitmap: Bitmap,
    startDate: String,
    endDate: String
): String {
    return try {
        val fileName = "MoodGraph_${startDate}_to_${endDate}.png"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MoodCare")
            }
        }

        val uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )

        uri?.let {
            context.contentResolver.openOutputStream(it)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            "✅ ${context.getString(R.string.graph_saved)}\n\nLokasi: Pictures/MoodCare/$fileName"
        } ?: "❌ Gagal membuat file"

    } catch (e: Exception) {
        "❌ Gagal menyimpan grafik:\n${e.message}"
    }
}

suspend fun saveDownloadHistory(
    token: String?,
    startDate: String,
    endDate: String,
    fileName: String
) = withContext(Dispatchers.IO) {
    try {
        val json = JSONObject().apply {
            put("start_date", startDate)
            put("end_date", endDate)
            put("file_name", fileName)
        }

        val requestBody = json.toString()
            .toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("http://10.0.2.2/moodcare_api/api/graphs/save.php")
            .addHeader("Authorization", "Bearer $token")
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        val response = client.newCall(request).execute()

        println("✅ Riwayat download berhasil disimpan")
    } catch (e: Exception) {
        println("❌ Gagal simpan riwayat: ${e.message}")
    }
}