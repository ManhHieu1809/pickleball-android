package com.example.pickleball.data.repository

import com.example.pickleball.data.model.RefereeAssignedMatchDTO
import com.example.pickleball.data.model.RefereeDisputeDTO
import com.example.pickleball.data.model.RefereeProfileDTO
import com.example.pickleball.data.model.RefereeQuestionDTO
import com.example.pickleball.data.model.SubmitMatchResultRequest
import com.example.pickleball.data.model.SubmitRefereeEvidenceRequest
import com.example.pickleball.data.model.SubmitTestRequest
import com.example.pickleball.data.model.TestResultResponse
import com.example.pickleball.data.model.RoleRequestDTO
import com.example.pickleball.data.remote.RefereeApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RefereeRepository @Inject constructor(
    private val apiService: RefereeApiService
) {
    suspend fun generateTest(): Result<List<RefereeQuestionDTO>> {
        return try {
            val response = apiService.generateTest()
            if (response.isSuccessful) {
                response.body()?.data?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to generate test: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun submitTest(userId: Long, answers: Map<String, String>): Result<TestResultResponse> {
        return try {
            val request = SubmitTestRequest(userId = userId, answers = answers)
            val response = apiService.submitTest(request)
            if (response.isSuccessful) {
                response.body()?.data?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to submit test: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRefereeProfile(userId: Long): Result<RefereeProfileDTO> {
        return try {
            val response = apiService.getRefereeProfile(userId)
            if (response.isSuccessful) {
                response.body()?.data?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                // Return null if not found (means they are not a referee)
                if (response.code() == 404 || response.code() == 400) {
                     Result.failure(Exception("Not a referee"))
                } else {
                     Result.failure(Exception("Failed to get profile: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTestHistory(userId: Long): Result<List<TestResultResponse>> {
        return try {
            val response = apiService.getTestHistory(userId)
            if (response.isSuccessful) {
                response.body()?.data?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Failed to get history: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun hasPendingRequest(userId: Long): Result<Boolean> {
        return try {
            val response = apiService.getPendingRefereeRequests()
            if (response.isSuccessful) {
                val requests = response.body()?.data ?: emptyList()
                val isPending = requests.any { it.userId == userId && it.requestType == "PLATFORM_REFEREE" }
                Result.success(isPending)
            } else {
                Result.failure(Exception("Failed to check pending request: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateAvailability(userId: Long, isReady: Boolean): Result<Unit> {
        return try {
            val response = apiService.updateAvailability(userId, isReady)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to update availability"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAssignedMatches(userId: Long, status: String): Result<List<RefereeAssignedMatchDTO>> {
        return try {
            val response = apiService.getAssignedMatches(userId, status)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data.orEmpty())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to load matches"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun submitMatchResult(
        matchId: Long,
        refereeUserId: Long,
        teamAScore: Int,
        teamBScore: Int,
        winningTeam: String
    ): Result<Unit> {
        return try {
            val request = SubmitMatchResultRequest(refereeUserId, teamAScore, teamBScore, winningTeam)
            val response = apiService.submitMatchResult(matchId, request)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to submit match result"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDisputes(userId: Long): Result<List<RefereeDisputeDTO>> {
        return try {
            val response = apiService.getDisputes(userId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data.orEmpty())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to load disputes"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun submitDisputeEvidence(
        disputeId: Long,
        refereeUserId: Long,
        evidenceUrl: String,
        responseText: String
    ): Result<Unit> {
        return try {
            val request = SubmitRefereeEvidenceRequest(refereeUserId, evidenceUrl, responseText)
            val response = apiService.submitDisputeEvidence(disputeId, request)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to submit evidence"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
