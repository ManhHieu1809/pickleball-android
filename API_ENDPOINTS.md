# Pickleball Platform - API Endpoints Reference

> **FOR AI AGENTS**: Read this file to understand ALL existing APIs before implementing new features.
> **Last Updated**: 2025-01-31

---

## 📋 Summary

| Controller | Base Path | Endpoints Count |
|------------|-----------|-----------------|
| AuthController | `/api/auth` | 4 |
| UserController | `/api/users` | 1 |
| AdminController | `/api/admin` | 4 |
| VenueController | `/api/venues` | 11 |
| CourtController | `/api/courts` | 6 |
| BookingController | `/api/bookings` | 4 |
| TimeSlotController | `/api/courts/{courtId}/slots` | 2 |
| **VenueStaffController** | `/api/staff` | **7** |
| HealthController | `/api/health` | 1 |

**Total: 40 endpoints**

---

## 🔐 AuthController (`/api/auth`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/register` | Register new user, returns JWT tokens | ❌ No |
| `POST` | `/login` | Login, returns JWT tokens | ❌ No |
| `POST` | `/refresh-token` | Refresh access token | ❌ No |
| `GET` | `/me` | Get current user from token | ✅ Yes |

### Request/Response Examples:

**Register:**
```json
POST /api/auth/register
Request: { "email", "password", "fullName", "phoneNumber" }
Response: { "accessToken", "refreshToken", "tokenType", "expiresIn", "user" }
```

**Login:**
```json
POST /api/auth/login
Request: { "email", "password" }
Response: { "accessToken", "refreshToken", "tokenType", "expiresIn", "user" }
```

---

## 👤 UserController (`/api/users`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `GET` | `/{userId}` | Get user profile by ID | ❌ No (public) |

---

## 👨‍💼 AdminController (`/api/admin`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/role-requests/venue-owner` | Submit venue owner request | ✅ Yes |
| `GET` | `/role-requests/pending` | Get pending role requests | ✅ Admin |
| `POST` | `/role-requests/{requestId}/approve` | Approve role request | ✅ Admin |
| `POST` | `/role-requests/{requestId}/reject` | Reject role request | ✅ Admin |

---

## 🏟️ VenueController (`/api/venues`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/` | Create new venue | ✅ Venue Owner |
| `POST` | `/{venueId}/approve` | Approve venue (Admin) | ✅ Admin |
| `POST` | `/{venueId}/reject` | Reject venue (Admin) | ✅ Admin |
| `GET` | `/pending` | Get pending venues for approval | ✅ Admin |
| `PUT` | `/{venueId}` | Update venue | ✅ Venue Owner |
| `GET` | `/active` | Get all active venues | ❌ No |
| `GET` | `/nearby` | Get nearby venues (lat, lng, radius) | ❌ No |
| `GET` | `/{venueId}` | Get venue by ID | ❌ No |
| `GET` | `/owner/{ownerId}` | Get venues by owner | ✅ Yes |
| `PUT` | `/{venueId}/activate` | Activate venue | ✅ Owner/Admin |
| `PUT` | `/{venueId}/deactivate` | Deactivate venue | ✅ Owner/Admin |

---

## 🎾 CourtController (`/api/courts`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/` | Create new court | ✅ Venue Owner |
| `GET` | `/venue/{venueId}` | Get courts by venue | ❌ No |
| `GET` | `/{courtId}` | Get court details | ❌ No |
| `GET` | `/venue/{venueId}/active` | Get active courts by venue | ❌ No |
| `GET` | `/active` | Get all active courts | ❌ No |
| `PUT` | `/{courtId}/activate` | Activate court | ✅ Owner/Admin |
| `PUT` | `/{courtId}/deactivate` | Deactivate court | ✅ Owner/Admin |

---

## 📅 BookingController (`/api/bookings`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/` | Create booking | ✅ Yes |
| `POST` | `/{bookingId}/join` | Join existing booking | ✅ Yes |
| `POST` | `/{bookingId}/cancel` | Cancel booking | ✅ Yes |
| `GET` | `/{bookingId}` | Get booking details | ✅ Yes |

### Booking Types (from WORKFLOW.md):
- `PRIVATE` - Host pays 100%, no matching (✅ Implemented with Payment)
- `CASUAL` - 4 players share cost, no Elo
- `RANKED` - 4 players + 1 referee, Elo changes
- `WALK_IN` - Staff creates for walk-in customers (✅ Implemented)

---

## 👷 VenueStaffController (`/api/staff`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/login` | Staff login, returns JWT | ❌ No |
| `POST` | `/` | Create new staff | ✅ Venue Owner |
| `GET` | `/venue/{venueId}` | Get all staff for venue | ✅ Venue Owner |
| `PUT` | `/{staffId}/deactivate` | Deactivate staff | ✅ Venue Owner |
| `PUT` | `/{staffId}/activate` | Activate staff | ✅ Venue Owner |
| `PUT` | `/{staffId}/permissions` | Update staff permissions | ✅ Venue Owner |
| `POST` | `/walk-in-booking` | Create walk-in booking | ✅ Staff |

