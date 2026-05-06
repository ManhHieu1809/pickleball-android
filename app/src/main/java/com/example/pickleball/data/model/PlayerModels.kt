package com.example.pickleball.data.model

data class LeaderboardEntryDTO(
    val rank: Int?,
    val playerId: Long,
    val fullName: String?,
    val currentElo: Int?,
    val avatarUrl: String?,
    val loyaltyTier: String?,
    val totalMatches: Int? = null,
    val winRate: Double? = null
)

data class LeaderboardPageDTO(
    val content: List<LeaderboardEntryDTO>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)

data class EloHistoryRecord(
    val id: Long,
    val rankedMatchId: Long?,
    val seasonId: Long?,
    val eloBefore: Int,
    val eloAfter: Int,
    val eloChange: Int,
    val reason: String? = null,
    val createdAt: String?
)
