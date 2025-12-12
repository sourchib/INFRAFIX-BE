# Infrafix - Backend Pelaporan Warga

## Deskripsi
Proyek ini adalah aplikasi backend untuk sistem pelaporan warga, dibangun dengan Spring Boot. Sistem ini menyediakan API yang memungkinkan warga untuk melaporkan masalah infrastruktur atau layanan publik, administrator untuk mengelola laporan dan pengguna, serta teknisi untuk menangani tugas perbaikan yang ditugaskan.

## Struktur Folder
Berikut adalah struktur direktori proyek ini:

```
.
├── .dockerignore
├── .github/
├── .gitignore
├── Dockerfile
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── infrafix/
│   │   │           └── citizen_reporting/
│   │   │               ├── Infrafix.java (Main Application)
│   │   │               ├── config/ (Konfigurasi Proyek)
│   │   │               ├── controller/ (Endpoint API)
│   │   │               ├── core/ (Interface Layanan)
│   │   │               ├── dto/ (Data Transfer Objects)
│   │   │               ├── handler/ (Global Exception Handling)
│   │   │               ├── model/ (Entity Database)
│   │   │               ├── repo/ (Repository JPA)
│   │   │               ├── security/ (Konfigurasi Keamanan & JWT)
│   │   │               ├── service/ (Logika Bisnis)
│   │   │               └── util/ (Utility & Helper Global)
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── jwt.properties
│   │       └── otherconfig.properties
│   └── test/
├── target/
└── tokens/
```

## Teknologi yang Digunakan
*   **Java 17+**
*   **Spring Boot 3.x** (Web, Data JPA, Security, Validation)
*   **Database:** MySQL / PostgreSQL (sesuai konfigurasi)
*   **Keamanan:** Spring Security & JWT (JSON Web Token)
*   **Build Tool:** Maven

## Instalasi dan Menjalankan

1.  **Clone Repository**
    ```bash
    git clone https://github.com/username/INFRAFIX-BE.git
    cd INFRAFIX-BE
    ```

2.  **Konfigurasi Database**
    Sesuaikan pengaturan database di `src/main/resources/application.properties`.

3.  **Build dan Jalankan**
    ```bash
    mvn spring-boot:run
    ```

## Dokumentasi API

Base URL: `http://localhost:8080/`

Berikut adalah daftar endpoint API yang tersedia, dikelompokkan berdasarkan fungsinya.

### 1. Otentikasi & Inisialisasi
Endpoint untuk login, registrasi, dan inisialisasi awal sistem.

| Method | Endpoint             | Deskripsi                                      | Akses       |
| :----- | :------------------- | :--------------------------------------------- | :---------- |
| POST   | `/auth/login`        | Masuk ke sistem (mendapatkan token).           | Publik      |
| POST   | `/auth/register`     | Mendaftar sebagai warga baru.                  | Publik      |
| GET    | `/auth/verify-email` | Verifikasi email pengguna.                     | Publik      |
| POST   | `/admin/init`        | Inisialisasi akun Super Admin pertama.         | Publik      |
| POST   | `/roles/init`        | Inisialisasi Role (Admin, Citizen, Technician).| Publik      |
| POST   | `/status/init`       | Inisialisasi Status Laporan.                   | Publik      |

### 2. Manajemen Pengguna
Endpoint untuk mengelola data pengguna (Warga, Admin, Teknisi).

| Method | Endpoint                        | Deskripsi                                   | Akses |
| :----- | :------------------------------ | :------------------------------------------ | :---- |
| GET    | `/users`                        | Mendapatkan semua pengguna.                 | Admin |
| GET    | `/users/{id}`                   | Mendapatkan detail pengguna berdasarkan ID. | Admin |
| POST   | `/users`                        | Membuat pengguna (Warga) baru.              | Publik|
| PUT    | `/users/{id}`                   | Memperbarui data pengguna.                  | User  |
| DELETE | `/users/{id}`                   | Menghapus pengguna.                         | Admin |
| POST   | `/adminuser/create-technician`  | Membuat akun Teknisi baru.                  | Admin |
| POST   | `/adminuser/create-admin`       | Membuat akun Admin baru.                    | Admin |
| GET    | `/users/{sort}/{sort_by}/{page}`| Mencari pengguna dengan filter & pagination.| Admin |

### 3. Manajemen Laporan
Endpoint untuk membuat, melihat, dan mengelola status laporan warga.

| Method | Endpoint                             | Deskripsi                                      | Akses                      |
| :----- | :----------------------------------- | :--------------------------------------------- | :------------------------- |
| POST   | `/report/create`                     | Membuat laporan baru.                          | Warga                      |
| GET    | `/report/all`                        | Mendapatkan semua laporan.                     | Semua                      |
| GET    | `/report/{id}`                       | Mendapatkan detail laporan.                    | Semua                      |
| GET    | `/report/download/{reportId}`        | Mengunduh laporan dalam format PDF.            | Semua                      |
| PUT    | `/report/{id}/status/{statusId}`     | Memperbarui status laporan secara manual.      | Admin                      |
| PUT    | `/report/complete/{reportId}`        | Menandai laporan sebagai selesai.              | Admin, Teknisi             |
| PUT    | `/report/cancel/{id}`                | Membatalkan laporan.                           | Admin                      |
| DELETE | `/report/delete/{id}`                | Menghapus laporan.                             | Admin                      |
| GET    | `/report/{sort}/{sort_by}/{page}`    | Mencari laporan dengan filter parameter.       | Admin                      |

### 4. Manajemen Teknisi & Penugasan
Endpoint khusus untuk operasional teknisi dan penugasan kerja.

| Method | Endpoint                                   | Deskripsi                                      | Akses            |
| :----- | :----------------------------------------- | :--------------------------------------------- | :--------------- |
| POST   | `/tech/assign`                             | Menugaskan teknisi ke sebuah laporan.          | Admin            |
| POST   | `/tech/unassign/{reportId}`                | Membatalkan penugasan teknisi.                 | Admin            |
| GET    | `/tech/assignment/all`                     | Melihat semua daftar penugasan.                | Admin            |
| GET    | `/tech/assignment/{id}`                    | Melihat detail penugasan.                      | Admin            |
| GET    | `/tech/reports/{sort}/{sort_by}/{page}`    | Filter laporan khusus teknisi.                 | Admin, Teknisi   |

## Lisensi

Hak Cipta (c) 2025 Infrafix Team.

Dilisensikan di bawah Lisensi MIT. Lihat file [LICENSE](LICENSE) untuk detail lebih lanjut.

---
*Dibuat dengan ❤️ oleh Tim Infrafix.*