### Staff Permissions:
- `CAN_CREATE_BOOKING` - Create walk-in bookings
- `CAN_CHECK_IN` - Check-in customers
- `CAN_VIEW_REVENUE` - View revenue reports
- `CAN_CANCEL_BOOKING` - Cancel bookings

---

### 1. Staff Login
**POST** `/api/staff/login`

**Request:**
```json
{
  "username": "staff_user1",
  "password": "securePassword123"
}
```

**Response (Success):**
```json
{
  "success": true,
  "message": "Đăng nhập thành công",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "staff": {
      "id": 1,
      "venueId": 5,
      "venueName": "Sân Pickleball Cầu Giấy",
      "username": "staff_user1",
      "fullName": "Nguyễn Văn An",
      "isActive": true,
      "permissions": [
        "CAN_CREATE_BOOKING",
        "CAN_CHECK_IN"
      ],
      "createdAt": "2025-01-15T09:00:00"
    }
  },
  "timestamp": "2025-02-01T10:30:00"
}
```

**Response (Error - Invalid credentials):**
```json
{
  "success": false,
  "message": "Username hoặc mật khẩu không đúng",
  "data": null,
  "timestamp": "2025-02-01T10:30:00"
}
```

---

### 2. Create Staff (by Venue Owner)
**POST** `/api/staff?ownerId=3`

**Request:**
```json
{
  "venueId": 5,
  "username": "staff_newbie",
  "password": "newStaffPass123",
  "fullName": "Trần Thị Bình",
  "permissions": [
    "CAN_CREATE_BOOKING",
    "CAN_CHECK_IN"
  ]
}
```

**Response (Success):**
```json
{
  "success": true,
  "message": "Tạo nhân viên thành công",
  "data": {
    "id": 2,
    "venueId": 5,
    "venueName": "Sân Pickleball Cầu Giấy",
    "username": "staff_newbie",
    "fullName": "Trần Thị Bình",
    "isActive": true,
    "permissions": [
      "CAN_CREATE_BOOKING",
      "CAN_CHECK_IN"
    ],
    "createdAt": "2025-02-01T10:35:00"
  },
  "timestamp": "2025-02-01T10:35:00"
}
```

**Response (Error - Not venue owner):**
```json
{
  "success": false,
  "message": "Bạn không có quyền tạo staff cho venue này",
  "data": null,
  "timestamp": "2025-02-01T10:35:00"
}
```

---

### 3. Get Staff by Venue
**GET** `/api/staff/venue/{venueId}?ownerId=3`

