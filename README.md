# Infrafix - Backend Pelaporan Warga

## Deskripsi
Proyek ini adalah aplikasi backend untuk sistem pelaporan warga, dibangun dengan Spring Boot. Ini menyediakan API untuk warga melaporkan masalah, administrator mengelola laporan dan pengguna, serta teknisi menangani tugas yang ditugaskan.

## Struktur Folder

```
.
├── .gitignore
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── infrafix/
│   │   │           └── citizen_reporting/
│   │   │               ├── Infrafix.java
│   │   │               ├── config/
│   │   │               │   ├── JwtConfig.java
│   │   │               │   └── OtherConfig.java
│   │   │               ├── controller/
│   │   │               │   ├── AdminInitController.java
│   │   │               │   ├── AdminUserController.java
│   │   │               │   ├── AuthController.java
│   │   │               │   ├── ReportController.java
│   │   │               │   ├── RoleInitController.java
│   │   │               │   ├── StatusInitController.java
│   │   │               │   ├── TechnicianController.java
│   │   │               │   └── UserController.java
│   │   │               ├── core/
│   │   │               │   ├── IAuthService.java
│   │   │               │   ├── IReportService.java
│   │   │               │   ├── ITechnicianService.java
│   │   │               │   └── IUserService.java
│   │   │               ├── dto/
│   │   │               │   ├── request/
│   │   │               │   │   └── TechnicianRequestDTO.java
│   │   │               │   │   └── ... (DTO permintaan lainnya)
│   │   │               │   └── response/
│   │   │               │       ├── ReportResponseDTO.java
│   │   │               │       ├── TechnicianResponseDTO.java
│   │   │               │       └── UserResponseDTO.java
│   │   │               │       └── ... (DTO respons lainnya)
│   │   │               ├── dto/validation/
│   │   │               │   ├── ValReportCreateDTO.java
│   │   │               │   └── ValUserCreateDTO.java
│   │   │               ├── handler/
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   └── ResponseHandler.java
│   │   │               ├── model/
│   │   │               │   ├── Report.java
│   │   │               │   ├── Role.java
│   │   │               │   ├── Status.java
│   │   │               │   ├── Technician.java
│   │   │               │   └── User.java
│   │   │               ├── repo/
│   │   │               │   ├── ReportRepository.java
│   │   │               │   ├── RoleRepository.java
│   │   │               │   ├── StatusRepository.java
│   │   │               │   ├── TechnicianRepository.java
│   │   │               │   └── UserRepository.java
│   │   │               ├── security/
│   │   │               │   ├── BcryptCustom.java
│   │   │               │   ├── CustomUserDetails.java
│   │   │               │   ├── JwtContextUtil.java
│   │   │               │   ├── JwtFilter.java
│   │   │               │   ├── JwtUtility.java
│   │   │               │   ├── MyHttpServletRequestWrapper.java
│   │   │               │   └── SecurityConfig.java
│   │   │               ├── service/
│   │   │               │   ├── AuthService.java
│   │   │               │   ├── ReportService.java
│   │   │               │   ├── TechnicianService.java
│   │   │               │   └── UserService.java
│   │   │               └── util/
│   │   │                   ├── GlobalFunction.java
│   │   │                   ├── GlobalResponse.java
│   │   │                   ├── LoggingFile.java
│   │   │                   ├── RequestCapture.java
│   │   │                   └── TransformPagination.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── jwt.properties
│   │       ├── otherconfig.properties
│   │       ├── static/
│   │       └── templates/
│   └── test/
│       └── java/
│           └── com/
│               └── infrafix/
│                   └── citizen_reporting/
│                       └── InfrafixTests.java
├── target/
└── tokens/
```

