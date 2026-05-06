# Pickleball Platform - API Endpoints Reference

> **FOR AI AGENTS**: Read this file to understand ALL existing APIs before implementing new features.
> **Last Updated**: 2026-03-08

---

## 📋 Summary

| Controller | Base Path | Endpoints Count |
|------------|-----------|-----------------|
| AuthController | `/api/auth` | 4 |
| UserController | `/api/users` | 1 |
| **AdminController** | `/api/admin` | **23** |
| VenueController | `/api/venues` | 11 |
| CourtController | `/api/courts` | 6 |
| **BookingController** | `/api/bookings` | **19** |
| **PlayerController** | `/api/players` | **3** |
| **RefereeController** | `/api/referee` | **8** |
| TimeSlotController | `/api/courts/{courtId}/slots` | 2 |
| VenueStaffController | `/api/staff` | 8 |
| **MatchmakingController** | `/api/matchmaking` | **3** |
| **WalletController** | `/api/wallet` | **4** |
| HealthController | `/api/health` | 1 |

**Total: 83 endpoints**

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
| `GET` | `/dashboard/stats` | Get aggregated dashboard stats | ✅ Admin |
| `POST` | `/role-requests/venue-owner` | Submit venue owner request | ✅ Yes |
| `GET` | `/role-requests/pending` | Get pending role requests | ✅ Admin |
| `POST` | `/role-requests/{requestId}/approve` | Approve role request | ✅ Admin |
| `POST` | `/role-requests/{requestId}/reject` | Reject role request | ✅ Admin |
| `GET` | `/users` | Get paginated list of all users | ✅ Admin |
| `GET` | `/users/stats` | Get user statistics | ✅ Admin |
| `GET` | `/users/{userId}` | Get detailed user profile | ✅ Admin |
| `GET` | `/bookings` | Get paginated list of bookings | ✅ Admin |
| `GET` | `/bookings/stats` | Get booking statistics | ✅ Admin |
| `GET` | `/bookings/{bookingId}` | Get detailed booking info | ✅ Admin |
| `PUT` | `/bookings/{bookingId}/cancel` | Cancel a booking | ✅ Admin |
| `GET` | `/venues` | Get paginated list of venues | ✅ Admin |
| `GET` | `/venues/stats` | Get venue statistics | ✅ Admin |
| `GET` | `/venues/{venueId}` | Get venue details | ✅ Admin |
| `GET` | `/referee-requests/pending` | Get pending referee requests | ✅ Admin |
| `POST` | `/referee-requests/{requestId}/approve` | Approve referee request | ✅ Admin |
| `POST` | `/referee-requests/{requestId}/reject` | Reject referee request | ✅ Admin |
| `GET` | `/disputes` | Get all disputes | ✅ Admin |
| `POST` | `/disputes/{disputeId}/resolve` | Resolve a dispute | ✅ Admin |
| `GET` | `/finance/stats` | Get finance stats | ✅ Admin |
| `GET` | `/finance/chart` | Get finance chart | ✅ Admin |
| `GET` | `/finance/transactions` | Get finance transactions | ✅ Admin |

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
| `POST` | `/` | Create booking (auto-routes by type) | ✅ Yes |
| `GET` | `/{bookingId}` | Get booking details | ✅ Yes |
| `GET` | `/my` | Get current user's bookings | ✅ Yes |
| `GET` | `/owner/{ownerId}` | Get bookings by owner | ✅ Owner |
| `GET` | `/venue/{venueId}` | Get bookings by venue | ✅ Venue Owner |
| `POST` | `/{bookingId}/join` | Join existing booking (deposit + Elo check) | ✅ Yes |
| `POST` | `/{bookingId}/cancel` | Cancel booking | ✅ Yes |
| `POST` | `/{bookingId}/check-in` | GPS Check-in at venue | ✅ Yes |
| `POST` | `/{bookingId}/submit-result` | Referee submit match result | ✅ Referee |
| `POST` | `/{bookingId}/confirm-result` | Player confirm result | ✅ Player |
| **Casual Matches** | | | |
| `POST` | `/casual` | Create casual match + find candidates | ✅ Yes |
| `GET` | `/casual/available` | List PENDING casual matches | ❌ No |
| `GET` | `/{bookingId}/candidates` | Get matching candidates for casual match | ✅ Yes |
| **Ranked Matches** | | | |
| `POST` | `/ranked` | Create ranked match | ✅ Yes |
| `GET` | `/ranked/available` | List available ranked matches | ❌ No |
| `GET` | `/{bookingId}/ranked-candidates` | Get candidates for ranked match | ✅ Yes |
| `POST` | `/{bookingId}/submit-result` | Submit match result (Referee) | ✅ Referee |
| `POST` | `/{bookingId}/confirm-result` | Confirm match result (Player) | ✅ Player |
| `POST` | `/{bookingId}/disputes` | Submit dispute | ✅ Player |

