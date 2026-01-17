# MoodCare – Aplikasi Pencatatan Mood Harian

## 📌 Deskripsi Proyek

MoodCare adalah aplikasi Android yang dirancang untuk membantu pengguna mencatat, memantau, dan merefleksikan kondisi mood harian mereka. Aplikasi ini dikembangkan sebagai **tugas akhir / proyek akademik** pada mata kuliah Pengembangan Aplikasi Mobile.

Aplikasi menerapkan arsitektur **client–server**, dengan Android sebagai frontend dan REST API berbasis PHP + MySQL sebagai backend.

---

## 🧱 Arsitektur Sistem

### Frontend (Android)

* Bahasa: **Kotlin**
* IDE: **Android Studio**
* UI: Material Design
* Komunikasi data: REST API (JSON)
* Autentikasi: JWT Token

### Backend

* Bahasa: **PHP**
* Database: **MySQL**
* Server lokal: XAMPP / Apache
* API Style: RESTful API

---

## ✨ Fitur Aplikasi

### 1️⃣ Autentikasi Pengguna

* Register
* Login
* Validasi input & error handling
* Logout dengan konfirmasi

### 2️⃣ Manajemen Profil

* Lihat profil pengguna
* Edit nama & foto profil
* Logout

### 3️⃣ Input Mood Harian

* Pilih level mood
* Catatan perasaan
* Refleksi harian:

  * Hal yang disyukuri
  * Hal yang membuat sedih
  * Hal yang perlu diperbaiki

### 4️⃣ Riwayat Mood

* Daftar mood
* Detail mood
* Edit mood
* Hapus mood (dengan konfirmasi)
* Filter berdasarkan tanggal

### 5️⃣ Grafik Mood

* Visualisasi grafik mood
* Filter rentang tanggal
* Download grafik (PNG)

---

## 👤 User Manual Singkat

1. Pengguna membuka aplikasi → Splash Screen
2. Login / Register akun
3. Masuk ke halaman Home
4. Tambah mood harian
5. Lihat & kelola riwayat mood
6. Lihat grafik perkembangan mood
7. Download grafik
8. Edit profil atau logout

---
## 🖼️ Lampiran Screenshot (Markdown Grid)

### 🔐 Autentikasi

| Splash Screen            | Login            | Register     |
| ------------------------ | ---------------- | ------------ |
| <img width="1280" height="2856" alt="TampilanSplashScreen" src="https://github.com/user-attachments/assets/82db9bfd-f9e0-424f-8444-45b3a0951727" />| <img width="1280" height="2856" alt="HalamanLogin" src="https://github.com/user-attachments/assets/6d4aad04-2db8-4eff-9254-e7dfb5018cf4" />| <img width="1280" height="2856" alt="UpdateUIRegist" src="https://github.com/user-attachments/assets/bb3a6657-2831-4a3d-99b9-1173fb598fb5" />|

| Validasi Login    | Validasi Password          | Email Sudah Terdaftar                   |
| ----------------- | -------------------------- | --------------------------------------- |
| <img width="1280" height="2856" alt="ValidasiLogin" src="https://github.com/user-attachments/assets/993522be-9f05-47a8-bd97-9bde4e758a34" />|<img width="1280" height="2856" alt="UpdateValidasiPasswordRegis" src="https://github.com/user-attachments/assets/7f1ed678-c730-414c-890c-db497dc8f51a" />|<img width="1280" height="2856" alt="UpdateEmailTerdaftar" src="https://github.com/user-attachments/assets/b442ed78-8e0e-4dd4-b578-0f6ea4283649" />|

### 🏠 Home & Mood

| Home            | Home Setelah + Mood & FotoProfile              | Tambah Mood           |
| --------------- | -------------------------------- | --------------------- |
|<img width="1280" height="2856" alt="UpdateUiHomebaru" src="https://github.com/user-attachments/assets/efb4e7a9-b02a-4f34-933d-36ed5b6f0dc7" />| <img width="1280" height="2856" alt="HomeAfterIsimooddanfoto" src="https://github.com/user-attachments/assets/a93efe3c-e9d5-49d7-a965-f217efd1df51" />|<img width="1280" height="2856" alt="UpdateUIAddMood" src="https://github.com/user-attachments/assets/1ec27d0e-7c38-4672-8870-5fa69443243c" />|

| Validasi               | Konfirmasi               | Notifikasi               |
| ---------------------- | ------------------------ | ------------------------ |
| <img width="1280" height="2856" alt="UpdateValidasiAddMood" src="https://github.com/user-attachments/assets/b0341793-31ac-4637-a975-6a94b2fe8724" />| <img width="1280" height="2856" alt="KonfirmasiTambahMood" src="https://github.com/user-attachments/assets/684afd95-d8a8-4613-a13e-309951a92ce8" />| <img width="1280" height="2856" alt="NotifikasiTambahMood" src="https://github.com/user-attachments/assets/15fbfc8e-4cbb-44fb-89d1-2c8f0cefdd9f" />|

### 📜 Riwayat Mood

| Detail Mood           | Edit Mood           | Hasil Edit           |
| --------------------- | ------------------- | -------------------- |
| <img width="1280" height="2856" alt="UpdateUIDetail" src="https://github.com/user-attachments/assets/3c6eeaa1-37f4-479d-9192-d1bdb99c3dd5" />| <img width="1280" height="2856" alt="UpdateUIEditMood" src="https://github.com/user-attachments/assets/b9748fba-7bd9-4137-95d5-f677d11ef573" />| <img width="1280" height="2856" alt="HasilEditBaru" src="https://github.com/user-attachments/assets/b77f627b-f846-4318-8cb9-10948f3533cc" />|

