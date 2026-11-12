# Laporan Fitur Pencarian Profil

## Implementasi
StudentApiService mendefinisikan GET users/search dengan query parameter q. StudentRemoteDataSource menjalankan request secara asynchronous dan meneruskan Result berisi UserDto. DefaultStudentRepository memetakan DTO menjadi Student. HomeViewModel mengelola searchQuery, loading, hasil, dan error. HomeScreen hanya menerima state serta callback tanpa mengakses API atau Retrofit.

Mengetik hanya mengubah searchQuery. Tombol Cari mengirim query yang sudah di-trim; query kosong atau spasi memanggil daftar awal GET users?limit=10. Tombol Coba Lagi mengulang request terakhir yang dikirim. Hasil kosong ditampilkan sebagai "Profil tidak ditemukan". Klik hasil meneruskan id ke rute Detail yang sudah ada.

## Verifikasi
Unit test ditambahkan untuk pemisahan edit dan submit, hasil sukses, query kosong, loading/error/retry, serta respons lama yang datang terlambat. Build dan unit test belum dijalankan dalam lingkungan pengerjaan: clone terminal gagal karena jaringan dan Android SDK/ADB tidak tersedia pada lokasi standar.

## Bukti screenshot — belum tersedia
Screenshot aktual belum diambil. Bagian ini belum memenuhi syarat bukti tugas.

Setelah menjalankan aplikasi di Android Studio:
1. Masukkan John dan tekan Cari.
2. Tunggu hasil muncul; ambil screenshot yang memperlihatkan query dan daftar hasil.
3. Simpan sebagai docs/screenshots/search-john.png, lalu ganti bagian ini dengan gambar dan keterangan hasil aktual.
4. Klik satu hasil untuk memeriksa Detail berdasarkan id.
5. Kosongkan query dan tekan Cari untuk memeriksa daftar awal.
6. Matikan jaringan dan lakukan pencarian untuk memeriksa error serta Coba Lagi.