### Booking Types (from WORKFLOW.md):
- `PRIVATE` - Host pays 100%, no matching (✅ Implemented with Payment)
- `CASUAL` - 4 players share cost 25% each, Elo ±200 matchmaking (✅ Implemented)
- `RANKED` - 4 players + 1 referee, Elo changes (✅ Implemented)
- `WALK_IN` - Staff creates for walk-in customers (✅ Implemented)

### Casual Match Endpoints Detail:

**POST** `/api/bookings/casual` - Create Casual Match
```json
Request: {
  "courtId": 1,
  "startTime": "2026-03-10T14:00:00",
  "endTime": "2026-03-10T15:00:00",
  "creatorUserId": 5
}
Response: {
  "booking": { "id", "courtId", "status": "PENDING", "bookingType": "CASUAL", "venueFee", "totalCost" },
  "payment": { "transactionId", "status": "SUCCESS", "amount" },
  "depositPerPlayer": 50000.00,
  "depositCurrency": "VND",
  "currentPlayerCount": 1,
  "requiredPlayerCount": 4,
  "candidates": [
    { "userId": 10, "currentElo": 1050, "loyaltyTier": "BRONZE" },
    { "userId": 15, "currentElo": 980, "loyaltyTier": "SILVER" }
  ]
}
```

**GET** `/api/bookings/casual/available` - Browse Available Casual Matches
```json
Response: [
  {
    "booking": { "id": 5, "status": "PENDING", "bookingType": "CASUAL" },
    "depositPerPlayer": 50000.00,
    "currentPlayerCount": 2,
    "requiredPlayerCount": 4
  }
]
```

**GET** `/api/bookings/{bookingId}/candidates` - Get Matching Candidates
```json
Response: [
  { "userId": 10, "currentElo": 1050, "loyaltyTier": "BRONZE" },
  { "userId": 15, "currentElo": 980, "loyaltyTier": "SILVER" }
]
```

**POST** `/api/bookings/{bookingId}/join` - Join Casual Match (deposit 25%, Elo check)
```json
Request: { "userId": 10 }
Response: {
  "id": 5, "status": "PENDING|CONFIRMED", "bookingType": "CASUAL",
  "payment": { "transactionId", "status": "SUCCESS", "amount": 50000.00 }
}
Note: Status auto-changes to CONFIRMED when 4 players have paid
```

---

## 🏃 PlayerController (`/api/players`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `PUT` | `/location` | Update player GPS location (from Android app) | ✅ Yes |
| `GET` | `/{userId}/elo-history` | Get Elo history | ✅ Yes |
| `GET` | `/{userId}` | Get player profile | ✅ Yes |

**PUT** `/api/players/location` - Update Player GPS Location
```json
Request: {
  "userId": 5,
  "latitude": 21.0285,
  "longitude": 105.8542
}
Response: {
  "success": true,
  "data": "Location updated successfully"
}
Note: Called by Android app when GPS is enabled or when searching for matches.
Used by matchmaking to filter players within 15km radius of venue.
```

---

## 👷 VenueStaffController (`/api/staff`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/login` | Staff login, returns JWT | ❌ No |
| `POST` | `/` | Create new staff | ✅ Venue Owner |
| `GET` | `/venue/{venueId}` | Get all staff for venue | ✅ Venue Owner |
| `GET` | `/owner/{ownerId}` | Get all staff for owner | ✅ Venue Owner |
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

## 👀 RefereeController (`/api/referee`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `GET` | `/test/generate` | Generate 10 random test questions (2 per category) | ✅ Player |
| `POST` | `/test/submit` | Submit test answers, auto-creates referee request if ≥9/10 | ✅ Player |
| `GET` | `/test/history?userId={id}` | Get test attempt history for a player | ✅ Player |
| `GET` | `/profile?userId={id}` | Get referee profile (trust score, match count, etc.) | ✅ Referee |
| `PUT` | `/{refereeId}/availability?isReady={true/false}` | Update referee's availability status | ✅ Referee |
| `GET` | `/{refereeId}/matches?status={status}` | Get assigned matches for referee (status: UPCOMING or HISTORY) | ✅ Referee |
| `POST` | `/matches/{matchId}/result` | Referee submits match result (score + winning team) | ✅ Referee |
| `GET` | `/{refereeId}/disputes` | Get dispute history for referee | ✅ Referee |
| `POST` | `/disputes` | Player submits dispute against referee's result | ✅ Player |
| `POST` | `/disputes/{disputeId}/evidence` | Referee submits evidence for dispute (within 24h) | ✅ Referee |

### Generate Test
```
GET /api/referee/test/generate

Response: [
  {
    "id": 1,
    "category": "SCORING",
    "questionText": "In a standard Pickleball game, what is the winning score?",
    "optionA": "15 points",
    "optionB": "11 points",
    "optionC": "21 points",
    "optionD": "25 points"
  },
  ... (10 questions total)
]
```

