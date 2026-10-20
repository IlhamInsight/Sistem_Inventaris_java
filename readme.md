# 🏢 ERM - Enterprise Resource Management (Sistem Manajemen Inventaris & Aset)

Aplikasi web berbasis **Spring Boot** untuk pengelolaan inventaris barang gudang, pelacakan aset perusahaan, alur peminjaman aset (*loan & return*), persetujuan (*approval*), denda (*fines*), serta riwayat pemeliharaan aset (*maintenance logs*).

Dilengkapi dengan sistem keamanan berbasis peran (*Role-Based Access Control*), UI responsif menggunakan **Thymeleaf + Bootstrap 5**, serta dokumentasi interaktif REST API menggunakan **Springdoc OpenAPI (Swagger UI)**.

---

## 📌 Daftar Isi
- [Fitur Utama](#-fitur-utama)
- [Teknologi yang Digunakan](#-teknologi-yang-digunakan)
- [Hak Akses & Akun Demo](#-hak-akses--akun-demo)
- [Prasyarat Sistem](#-prasyarat-sistem)
- [Panduan Menjalankan Aplikasi](#-panduan-menjalankan-aplikasi)
  - [1. Konfigurasi Database](#1-konfigurasi-database)
  - [2. Menjalankan Aplikasi](#2-menjalankan-aplikasi)
  - [3. Mengakses Web & API Docs](#3-mengakses-web--api-docs)
- [Perintah Gradle Bermanfaat](#-perintah-gradle-bermanfaat)
- [Struktur Direktori Proyek](#-struktur-direktori-proyek)
- [Troubleshooting & FAQ](#-troubleshooting--faq)

---

## ✨ Fitur Utama

### 1. Manajemen Inventaris (*Inventory Management*)
- **Kategori Produk**: Pengelompokan jenis barang (`/inventory/categories`).
- **Merek / Brand**: Pendataan brand manufaktur barang (`/inventory/brands`).
- **Satuan (Unit)**: Manajemen unit hitung barang (Pcs, Box, Unit, dll.) (`/inventory/units`).
- **Lokasi & Gudang**: Pemetaan gudang penyimpanan (*warehouses*) dan posisi rak/ruang (*locations*).
- **Pemasok / Supplier**: Database vendor dan supplier logistik (`/inventory/suppliers`).
- **Katalog Produk**: Manajemen data barang/stok inventaris (`/inventory/products`).

### 2. Manajemen Aset (*Asset Management*)
- **Item Aset Fisik**: Pencatatan spesifik setiap unit aset perusahaan dengan serial number dan kode aset (`/assets/items`).
- **Kondisi Aset**: Pemantauan kondisi (Baik, Rusak Ringan, Rusak Berat, dll.).
- **Log Pemeliharaan (*Maintenance Logs*)**: Riwayat perbaikan dan jadwal servis aset (`/maintenance/logs`).

### 3. Transaksi Sirkulasi & Peminjaman (*Loans & Returns*)
- **Pengajuan Pinjam (*Loan Request*)**: Formulir peminjaman aset oleh karyawan (`/loans/requests`).
- **Persetujuan Pinjam (*Approval Flow*)**: Otorisasi peminjaman oleh admin/staff gudang (`/loans/approvals`).
- **Pengembalian (*Asset Return*)**: Form inspeksi saat barang dikembalikan (`/loans/returns`).
- **Denda (*Fines*)**: Perhitungan denda jika terjadi keterlambatan atau kerusakan aset (`/loans/fines`).

### 4. Manajemen Autentikasi & Organisasi (*Auth & Org*)
- **Departemen & Jabatan**: Struktur divisi perusahaan (`/auth/departments`).
- **Data Karyawan**: Profil karyawan pengguna sistem (`/auth/employees`).
- **Manajemen Akun & Role**: Pengaturan pengguna, password terenkripsi BCrypt, dan role akses (`/auth/users`, `/auth/roles`).

### 5. Dokumentasi API & Seeder Otomatis
- **Swagger UI / OpenAPI**: Dokumentasi endpoint interaktif langsung dari browser.
- **Database Seeder**: Pengisian data master dan akun default secara otomatis pada inisialisasi pertama kali.

---

## 🛠 Teknologi yang Digunakan

| Komponen | Teknologi |
|---|---|
| **Framework Backend** | Spring Boot 4.0.3 (Java 25 / 21+) |
| **Keamanan** | Spring Security 6 (BCrypt Password Hashing, Session Based) |
| **ORM / Data Access** | Spring Data JPA, Hibernate ORM 7 |
| **Database** | MySQL (Didukung H2 Database untuk dev/testing) |
| **Template Engine** | Thymeleaf + Thymeleaf Extras Spring Security |
| **Frontend Styling** | Bootstrap 5, Bootstrap Icons, Custom CSS |
| **API Documentation** | Springdoc OpenAPI Starter WebMVC UI (v3.0.1) |
| **Build Tool** | Gradle Wrapper (v8.x) |
| **Containerization** | Docker Compose (`compose.yaml`) |

---

## 👥 Hak Akses & Akun Demo

Aplikasi sudah dilengkapi dengan data *seeder* otomatis (`DatabaseSeeder.java`). Gunakan kredensial berikut untuk masuk:

| Role | Username | Password | Deskripsi Hak Akses |
|---|---|---|---|
| **ADMIN** | `admin` | `admin` | Akses penuh ke seluruh menu (Auth, Master Data, Inventaris, Aset, Transaksi, Approval, Denda). |
| **STAFF_GUDANG** | `staff` | `staff` | Akses ke Master Inventaris, Aset, Persetujuan Peminjaman, dan Pengembalian. |
| **KARYAWAN** | `user` | `user` | Mengajukan pinjaman aset, memantau pengembalian barang miliknya, dan melihat riwayat log. |

---

## 📋 Prasyarat Sistem

Sebelum menjalankan aplikasi, pastikan komputer Anda telah terinstal:
1. **Java Development Kit (JDK)**: Versi 25 (atau JDK 21+). Cek dengan perintah:
   ```bash
   java -version
   ```
2. **MySQL Server**: Versi 8.0 ke atas (bisa via **XAMPP**, **MySQL Installer**, atau **Docker**).
3. **Git**: Untuk manajemen repositori versi.

---

## 🚀 Panduan Menjalankan Aplikasi

### 1. Konfigurasi Database

Secara default, konfigurasi koneksi database terletak di `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_inventaris?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

Pilih salah satu metode berikut untuk menjalankan database:

#### Opsi A: Menggunakan MySQL Lokal (XAMPP / MySQL Workbench)
1. Buka control panel XAMPP atau MySQL Service Anda.
2. Pastikan service **MySQL** sudah berjalan pada port `3306`.
3. Sesuaikan `username` dan `password` di `application.properties` jika database lokal Anda memiliki password.
4. Database `db_inventaris` akan otomatis dibuat oleh Spring Boot jika belum ada (`createDatabaseIfNotExist=true`).

#### Opsi B: Menggunakan Docker Compose
Di dalam direktori proyek telah disediakan file `compose.yaml`. Anda dapat menjalankan MySQL container instan:
```bash
docker compose up -d
```

---

### 2. Menjalankan Aplikasi

Buka terminal/PowerShell di direktori proyek `erm/`:

#### Untuk Pengguna Windows:
```powershell
.\gradlew bootRun
```

#### Untuk Pengguna Linux / macOS:
```bash
chmod +x gradlew
./gradlew bootRun
```

#### Mode Live Reload (Spring Boot DevTools)
Untuk pengembangan aktif dengan *hot-reload* otomatis tanpa restart manual:
- **Terminal 1** (Menjalankan server):
  ```powershell
  .\gradlew bootRun
  ```
- **Terminal 2** (Memantau perubahan kode secara terus-menerus):
  ```powershell
  .\gradlew classes --continuous
  ```

---

### 3. Mengakses Web & API Docs

Setelah terminal menampilkan log `Started ErmApplication in ... seconds`:

| Layanan | URL Akses |
|---|---|
| **Halaman Login** | [http://localhost:8080/login](http://localhost:8080/login) |
| **Dashboard Utama** | [http://localhost:8080/](http://localhost:8080/) |
| **Swagger UI (Dokumentasi API)** | [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) |
| **OpenAPI Specification (JSON)** | [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs) |

---

## 📦 Perintah Gradle Bermanfaat

| Perintah | Deskripsi |
|---|---|
| `.\gradlew bootRun` | Menjalankan aplikasi dalam mode pengembangan |
| `.\gradlew clean build` | Membersihkan cache dan mengompilasi seluruh aplikasi |
| `.\gradlew test` | Menjalankan seluruh rangkaian pengujian unit (*JUnit tests*) |
| `.\gradlew clean compileJava` | Membersihkan dan mengompilasi ulang kode sumber Java |
| `java -jar build/libs/erm-0.0.1-SNAPSHOT.jar` | Menjalankan artefak JAR hasil produksi |

---

## 📁 Struktur Direktori Proyek

Berikut adalah gambaran struktur direktori proyek beserta penjelasannya:

```text
erm/
├── .gitignore                          # Aturan pengecualian berkas Git
├── .gitattributes                      # Konfigurasi atribut Git (EOL, text/binary)
├── build.gradle                        # Definisi dependensi & task build Gradle
├── settings.gradle                     # Pengaturan nama proyek Gradle
├── gradle.properties                   # Variabel environment Gradle
├── compose.yaml                        # Definisi container Docker (MySQL)
├── gradlew / gradlew.bat               # Gradle Wrapper (Linux/macOS & Windows)
├── README.md                           # Dokumentasi utama proyek
│
├── src/
│   ├── main/
│   │   ├── java/com/bangone/erm/
│   │   │   ├── ErmApplication.java     # Entry point utama aplikasi Spring Boot
│   │   │   │
│   │   │   ├── config/                 # Konfigurasi Keamanan & Database
│   │   │   │   ├── DatabaseSeeder.java         # Seeder data awal otomatis
│   │   │   │   ├── CustomUserDetailsService.java # Integrasi Spring Security UserDetails
│   │   │   │   ├── SecurityConfig.java         # Konfigurasi filter autentikasi & otorisasi
│   │   │   │   └── SpringConfig.java           # Bean & konfigurasi Spring tambahan
│   │   │   │
│   │   │   ├── controllers/            # Controller Web (Thymeleaf) & REST API
│   │   │   │   ├── HomeController.java         # Routing Dashboard & Halaman Utama
│   │   │   │   ├── assets/                     # Controller modul Aset & Maintenance
│   │   │   │   ├── auth/                       # Controller modul Pengguna & Karyawan
│   │   │   │   ├── inventory/                  # Controller modul Produk, Brand, Kategori, dll.
│   │   │   │   └── transaction/                # Controller modul Peminjaman, Denda & Approval
│   │   │   │
│   │   │   ├── dto/                    # Data Transfer Objects (Form request & response)
│   │   │   │   ├── LoanItemForm.java
│   │   │   │   └── LoanRequestForm.java
│   │   │   │
│   │   │   ├── exceptions/             # Penanganan Error Terpusat
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   │
│   │   │   ├── models/                 # JPA Entity (Representasi Tabel Database)
│   │   │   │   ├── BaseEntity.java             # Superclass ID, CreatedAt, UpdatedAt
│   │   │   │   ├── assets/                     # AssetItem, AssetCondition, MaintenanceLog
│   │   │   │   ├── auth/                       # User, Role, Permission, Employee, Department
│   │   │   │   ├── inventory/                  # Product, Category, Brand, Unit, Supplier, Warehouse, Location
│   │   │   │   └── transaction/                # LoanRequest, LoanDetail, ApprovalLog, ReturnTransaction, Fine
│   │   │   │
│   │   │   ├── repositories/           # Spring Data JPA Repository Interfaces
│   │   │   │   ├── assets/
│   │   │   │   ├── auth/
│   │   │   │   ├── inventory/
│   │   │   │   └── transaction/
│   │   │   │
│   │   │   └── services/               # Lapisan Logika Bisnis (Business Logic Layer)
│   │   │       ├── assets/
│   │   │       ├── auth/
│   │   │       ├── inventory/
│   │   │       └── transaction/
│   │   │
│   │   └── resources/
│   │       ├── application.properties  # Konfigurasi database, port, logging, devtools
│   │       │
│   │       ├── static/                 # Aset Statis (CSS, JS, Fonts, Gambar)
│   │       │   ├── css/                # Bootstrap & Bootstrap Icons
│   │       │   ├── js/                 # Script interaktif & bundle Bootstrap
│   │       │   └── images/             # Gambar & ikon web
│   │       │
│   │       └── templates/              # View Thymeleaf HTML
│   │           ├── index.html                  # Halaman dashboard utama
│   │           ├── login.html                  # Halaman antarmuka login
│   │           ├── fragments/                  # Fragmen layout (header, sidebar, footer)
│   │           └── pages/                      # Template CRUD berdasarkan modul
│   │               ├── assets/
│   │               ├── auth/
│   │               ├── inventory/
│   │               └── transaction/
│   │
│   └── test/                           # Unit testing & integration testing
```

---

## ❓ Troubleshooting & FAQ

### 1. Error: `Communications link failure` / `Access denied for user`
- **Penyebab**: MySQL server belum aktif atau password database pada `application.properties` tidak cocok dengan MySQL Anda.
- **Solusi**: Periksa apakah MySQL berjalan di port `3306`, lalu pastikan `spring.datasource.username` dan `spring.datasource.password` di `src/main/resources/application.properties` sudah sesuai.

### 2. Port `8080` sudah digunakan (*Port already in use*)
- Ubah port pada `src/main/resources/application.properties`:
  ```properties
  server.port=8081
  ```
- Kemudian akses melalui `http://localhost:8081`.

### 3. Versi Java tidak cocok (*Unsupported class file major version*)
- Proyek ini dikonfigurasi menggunakan toolchain **Java 25**.
- Pastikan Anda menggunakan JDK yang mendukung (Java 21 ke atas) dan variabel `JAVA_HOME` mengarah ke instalasi JDK tersebut.

---

## 📄 Lisensi
Hak Cipta © 2026 ERM Project. Dikembangkan untuk keperluan manajemen operasional inventaris dan aset.