## Detail Teknis API
API dibangun menggunakan Spring Boot dan mengikuti arsitektur RESTful. Ini berfungsi sebagai backend untuk sistem pelaporan warga, menyediakan berbagai endpoint untuk peran pengguna yang berbeda.
- **Otentikasi:** Otentikasi berbasis JWT diimplementasikan menggunakan `JwtConfig`, `JwtFilter`, dan `JwtUtility`.
- **Lapisan Data:** Spring Data JPA digunakan untuk interaksi database, dengan repositori didefinisikan dalam paket `repo`.
- **Logika Bisnis:** Layanan dalam paket `service` merangkum logika bisnis inti.
- **Controller:** Endpoint REST didefinisikan dalam paket `controller`.
- **Penanganan Error:** Penanganan exception global dikelola oleh `GlobalExceptionHandler`.
- **DTO:** Objek Transfer Data untuk permintaan dan respons terletak dalam paket `dto`.

## Endpoint API REST (Backend)
URL dasar untuk semua endpoint API biasanya `http://localhost:8080` (atau host dan port yang dikonfigurasi). Semua permintaan dan respons diharapkan dalam format JSON.

### Otentikasi
- `POST /auth/login`: Masuk pengguna.
  - **Contoh Body Permintaan:**
    ```json
    {
      "username": "admin@example.com",
      "password": "Admin123!"
    }
    ```
  - **Respons:** Masuk yang berhasil akan mengembalikan token JWT untuk permintaan yang diautentikasi selanjutnya.
- `POST /auth/register`: Registrasi pengguna. Endpoint ini memungkinkan pengguna baru membuat akun.
  - **Contoh Body Permintaan:**
    ```json
      {
        "name": "John Doe",
        "email": "john.doe@example.com",
        "password": "Password123!",
        "phoneNumber": "081234567890",
        "address": "Jl. Gatot Subroto, Jakarta Selatan",
        "postCode": "12140"
     }
    ```
  - **Respons:** Registrasi yang berhasil biasanya akan mengembalikan pesan sukses atau detail pengguna yang baru dibuat (tidak termasuk informasi sensitif).

### Warga
- `POST /report/create`: Buat laporan baru (Hanya Warga).
  - **Contoh Body Permintaan:**
    ```json
    {
      "title": "Lubang Jalan di Main Street",
      "description": "Ada lubang besar dekat persimpangan Main dan 1st.",
      "street": "Main Street",
      "city": "Jakarta",
      "province": "DKI Jakarta",
      "postCode": "12140"
    }
    ```
  - **Respons:** Detail laporan yang baru dibuat.
- `GET /report/all`: Dapatkan semua laporan (Admin, Teknisi, Warga).
  - **Respons:** Daftar semua laporan.
- `GET /report/{id}`: Dapatkan laporan spesifik berdasarkan ID (Admin, Teknisi, Warga).
  - **Variabel Path:** `id` (Long)
  - **Respons:** Detail laporan spesifik.
- `GET /report/{sort}/{sort_by}/{page}`: Cari laporan berdasarkan parameter (Hanya Admin).
  - **Variabel Path:** `sort` (String - "asc" atau "desc"), `sort_by` (String - "title", "created", "updated", "id"), `page` (Integer)
  - **Parameter Query:** `column` (String), `value` (String), `size` (Integer)
  - **Respons:** Daftar laporan yang dipaginasi yang cocok dengan kriteria.
- `GET /report/download/{reportId}`: Unduh laporan sebagai PDF.
  - **Variabel Path:** `reportId` (Long)
  - **Respons:** File PDF laporan.

### Admin
- `POST /adminuser/create-technician`: Buat pengguna teknisi baru (Hanya Admin).
  - **Respons:** Detail pengguna teknisi yang baru dibuat.
- `POST /adminuser/create-admin`: Buat pengguna admin baru (Hanya Admin).
  - **Respons:** Detail pengguna admin yang baru dibuat.
- `GET /admin/users`: Dapatkan semua pengguna
- `GET /admin/users/{id}`: Dapatkan pengguna spesifik berdasarkan ID
- `PUT /admin/users/{id}/role`: Perbarui peran pengguna
- `GET /admin/reports`: Dapatkan semua laporan
- `PUT /report/{id}/status/{statusId}`: Perbarui status laporan (Hanya Admin).
  - **Variabel Path:** `id` (Long - ID laporan), `statusId` (Long - ID status baru)
  - **Respons:** Konfirmasi pembaruan status.
