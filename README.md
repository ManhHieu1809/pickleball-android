# 🏓 Pickleball

Ứng dụng Android hiện đại dành cho người chơi pickleball — đặt sân, tìm & tạo trận đấu, leo bảng xếp hạng và quản lý ví, tất cả trong một nơi.

Xây dựng bằng **Kotlin**, **Jetpack Compose** và **Material 3**.

---

## ✨ Tính năng nổi bật

| Tính năng | Mô tả |
|---|---|
| **Onboarding & Đăng nhập** | Màn hình chào mừng, đăng nhập email, đăng nhập Google |
| **Trang chủ** | Chỉ số rating, thao tác nhanh, sân gần đây, trận sắp tới |
| **Đặt sân** | Duyệt sân → xem chi tiết → chọn ngày giờ → thanh toán → xác nhận |
| **Hệ thống trận đấu** | Tìm trận đang mở, tham gia với đặt cọc, hoặc tự tạo trận |
| **Xếp hạng có thứ hạng** | Phòng chờ trước trận, mời đồng đội duo, tìm kiếm thời gian thực & đếm ngược |
| **Hồ sơ cá nhân** | Điểm Elo, thống kê mùa giải, lịch sử trận, phân tích hiệu suất |
| **Bảng xếp hạng** | Xếp hạng cạnh tranh giữa tất cả người chơi |
| **Ví điện tử** | Số dư trong app với nạp tiền, rút tiền và lịch sử giao dịch |

---

## 🛠 Công nghệ sử dụng

- **Kotlin** — 100% Kotlin
- **Jetpack Compose** — UI khai báo với Material 3
- **Compose Navigation** — Kiến trúc Single-Activity với 30+ màn hình
- **Accompanist** — Hiệu ứng chuyển màn hình, điều khiển System UI
- **Coil 3** — Tải ảnh bất đồng bộ với OkHttp
- **Kotlin Coroutines** — Lập trình bất đồng bộ
- **DataStore** — Lưu trữ cài đặt cục bộ nhẹ
- **Google Play Services** — Đăng nhập bằng Google

---

## 📐 Kiến trúc

```
Single Activity (MainActivity)
└── AppNavigation (Compose NavHost)
    ├── Onboarding / Đăng nhập / Đăng ký
    ├── Home (với Bottom Navigation)
    │   ├── Tab Trang chủ   — Dashboard với thống kê & thao tác nhanh
    │   ├── Tab Trận đấu    — Duyệt & tham gia trận
    │   ├── Tab Đặt sân     — Xem lịch sử đặt sân
    │   ├── Tab Sân         — Khám phá sân gần đây
    │   └── Tab Hồ sơ       — Thông tin cá nhân & cài đặt
    ├── Chi tiết sân → Đặt sân → Thanh toán → Xác nhận
    ├── Chi tiết trận → Tham gia → Thành công
    ├── Tạo trận → Dự toán chi phí → Đã tạo
    ├── Xếp hạng: Phòng chờ → Mời Duo → Đang tìm → Tìm thấy → Bắt đầu
    └── Hồ sơ → Ví / Bảng XH / Lịch sử / Phân tích
```

---

## 📱 Tổng quan màn hình

**30+ màn hình** bao phủ toàn bộ hành trình người dùng:

- Onboarding · Đăng nhập · Đăng ký
- Trang chủ · Danh sách sân · Chi tiết sân
- Luồng đặt sân (4 màn hình) · Lịch sử đặt sân
- Tìm trận · Chi tiết trận · Xác nhận tham gia · Tham gia thành công
- Tạo trận · Dự toán chi phí · Tạo thành công
- Xếp hạng (Phòng chờ · Mời đồng đội · Đang tìm · Tìm thấy · Bắt đầu)
- Hồ sơ · Bảng xếp hạng · Lịch sử trận · Phân tích hiệu suất
- Ví · Nạp tiền · Rút tiền · Xác nhận rút · Thành công

---

## 🎨 Điểm nhấn thiết kế

- **Bottom navigation glassmorphism** — Thanh điều hướng tối kiểu frosted-glass với điểm nhấn xanh neon
- **Hiệu ứng chuyển màn hình mượt mà** — Fade, slide và scale giữa các màn hình
- **Hệ thống màu riêng** — Bảng màu chủ đề thể thao với màu cấp bậc (Đồng → Kim Cương)
- **Card gradient & UI hiện đại** — Bo góc, đổ bóng, chi tiết tinh tế

---

## 🚀 Hướng dẫn chạy dự án

**Yêu cầu:** Android Studio Ladybug+ · JDK 11+ · Android SDK 36

```bash
git clone https://github.com/<your-username>/pickleball.git
cd pickleball
./gradlew assembleDebug
```

Hoặc mở bằng Android Studio và nhấn **Run ▶️**.

---

## 📄 Giấy phép

Dự án này được tạo cho mục đích portfolio & demo.
