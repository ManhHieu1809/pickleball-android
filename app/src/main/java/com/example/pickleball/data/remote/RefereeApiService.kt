package com.example.pickleball.data.remote

import com.example.pickleball.data.model.ApiResponse
import com.example.pickleball.data.model.RefereeAssignedMatchDTO
import com.example.pickleball.data.model.RefereeDisputeDTO
import com.example.pickleball.data.model.RefereeProfileDTO
import com.example.pickleball.data.model.RefereeQuestionDTO
import com.example.pickleball.data.model.SubmitTestRequest
import com.example.pickleball.data.model.SubmitMatchResultRequest
import com.example.pickleball.data.model.SubmitRefereeEvidenceRequest
import com.example.pickleball.data.model.TestResultResponse
import com.example.pickleball.data.model.RoleRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RefereeApiService {
    
    @GET("referee/test/generate")
    suspend fun generateTest(): Response<ApiResponse<List<RefereeQuestionDTO>>>

    @POST("referee/test/submit")
    suspend fun submitTest(@Body request: SubmitTestRequest): Response<ApiResponse<TestResultResponse>>

    @GET("referee/profile")
    suspend fun getRefereeProfile(@Query("userId") userId: Long): Response<ApiResponse<RefereeProfileDTO>>

    @GET("referee/test/history")
    suspend fun getTestHistory(@Query("userId") userId: Long): Response<ApiResponse<List<TestResultResponse>>>
    
    @GET("admin/referee-requests/pending")
    suspend fun getPendingRefereeRequests(): Response<ApiResponse<List<RoleRequestDTO>>>

    @PUT("referee/{refereeId}/availability")
    suspend fun updateAvailability(
        @Path("refereeId") refereeId: Long,
        @Query("isReady") isReady: Boolean
    ): Response<ApiResponse<String>>

    @GET("referee/{refereeId}/matches")
    suspend fun getAssignedMatches(
        @Path("refereeId") refereeId: Long,
        @Query("status") status: String
    ): Response<ApiResponse<List<RefereeAssignedMatchDTO>>>

    @POST("referee/matches/{matchId}/result")
    suspend fun submitMatchResult(
        @Path("matchId") matchId: Long,
        @Body request: SubmitMatchResultRequest
    ): Response<ApiResponse<Void>>

    @GET("referee/{refereeId}/disputes")
    suspend fun getDisputes(
        @Path("refereeId") refereeId: Long
    ): Response<ApiResponse<List<RefereeDisputeDTO>>>

    @POST("referee/disputes/{disputeId}/evidence")
    suspend fun submitDisputeEvidence(
        @Path("disputeId") disputeId: Long,
        @Body request: SubmitRefereeEvidenceRequest
    ): Response<ApiResponse<Void>>
}
