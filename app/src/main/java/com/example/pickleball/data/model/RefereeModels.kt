package com.example.pickleball.data.model

import com.squareup.moshi.Json

data class RefereeQuestionDTO(
    val id: Long,
    val category: String?,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String
)

data class SubmitTestRequest(
    val userId: Long,
    val answers: Map<String, String> // e.g. "1": "A", "2": "C"
)

data class TestResultResponse(
    val attemptId: Long,
    val userId: Long,
    val score: Int,
    val totalQuestions: Int,
    val passed: Boolean,
    val attemptedAt: String?,
    val message: String?
)

data class RefereeProfileDTO(
    val userId: Long,
    val trustScore: Int?,
    val totalMatchesRefereed: Int?,
    val pendingDisputes: Int?,
    val isActive: Boolean
)

data class RoleRequestDTO(
    val id: Long,
    val userId: Long,
    val requestType: String,
    val status: String
)

data class RefereeAssignedMatchDTO(
    val rankedMatchId: Long,
    val matchStatus: String?,
    val booking: Booking?,
    val totalCost: Double?,
    val refereeAssigned: Boolean?,
    val playerCandidates: List<PlayerMatchDTO>? = null
)

data class RefereeDisputeDTO(
    @Json(name = "id") val id: Long?,
    val rankedMatchId: Long?,
    val status: String?,
    val reason: String?,
    val evidenceUrl: String?,
    val response: String?,
    val createdAt: String?
)

data class SubmitRefereeEvidenceRequest(
    val refereeUserId: Long,
    val evidenceUrl: String,
    val response: String
)

