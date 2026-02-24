package com.example.pickleball.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

object Routes {
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val FIND_COURT = "find_court"
    const val COURT_DETAIL = "court_detail/{courtId}"
    const val BOOKING = "booking"
    const val PAYMENT_CONFIRMATION = "payment_confirmation"
    const val BOOKING_SUCCESS = "booking_success"
    const val MY_BOOKINGS = "my_bookings"
    const val BOOKING_DETAILS = "booking_details"
    const val FIND_MATCH = "find_match"
    const val PROFILE = "profile"
    const val MATCH_DETAIL = "match_detail/{matchId}"
    const val CONFIRM_JOIN = "confirm_join/{matchId}"
    const val MATCH_JOINED_SUCCESS = "match_joined_success"
    const val LEADERBOARD = "leaderboard"
    const val WALLET = "wallet"
    const val ADD_FUNDS = "add_funds"
    const val WITHDRAW_FUNDS = "withdraw_funds"
    const val CONFIRM_WITHDRAWAL = "confirm_withdrawal"
    const val WITHDRAWAL_SUCCESS = "withdrawal_success"
    const val MATCH_HISTORY = "match_history"
    const val MATCH_ANALYSIS = "match_analysis"
    const val PRE_MATCH_LOBBY = "pre_match_lobby"
    const val INVITE_DUO = "invite_duo"
    const val SEARCHING_MATCH = "searching_match"
    const val MATCH_FOUND = "match_found"
    const val MATCH_STARTING = "match_starting"
    const val CREATE_MATCH = "create_match"
    const val ESTIMATED_COST = "estimated_cost"
    const val MATCH_CREATED = "match_created"
}

fun NavController.navigateToTab(route: String) {
    this.navigate(route) {
        popUpTo(this@navigateToTab.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}