- `PUT /report/cancel/{id}`: Batal laporan (Hanya Admin).
  - **Variabel Path:** `id` (Long - ID laporan)
  - **Respons:** Konfirmasi pembatalan laporan.
- `PUT /admin/reports/{id}/status`: Perbarui status laporan
- `PUT /admin/reports/{id}/assign`: Tugaskan laporan ke teknisi

### Teknisi
- `POST /tech/assign`: Tugaskan laporan ke teknisi.
  - **Respons:** Konfirmasi penugasan.
- `POST /tech/unassign/{reportId}`: Batal tugas teknisi dari laporan.
  - **Variabel Path:** `reportId` (Long)
  - **Respons:** Konfirmasi pembatalan tugas.
- `GET /tech/assignment/all`: Dapatkan semua penugasan teknisi (Hanya Admin).
  - **Parameter Query:** `page`, `size`, `sort` (untuk paginasi)
  - **Respons:** Daftar semua penugasan yang dipaginasi.
- `GET /tech/assignment/{id}`: Dapatkan penugasan teknisi spesifik berdasarkan ID (Hanya Admin).
  - **Variabel Path:** `id` (Long)
  - **Respons:** Detail penugasan spesifik.
- `PUT /report/complete/{reportId}`: Selesaikan laporan (Admin, Teknisi).
  - **Variabel Path:** `reportId` (Long)
  - **Respons:** Konfirmasi penyelesaian laporan.
- `GET /technician/reports/assigned`: Dapatkan laporan yang ditugaskan ke teknisi
- `PUT /technician/reports/{id}/status`: Perbarui status laporan yang ditugaskan

### API Transaksional
API ini mewakili operasi inti dan perubahan status dalam sistem pelaporan.
- `POST /citizen/reports`: Buat laporan baru (operasi transaksional)
- `PUT /admin/reports/{id}/status`: Perbarui status laporan (operasi transaksional)
- `PUT /admin/reports/{id}/assign`: Tugaskan laporan ke teknisi (operasi transaksional)
- `PUT /technician/reports/{id}/status`: Teknisi memperbarui status laporan yang ditugaskan (operasi transaksional)

### Inisialisasi
- `POST /roles/init`: Inisialisasi peran (misalnya, ADMIN, CITIZEN, TECHNICIAN)
- `POST /status/init`: Inisialisasi status laporan (misalnya, Pending, In Progress, Completed, Rejected)
- `POST /admin/init`: Inisialisasi pengguna admin

## Admin
Modul admin menyediakan fungsionalitas untuk mengelola sistem.
- **Manajemen Pengguna:** Admin dapat mengelola akun pengguna (misalnya, buat, perbarui, hapus, tugaskan peran).
- **Manajemen Laporan:** Admin dapat melihat, memperbarui status, dan menugaskan laporan ke teknisi.
- **Inisialisasi:** `AdminInitController` dan `RoleInitController` kemungkinan digunakan untuk setup awal pengguna admin dan peran.

## Warga
Modul warga memungkinkan warga berinteraksi dengan sistem pelaporan.
- **Pengiriman Laporan:** Warga dapat mengirim laporan baru mengenai berbagai masalah.
- **Pelacakan Laporan:** Warga dapat melihat status dan detail laporan yang mereka kirim.
- **Otentikasi:** Warga dapat mendaftar dan masuk ke sistem.

## Inisialisasi
Proyek ini mencakup beberapa controller inisialisasi dan konfigurasi:
- `AdminInitController`: Kemungkinan bertanggung jawab untuk membuat pengguna admin awal.
- `RoleInitController`: Kemungkinan bertanggung jawab untuk mengisi peran awal (misalnya, ADMIN, CITIZEN, TECHNICIAN).
- `StatusInitController`: Kemungkinan bertanggung jawab untuk mengisi status laporan awal (misalnya, PENDING, IN_PROGRESS, RESOLVED).
- `application.properties`, `jwt.properties`, `otherconfig.properties`: File konfigurasi untuk database, pengaturan JWT, dan properti spesifik aplikasi lainnya.