### Submit Test
```
POST /api/referee/test/submit

Request:
{
  "userId": 1,
  "answers": {
    "1": "B",
    "2": "A",
    "3": "C",
    ... (10 answers, questionId -> answer)
  }
}

Response:
{
  "attemptId": 1,
  "userId": 1,
  "score": 9,
  "totalQuestions": 10,
  "passed": true,
  "attemptedAt": "2026-03-08T15:30:00",
  "message": "Congratulations! You passed the referee test. Your request is pending admin approval."
}
```

### Submit Match Result (Referee Only)
```
POST /api/referee/matches/{matchId}/result

Request:
{
  "refereeUserId": 5,
  "teamAScore": 11,
  "teamBScore": 7,
  "winningTeam": "A"
}
```

### Get Referee Matches
```
GET /api/referee/{refereeId}/matches?status=UPCOMING

Response:
[
  {
    "rankedMatchId": 1,
    "matchStatus": "PENDING",
    "booking": {
      "id": 100,
      "courtId": 1,
      "startTime": "2026-03-10T14:00:00",
      "endTime": "2026-03-10T15:00:00",
      "bookingType": "RANKED",
      "status": "PENDING",
      "venueFee": 100000.0,
      "refereeFee": 50000.0
    },
    "totalCost": 150000.0,
    "refereeAssigned": true
  }
]
```

### Submit Dispute (Player)
```
POST /api/referee/disputes

Request:
{
  "rankedMatchId": 1,
  "reportingPlayerId": 2,
  "reason": "Referee reported wrong score",
  "evidence": "[\"https://photo1.jpg\",\"https://video1.mp4\"]"
}
```

### Submit Referee Evidence
```
POST /api/referee/disputes/{disputeId}/evidence

Request:
{
  "refereeUserId": 5,
  "evidenceUrl": "https://drive.google.com/evidence-folder",
  "response": "The score was correct, here is the scorecard photo"
}
```

---

## 👨‍💼 AdminController - Referee & Dispute Endpoints (`/api/admin`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/referee-requests/{requestId}/approve?adminId={id}` | Approve referee registration request | ✅ Admin |
| `POST` | `/referee-requests/{requestId}/reject?adminId={id}` | Reject referee registration request | ✅ Admin |
| `GET` | `/disputes` | Get all disputes | ✅ Admin |
| `GET` | `/disputes/{disputeId}` | Get dispute details | ✅ Admin |
| `POST` | `/disputes/{disputeId}/resolve` | Resolve dispute (UPHOLD or OVERTURN) | ✅ Admin |

### Resolve Dispute (Admin)
```
POST /api/admin/disputes/{disputeId}/resolve

Request:
{
  "adminId": 1,
  "decision": "Referee's evidence confirms the reported score was correct",
  "decisionType": "UPHOLD"
}

decisionType: "UPHOLD" (referee correct) or "OVERTURN" (referee wrong, penalty applied)
```

---

## 💳 WalletController (`/api/wallet`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `GET` | `/` | Get current user's wallet balance | ✅ Yes |
| `GET` | `/transactions` | Get current user's transaction history | ✅ Yes |
| `POST` | `/topup` | Top up the wallet with a specified amount (mock) | ✅ Yes |
| `POST` | `/withdraw` | Withdraw funds from the wallet (mock) | ✅ Yes |

### Example Request/Response

**Top Up:**
```json
POST /api/wallet/topup
Request: 
{ 
  "amount": 100000, 
  "description": "Nạp tiền vào ví" 
}
Response: 
{ "userId": 1, "balance": 150000.0, "updatedAt": "2026-05-04T10:00:00" }
```

**Withdraw:**
```json
POST /api/wallet/withdraw
Request: 
{ 
  "amount": 50000, 
  "description": "Rút tiền về tài khoản ngân hàng" 
}
Response: 
{ "userId": 1, "balance": 100000.0, "updatedAt": "2026-05-04T10:05:00" }
```

---

## 🤝 MatchmakingController (`/api/matchmaking`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/join` | Join the Ranked matchmaking queue | ✅ Yes |
| `POST` | `/leave` | Leave the Ranked matchmaking queue | ✅ Yes |
| `GET` | `/status` | Check the current queue status for a user | ✅ Yes |

### Example Request/Response

**Join Queue:**
```json
POST /api/matchmaking/join
Request: 
{
  "userId": 5,
  "role": "PLAYER",
  "latitude": 21.028511,
  "longitude": 105.804817
}
Response: 
{ "ticketId": 1, "userId": 5, "role": "PLAYER", "status": "WAITING" }
```

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
- `/api/referee/**`
- `/api/players/**`
- `/api/admin/**`

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