| Konfirmasi Hapus        | Setelah Dihapus            | Filter Valid                             |
| ----------------------- | -------------------------- | ---------------------------------------- |
| <img width="1280" height="2856" alt="KonfirmasiDeleteMoodBaru" src="https://github.com/user-attachments/assets/91eaa7ac-4a51-4d60-83cd-c0efb0bb6e65" />| <img width="1280" height="2856" alt="SetelehDeleteMood" src="https://github.com/user-attachments/assets/d8826a70-f8c5-42e8-911c-db7886268e3c" />| <img width="1280" height="2856" alt="ValidasiRiwayatTanggalBaru" src="https://github.com/user-attachments/assets/01aab1e1-4eb4-40a7-b9dd-c7e799d300fe" />|

| Filter Tidak Valid                            | Konfirmasi Edit Mood  |  Riwayat Screen |
| --------------------------------------------- |  ---------------------| --------------- |
| <img width="1280" height="2856" alt="ValidasiRiwayatTanggalKosong" src="https://github.com/user-attachments/assets/aec69547-54ef-4e01-b0b6-2c5db9a7caf6" />|<img width="1280" height="2856" alt="KonfirmasiEditMoodBaru" src="https://github.com/user-attachments/assets/f4d3b542-427b-4f79-a537-faebf49cf93e" />| <img width="1280" height="2856" alt="EditMoodBaru" src="https://github.com/user-attachments/assets/ce53f790-7aef-4c7b-9cbe-95c631e99458" />|

### 📊 Grafik Mood

| Grafik                      | Grafik Tanggal Valid         | Konfirmasi Berhasil Download |
| ----------------------------| ---------------------------- | ----------------------- |
| <img width="1280" height="2856" alt="HalamanGrafikBaru" src="https://github.com/user-attachments/assets/75495e22-2fc9-4343-8e2e-e32e516434c5" />| <img width="1280" height="2856" alt="TampilanGrafikIsiFilterValid" src="https://github.com/user-attachments/assets/bb4e7911-2c75-49fe-91e8-f675bbd43c99" />|<img width="1280" height="2856" alt="KonfirmasiPesanBerhasilDownload" src="https://github.com/user-attachments/assets/4137ad9f-9a31-443a-ad04-10181c51f685" />|

### 👤 Profil & Logout

| Profil             | Edit Profil            | Konfirmasi Edit               |
| ------------------ | ---------------------- | ----------------------------- |
| <img width="1280" height="2856" alt="UIProfileScreenBaru" src="https://github.com/user-attachments/assets/37556b12-8f2c-4016-b016-2e925913e531" />| <img width="1280" height="2856" alt="EditUIProfileScreenBaru" src="https://github.com/user-attachments/assets/318cd6b9-1db1-4151-879c-5aff5683ecb3" />| <img width="1280" height="2856" alt="KonfirmasiUbahProfileScreenBaru" src="https://github.com/user-attachments/assets/6a565c53-3cd0-4a24-bc60-327dd5300a72" />|

| Konfirmasi Logout          | Notifikasi Logout           | Hasil Download Grafik   |
| ------------------------- | ------------------------------ | -------------------- |
| <img width="1280" height="2856" alt="KonfirmasiLogoutBaru" src="https://github.com/user-attachments/assets/764f4815-eaaa-456f-a3ef-530b1f1eef3c" />| <img width="1280" height="2856" alt="NotifikasiLogoutBaru" src="https://github.com/user-attachments/assets/1a63bcbf-69fd-4256-b094-37a1d9ac0373" />| <img width="1280" height="2856" alt="NotifikasiLogoutBaru" src="https://github.com/user-attachments/assets/1fb340e2-4bb0-4076-99d4-9723ffdebcee" />|
 ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎ ‎   |


---

## 🛠️ Developer Setup (Android)

### 1️⃣ Clone Project

```bash
git clone <repository-url>
```

### 2️⃣ Konfigurasi BASE URL

📍 **Jika dijalankan di Android Emulator:**

```kotlin
private const val BASE_URL = "http://10.0.2.2/moodcare-api/"
```

📍 **Jika dijalankan di perangkat fisik (real device):**

```kotlin
private const val BASE_URL = "http://192.168.xxx.xxx/moodcare-api/"
```

> Ganti `xxx.xxx` dengan IP lokal laptop / server

### 3️⃣ Jalankan Backend

* Letakkan folder `moodcare-api` di `htdocs`
* Jalankan Apache & MySQL (XAMPP)
* Import database ke MySQL

### 4️⃣ Run Aplikasi

* Buka project di Android Studio
* Pilih emulator / device
* Klik **Run ▶️**

---

## 🗄️ Database

Database menggunakan **MySQL** dengan tabel utama:

* `users`
* `moods`
* `history_login`
* `graphs`

---
## 👤 Author

Nama : Dara Syauqi Darmawan
NIM : 20230140140

---

## 📜 Lisensi

Proyek **MoodCare** ini dikembangkan **khusus untuk keperluan tugas akhir / akademik**.
* Bebas digunakan sebagai referensi pembelajaran
* Hak cipta tetap dimiliki oleh pengembang

© 2026 – Dara Syauqi Darmawan