**Response (Success):**
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "venueId": 5,
      "venueName": "Sân Pickleball Cầu Giấy",
      "username": "staff_user1",
      "fullName": "Nguyễn Văn An",
      "isActive": true,
      "permissions": [
        "CAN_CREATE_BOOKING",
        "CAN_CHECK_IN",
        "CAN_VIEW_REVENUE"
      ],
      "createdAt": "2025-01-15T09:00:00"
    },
    {
      "id": 2,
      "venueId": 5,
      "venueName": "Sân Pickleball Cầu Giấy",
      "username": "staff_newbie",
      "fullName": "Trần Thị Bình",
      "isActive": true,
      "permissions": [
        "CAN_CREATE_BOOKING",
        "CAN_CHECK_IN"
      ],
      "createdAt": "2025-02-01T10:35:00"
    },
    {
      "id": 3,
      "venueId": 5,
      "venueName": "Sân Pickleball Cầu Giấy",
      "username": "staff_inactive",
      "fullName": "Lê Văn Cường",
      "isActive": false,
      "permissions": [
        "CAN_CREATE_BOOKING"
      ],
      "createdAt": "2025-01-10T08:00:00"
    }
  ],
  "timestamp": "2025-02-01T10:40:00"
}
```

---

### 4. Activate Staff
**PUT** `/api/staff/{staffId}/activate?ownerId=3`

**Response (Success):**
```json
{
  "success": true,
  "message": "Đã kích hoạt nhân viên",
  "data": {
    "id": 3,
    "venueId": 5,
    "venueName": "Sân Pickleball Cầu Giấy",
    "username": "staff_inactive",
    "fullName": "Lê Văn Cường",
    "isActive": true,
    "permissions": [
      "CAN_CREATE_BOOKING"
    ],
    "createdAt": "2025-01-10T08:00:00"
  },
  "timestamp": "2025-02-01T10:45:00"
}
```

---

### 5. Deactivate Staff
**PUT** `/api/staff/{staffId}/deactivate?ownerId=3`

**Response (Success):**
```json
{
  "success": true,
  "message": "Đã vô hiệu hóa nhân viên",
  "data": {
    "id": 2,
    "venueId": 5,
    "venueName": "Sân Pickleball Cầu Giấy",
    "username": "staff_newbie",
    "fullName": "Trần Thị Bình",
    "isActive": false,
    "permissions": [
      "CAN_CREATE_BOOKING",
      "CAN_CHECK_IN"
    ],
    "createdAt": "2025-02-01T10:35:00"
  },
  "timestamp": "2025-02-01T10:50:00"
}
```

**Response (Error - Staff not found):**
```json
{
  "success": false,
  "message": "Không tìm thấy nhân viên với ID: 99",
  "data": null,
  "timestamp": "2025-02-01T10:50:00"
}
```

---

### 6. Update Staff Permissions
**PUT** `/api/staff/{staffId}/permissions?ownerId=3`

**Request:**
```json
[
  "CAN_CREATE_BOOKING",
  "CAN_CHECK_IN",
  "CAN_VIEW_REVENUE",
  "CAN_CANCEL_BOOKING"
]
```

**Response (Success):**
```json
{
  "success": true,
  "message": "Đã cập nhật quyền của nhân viên",
  "data": {
    "id": 1,
    "venueId": 5,
    "venueName": "Sân Pickleball Cầu Giấy",
    "username": "staff_user1",
    "fullName": "Nguyễn Văn An",
    "isActive": true,
    "permissions": [
      "CAN_CREATE_BOOKING",
      "CAN_CHECK_IN",
      "CAN_VIEW_REVENUE",
      "CAN_CANCEL_BOOKING"
    ],
    "createdAt": "2025-01-15T09:00:00"
  },
  "timestamp": "2025-02-01T10:55:00"
}
```

**Response (Error - Invalid permission):**
```json
{
  "success": false,
  "message": "Quyền không hợp lệ: INVALID_PERMISSION",
  "data": null,
  "timestamp": "2025-02-01T10:55:00"
}
```

---

### 7. Create Walk-in Booking (by Staff)
**POST** `/api/staff/walk-in-booking?staffId=1`

**Request:**
```json
{
  "courtId": 1,
  "startTime": "2025-02-01T10:00:00",
  "endTime": "2025-02-01T11:00:00",
  "customerName": "Nguyễn Văn A",
  "customerPhone": "0901234567",
  "paymentMethod": "CASH",
  "notes": "Khách quen"
}
```

**Response (Success):**
```json
{
  "success": true,
  "message": "Tạo booking walk-in thành công",
  "data": {
    "id": 3,
    "courtId": 1,
    "courtName": "Sân A1",
    "venueId": 5,
    "venueName": "Sân Pickleball Cầu Giấy",
    "startTime": "2025-02-01T11:00:00",
    "endTime": "2025-02-01T12:00:00",
    "bookingType": "WALK_IN",
    "status": "CONFIRMED",
    "customerName": "Nguyễn Văn A",
    "customerPhone": "0901234567",
    "paymentMethod": "CASH",
    "notes": "[WALK-IN] Khách: Nguyễn Văn A | SĐT: 0901234567 | Thanh toán: CASH | Ghi chú: Khách quen",
    "createdByStaffId": 1,
    "venueFee": 180000.00,
    "platformFee": 36000.00,
    "totalCost": 216000.00,
    "createdAt": "2026-02-01T20:32:04.9407193",
    "payment": {
      "transactionId": "WALK_IN_TXN_3_1738414324940",
      "status": "SUCCESS",
      "amount": 216000.00,
      "currency": "VND",
      "message": "Walk-in payment recorded"
    }
  },
  "timestamp": "2026-02-01T20:32:05.0094415"
}
```

**Response (Error - Staff no permission):**
```json
{
  "success": false,
  "message": "Staff không có quyền tạo booking",
  "data": null,
  "timestamp": "2025-02-01T09:55:00"
}
```

**Response (Error - Time slot not available):**
```json
{
  "success": false,
  "message": "Khung giờ này đã được đặt",
  "data": null,
  "timestamp": "2025-02-01T09:55:00"
}
```

---

## ⏰ TimeSlotController (`/api/courts/{courtId}/slots`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `GET` | `/` | Get available slots for date | ❌ No |
| `GET` | `/all` | Get all slots (including booked) | ✅ Owner/Admin |

### Query Parameters:
- `date` - Format: `yyyy-MM-dd`

---

## 🏥 HealthController (`/api/health`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `GET` | `/` | Health check | ❌ No |

---

## 🔒 Authentication

### Using JWT Token:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Public Endpoints (No auth required):
- `/api/auth/**`
- `/api/health`
- `/api/users/{userId}`
- `/api/venues/active`
- `/api/venues/nearby`
- `/api/venues/{venueId}`
- `/api/courts/**` (GET methods)
- `/api/courts/{courtId}/slots` (GET)

---

## 📝 Notes for AI Agents

1. **Before creating new endpoint**: Check this file first!
2. **Naming convention**: 
   - GET for retrieving data
   - POST for creating/actions
   - PUT for updating
   - DELETE for removing (not used yet)
3. **Response format**: All responses use `ApiResponse<T>` wrapper
4. **Validation**: Use `@Valid` for request body validation
