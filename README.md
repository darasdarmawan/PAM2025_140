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
| <img width="1280" height="2856" alt="TampilanSplashScreen" src="https://github.com/user-attachments/assets/82db9bfd-f9e0-424f-8444-45b3a0951727" />| <img width="1280" height="2856" alt="HalamanLogin" src="https://github.com/user-attachments/assets/6d4aad04-2db8-4eff-9254-e7dfb5018cf4" />| <img width="1280" height="2856" alt="Register" src="https://github.com/user-attachments/assets/c23586e5-4a0d-49a8-82a6-d6248dab9001" />|

| Validasi Login    | Validasi Password          | Email Sudah Terdaftar                   |
| ----------------- | -------------------------- | --------------------------------------- |
| <img width="1280" height="2856" alt="ValidasiLogin" src="https://github.com/user-attachments/assets/993522be-9f05-47a8-bd97-9bde4e758a34" />| <img width="1280" height="2856" alt="ValidasiPasswordRegist" src="https://github.com/user-attachments/assets/1a4ac118-36b0-4ba7-8052-91c50c047f36" />| <img width="1280" height="2856" alt="ValidasiRegistEmailYgSudahTerdaftar" src="https://github.com/user-attachments/assets/7476267d-1473-4a48-bf70-28abb622496d" />|

### 🏠 Home & Mood

| Home            | Home Setelah + Mood & FotoProfile              | Tambah Mood           |
| --------------- | -------------------------------- | --------------------- |
| <img width="1280" height="2856" alt="HalamanHome" src="https://github.com/user-attachments/assets/ad2f5326-1652-4334-80da-3adc4fdcfde6" />| <img width="1280" height="2856" alt="HomeSetelahUpdateProfiledanMood" src="https://github.com/user-attachments/assets/784a1977-3f62-4f99-86c2-7f5ef3eb24df" />| <img width="1280" height="2856" alt="HalamanTambahMood" src="https://github.com/user-attachments/assets/26ed88f2-07b3-4d51-864b-482d30a1f548" />|

| Validasi               | Konfirmasi               | Notifikasi               |
| ---------------------- | ------------------------ | ------------------------ |
| <img width="1280" height="2856" alt="ValidasiTambahMood" src="https://github.com/user-attachments/assets/a2d3c80a-8438-40a1-b9f0-c0d488c3a8df" />| <img width="1280" height="2856" alt="KonfirmasiTambahMood" src="https://github.com/user-attachments/assets/1b413671-5d1c-4e03-8d3c-701d1817f9af" />| <img width="1280" height="2856" alt="NotifikasiTambahMood" src="https://github.com/user-attachments/assets/63af78df-5861-431d-a566-f978b7704d8a" />|

### 📜 Riwayat Mood

| Detail Mood           | Edit Mood           | Hasil Edit           |
| --------------------- | ------------------- | -------------------- |
| <img width="1280" height="2856" alt="HalamanDetailMood" src="https://github.com/user-attachments/assets/e22a9d27-b5ae-422e-b319-dd5b0e655f85" />| <img width="1280" height="2856" alt="HalamanEditMood" src="https://github.com/user-attachments/assets/9f5654db-2dd4-4c51-b7e2-c49dbaa856e2" />| <img width="1280" height="2856" alt="HasilSetelahEdit" src="https://github.com/user-attachments/assets/b67d5afd-acae-4650-8ee0-e42d277d8957" />|

| Konfirmasi Hapus        | Setelah Dihapus            | Filter Valid                             |
| ----------------------- | -------------------------- | ---------------------------------------- |
| <img width="1280" height="2856" alt="KonfirmasiHapusMood" src="https://github.com/user-attachments/assets/27c93748-fa52-4ae6-b780-a2e279278b33" />| <img width="1280" height="2856" alt="SetelahSatuMoodDihapus" src="https://github.com/user-attachments/assets/cf60d9dc-b79d-444f-8097-9facd68fb0f6" />| <img width="1280" height="2856" alt="FilterRiwayatBerdasarkanTanggalValid" src="https://github.com/user-attachments/assets/26918914-c0fc-4be0-8d72-841c66afacaa" />|

| Filter Tidak Valid                            | Konfirmasi Edit Mood  |   |
| --------------------------------------------- |  ---------------------- | - |
| <img width="1280" height="2856" alt="FilterRiwayatBerdasarkanTanggalTidakValid" src="https://github.com/user-attachments/assets/862ad279-6896-4267-9781-2b536ae9cf9a" />| <img width="1280" height="2856" alt="KonfirmasiPerubahan" src="https://github.com/user-attachments/assets/7b433b64-1cfd-4ff2-9814-f90ea8f3b2db" />|   |

### 📊 Grafik Mood

| Grafik + Filter                        | Notifikasi                   | Hasil Download          |
| -------------------------------------- | ---------------------------- | ----------------------- |
| <img width="1280" height="2856" alt="HalamanGrafikSetelahTerapkanFilter" src="https://github.com/user-attachments/assets/b7574a7f-3957-401e-bf8d-4f5521a59aae" />| <img width="1280" height="2856" alt="NotifikasiGrafikBerhasil" src="https://github.com/user-attachments/assets/2448c54d-7e64-4c86-a1f9-0646033f37e9" />|<img width="1280" height="2856" alt="HasilDownloadGrafik" src="https://github.com/user-attachments/assets/0b6719ec-2297-4541-b97b-25b08014051b" />|

### 👤 Profil & Logout

| Profil             | Edit Profil            | Edit Foto                     |
| ------------------ | ---------------------- | ----------------------------- |
| <img width="1280" height="2856" alt="HalamanProfile" src="https://github.com/user-attachments/assets/1445234a-8303-482f-bb7b-6604c48c4905" />| <img width="1280" height="2856" alt="HalamanEditProfile" src="https://github.com/user-attachments/assets/7110c713-bc33-4098-908d-a379a8ef8532" />| <img width="1280" height="2856" alt="HalamanEditProfilePicture" src="https://github.com/user-attachments/assets/90687a20-ab39-4c63-85c5-52c5db6ec99c" />|

| Konfirmasi Edit           | Profil Setelah Edit            | Konfirmasi Logout    |
| ------------------------- | ------------------------------ | -------------------- |
| <img width="1280" height="2856" alt="KonfirmasiEditProfile" src="https://github.com/user-attachments/assets/57c72578-0e39-4c7d-8091-a769125f51bd" />| <img width="1280" height="2856" alt="TampilanProfileSetelahEdit" src="https://github.com/user-attachments/assets/f72370be-f6c1-4395-a306-8221931c9f9d" />| <img width="1280" height="2856" alt="KonfirmasiLogout" src="https://github.com/user-attachments/assets/50fb9ff4-7e6c-4882-8502-3994db8a1564" />|

| Notifikasi Logout    |   |   |
| -------------------- | - | - |
| <img width="1280" height="2856" alt="NotifikasiLogout" src="https://github.com/user-attachments/assets/385b3a7a-cc0a-4179-adef-65e58c458433" />|   |   |


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
