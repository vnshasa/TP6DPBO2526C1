# 🐦 Flappy Bird - Java Swing Edition
**Tugas Praktikum 6 - Desain dan Pemrograman Berorientasi Objek (DPBO)**

Proyek ini adalah hasil pengembangan ulang dari permainan kasual populer, Flappy Bird, menggunakan pustaka antarmuka grafis **Java Swing**. Pengembangan game ini berfokus pada implementasi prinsip-prinsip Pemrograman Berorientasi Objek (PBO), pemisahan antara logika permainan dan rendering visual, serta manajemen aset (audio dan gambar).

---

### Janji
Saya **Vanisha Septiani Auliaputri** dengan **NIM 2510735** mengerjakan Tugas Praktikum 6 dalam mata kuliah Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

---

### Fitur
Berbeda dengan spesifikasi dasar, game ini telah dimodifikasi dengan berbagai fitur tambahan:
* **Persistent Best Score**: Menyimpan rekor skor tertinggi secara lokal menggunakan `Preferences API`, sehingga rekor tidak hilang meskipun komputer dimatikan.
* **Sistem Navigasi Layar**: Menggunakan `CardLayout` untuk perpindahan mulus antara halaman *Main Menu* dan area *Gameplay*.
* **Pause Overlay**: Pemain dapat menjeda (Pause) permainan kapan saja. Status burung dan pipa akan dibekukan, dan menu navigasi tambahan akan muncul.
* **Audio Interaktif (BGM & SFX)**: Terintegrasi dengan efek suara berlapis untuk musik latar, suara lompatan, perolehan skor, dan kondisi tertabrak.
* **Fisika Sederhana**: Menerapkan gaya gravitasi buatan untuk mensimulasikan burung jatuh secara natural.

---

### Kontrol
| Aksi / Tombol | Kondisi | Deskripsi |
| :--- | :--- | :--- |
| **`Spasi (Space)`** | Saat Bermain | Mengepakkan sayap / membuat burung melompat ke atas. |
| **`R`** | Game Over | Melakukan *restart* secara instan ke awal permainan. |
| **Tombol `⏸ Pause`** | Saat Bermain | Menghentikan waktu permainan sementara. |
| **Tombol `▶ Lanjutkan`** | Layar Pause | Melanjutkan kembali permainan yang sedang dijeda. |
| **Tombol `🏠 Main Menu`**| Pause / Game Over | Keluar dari sesi bermain dan kembali ke beranda utama. |

---

### Deskripsi Kelas

Program ini dibagi menjadi beberapa kelas terpisah agar kode lebih rapi dan mudah di- *maintenance*:

1. **`App.java` (Main Entry)**
   Jantung dari program yang memanggil `JFrame`. Di sini, layout manager (`CardLayout`) diatur untuk menangani perpindahan tampilan antara *Main Menu* dan panel permainan (*View*).

2. **`Logic.java` (Game Engine)**
  Kelas ini mengurus seluruh "matematika" di balik permainan. Mulai dari mengaktifkan timer (berjalan di ~60 Frame Per Second), mendeteksi tabrakan (*bounding box collision*), hingga mencatat penambahan skor dan menyimpannya ke memori sistem.

3. **`View.java` (Renderer & UI)**
   Bertugas murni untuk urusan visual. Menggunakan metode `paintComponent(Graphics g)`, kelas ini menggambar aset (burung, pipa, background) sesuai dengan titik koordinat yang dikirim oleh kelas `Logic`. Kelas ini juga menampung tombol-tombol interaktif seperti *Pause* dan *Resume*.

4. **`MainMenu.java` (Beranda)**
  Panel sederhana berbasis `GridBagLayout` yang menyambut pemain saat pertama kali aplikasi dibuka. Menyediakan tombol pintas untuk langsung bermain atau keluar dari aplikasi.

5. **`SoundPlayer.java` (Audio Controller)**
   Kelas utilitas (*utility class*) untuk mengelola aliran suara. Memiliki penanganan khusus untuk BGM (diulang terus-menerus/ *looping*) dan SFX (diputar sekali lalu ditutup agar tidak membebani memori).

6. **`Player.java` & `Pipe.java` (Model)**
  Kelas cetak biru (POJO) untuk entitas yang ada di dalam game. Menyimpan variabel seperti kordinat (`X`, `Y`), ukuran (`width`, `height`), velositas jatuhnya objek, serta aset gambarnya.

---

### Alur Permainan

1. **Start-up**: Pengguna menjalankan aplikasi. `App.java` memuat jendela `JFrame` dan menampilkan `MainMenu`. Musik latar khusus menu diputar.
2. **Inisialisasi Game**: Saat tombol "Start Game" diklik, sistem menghapus panel menu, lalu membuat objek `Logic` dan `View`. Musik beralih menjadi tema *gameplay*.
3. **Loop Permainan**: 
   * `Timer` dalam `Logic` memicu pembaharuan posisi objek sebanyak 60 kali per detik.
   * `View` terus-menerus menggambar ulang (`repaint()`) layar agar pergerakan terlihat patah-patah/mulus.
   * Rintangan pipa baru (*upper & lower*) dibuat setiap 1.5 detik sekali di luar layar sisi kanan.
4. **Scoring & Collision**: Jika koordinat burung melewati koordinat pipa, skor bertambah +1. Jika burung menyentuh area pipa atau jatuh ke batas bawah, fungsi pengecekan tabrakan mengirimkan nilai `true`.
5. **Kekalahan**: Status permainan diubah menjadi *Game Over*. Semua timer pergerakan dimatikan, layar membeku, dan tombol opsi kembali ke menu utama akan dimunculkan oleh `View`.

---

### Dokumentasi Visual
*Silakan tonton video demo jalannya program pada direktori repositori ini:*

https://github.com/user-attachments/assets/8f3fbebd-2d46-4b8d-86ba-4ff82d20b739


