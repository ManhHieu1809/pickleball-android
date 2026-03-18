package com.example.pickleball.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pickleball.ui.screens.booking.BookingScreen
import com.example.pickleball.ui.screens.booking.MyBookingsScreen
import com.example.pickleball.ui.screens.court.FindCourtScreen
import com.example.pickleball.ui.screens.court.VenueDetailScreen
import com.example.pickleball.ui.screens.home.HomeScreen
import com.example.pickleball.ui.screens.match.InviteDuoScreen
import com.example.pickleball.ui.screens.login.LoginScreen
import com.example.pickleball.ui.screens.match.ConfirmJoinMatchScreen
import com.example.pickleball.ui.screens.match.CreateMatchScreen
import com.example.pickleball.ui.screens.match.EstimatedCostScreen
import com.example.pickleball.ui.screens.match.FindMatchScreen
import com.example.pickleball.ui.screens.match.MatchCreatedScreen
import com.example.pickleball.ui.screens.match.MatchDetailsScreen
import com.example.pickleball.ui.screens.match.MatchFoundScreen
import com.example.pickleball.ui.screens.match.MatchJoinedScreen
import com.example.pickleball.ui.screens.match.MatchStartingScreen
import com.example.pickleball.ui.screens.match.PreMatchLobbyScreen
import com.example.pickleball.ui.screens.match.SearchingMatchScreen
import com.example.pickleball.ui.screens.onboarding.OnboardingScreens
import com.example.pickleball.ui.screens.profile.wallet.AddFundsScreen
import com.example.pickleball.ui.screens.profile.wallet.ConfirmWithdrawalScreen
import com.example.pickleball.ui.screens.profile.LeaderboardScreen
import com.example.pickleball.ui.screens.profile.MatchAnalysisScreen
import com.example.pickleball.ui.screens.profile.MatchHistoryScreen
import com.example.pickleball.ui.screens.profile.wallet.MyWalletScreen
import com.example.pickleball.ui.screens.profile.ProfileScreen
import com.example.pickleball.ui.screens.profile.wallet.WithdrawFundsScreen
import com.example.pickleball.ui.screens.profile.wallet.WithdrawalSuccessScreen
import com.example.pickleball.ui.screens.register.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.ONBOARDING
    ) {

        composable(
            route = Routes.ONBOARDING,
            exitTransition = {
                slideOutHorizontally { -it } + fadeOut()
            },
            popEnterTransition = {
                slideInHorizontally { -it } + fadeIn()
            }
        ) {
            OnboardingScreens(
                onGetStarted = {
                    navController.navigate(Routes.LOGIN)
                },
                onSignIn = {
                    navController.navigate(Routes.LOGIN)
                }
            )
        }

        composable(
            route = Routes.LOGIN,
            enterTransition = {
                slideInHorizontally { it } + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally { it } + fadeOut()
            }
        ) {
            LoginScreen(
                onBack = {
                    navController.popBackStack()
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                },
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ONBOARDING) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = Routes.REGISTER,
            enterTransition = {
                slideInHorizontally { it } + fadeIn()
            },
            popExitTransition = {
                slideOutHorizontally { it } + fadeOut()
            }
        ){
            RegisterScreen(
                onBack = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.HOME,
            enterTransition = { fadeIn(animationSpec = tween(durationMillis = 200)) },
            exitTransition = { fadeOut(animationSpec = tween(durationMillis = 200)) },
            popEnterTransition = { fadeIn(animationSpec = tween(durationMillis = 200)) },
            popExitTransition = { fadeOut(animationSpec = tween(durationMillis = 200)) }
        ) {
            HomeScreen(navController = navController)
        }

        composable(
            route = Routes.FIND_COURT,
            enterTransition = { fadeIn(animationSpec = tween(durationMillis = 200)) },
            exitTransition = { fadeOut(animationSpec = tween(durationMillis = 200)) },
            popEnterTransition = { fadeIn(animationSpec = tween(durationMillis = 200)) },
            popExitTransition = { fadeOut(animationSpec = tween(durationMillis = 200)) }
        ) {
            FindCourtScreen(
                navController = navController,
                onVenueClick = { venueId ->
                    navController.navigate("venue_detail/$venueId")
                }
            )
        }

        composable(
            route = Routes.MY_BOOKINGS,
            enterTransition = { fadeIn(animationSpec = tween(durationMillis = 200)) },
            exitTransition = { fadeOut(animationSpec = tween(durationMillis = 200)) },
            popEnterTransition = { fadeIn(animationSpec = tween(durationMillis = 200)) },
            popExitTransition = { fadeOut(animationSpec = tween(durationMillis = 200)) }
        ) {
            MyBookingsScreen(navController = navController)
        }

        composable(
            route = Routes.VENUE_DETAIL,
            arguments = listOf(navArgument("venueId") { type = NavType.StringType }),
            enterTransition = { slideInHorizontally { it } + fadeIn() },
            popExitTransition = { slideOutHorizontally { it } + fadeOut() }
        ) { backStackEntry ->
            val venueId = backStackEntry.arguments?.getString("venueId")
            VenueDetailScreen(
                venueId = venueId,
                onBackClick = { navController.popBackStack() },
                onCourtClick = { courtId ->
                    navController.navigate("booking/$courtId")
                }
            )
        }

        composable(
            route = "booking/{courtId}",
            arguments = listOf(navArgument("courtId") { type = NavType.StringType }),
            enterTransition = { slideInHorizontally { it } + fadeIn() },
            popExitTransition = { slideOutHorizontally { it } + fadeOut() }
        ) { backStackEntry ->
            val courtId = backStackEntry.arguments?.getString("courtId")
            BookingScreen(
                courtId = courtId,
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.PAYMENT_CONFIRMATION,
            arguments = listOf(
                navArgument("courtId") { type = NavType.StringType },
                navArgument("slotId") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType }
            ),
            enterTransition = { slideInHorizontally { it } + fadeIn() },
            popExitTransition = { slideOutHorizontally { it } + fadeOut() }
        ) { backStackEntry ->
            val courtId = backStackEntry.arguments?.getString("courtId")
            val slotId = backStackEntry.arguments?.getString("slotId")
            val date = backStackEntry.arguments?.getString("date")

            com.example.pickleball.ui.screens.payment.PaymentConfirmationScreen(
                courtId = courtId,
                slotId = slotId,
                date = date,
                navController = navController,
                onBackClick = { navController.popBackStack() },
                onConfirmClick = {
                    navController.navigate(Routes.BOOKING_SUCCESS)
                }
            )
        }

        composable(
            route = Routes.BOOKING_SUCCESS,
            enterTransition = { slideInHorizontally { it } + fadeIn() },
            popExitTransition = { slideOutHorizontally { it } + fadeOut() }
        ) {
            com.example.pickleball.ui.screens.booking.BookingSuccessScreen(
                navController = navController
            )
        }

        composable(
            route = "booking_details/{bookingId}",
            arguments = listOf(navArgument("bookingId") { type = NavType.StringType }),
            enterTransition = { slideInHorizontally { it } + fadeIn() },
            popExitTransition = { slideOutHorizontally { it } + fadeOut() }
        ) { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId")
            com.example.pickleball.ui.screens.booking.BookingDetailsScreen(
                bookingId = bookingId,
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.FIND_MATCH,
            enterTransition = { fadeIn(animationSpec = tween(durationMillis = 200)) },
            exitTransition = { fadeOut(animationSpec = tween(durationMillis = 200)) },
            popEnterTransition = { fadeIn(animationSpec = tween(durationMillis = 200)) },
            popExitTransition = { fadeOut(animationSpec = tween(durationMillis = 200)) }
        ) {
            FindMatchScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() },
                onMatchClick = { matchId ->
                    navController.navigate("match_detail/$matchId")
                }
            )
        }

        composable(
            route = Routes.MATCH_DETAIL,
            arguments = listOf(navArgument("matchId") { type = NavType.StringType }),
            enterTransition = { slideInHorizontally { it } + fadeIn() },
            popExitTransition = { slideOutHorizontally { it } + fadeOut() }
        ) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getString("matchId")

            MatchDetailsScreen(
                matchId = matchId,
                onBackClick = { navController.popBackStack() },
                onDepositClick = { navController.navigate("confirm_join/$matchId") }
            )
        }

        composable(
            route = Routes.CONFIRM_JOIN,
            arguments = listOf(navArgument("matchId") { type = NavType.StringType })
        ) {
            ConfirmJoinMatchScreen(
                onBackClick = { navController.popBackStack() },
                onCancelClick = { navController.popBackStack() },
                onConfirmClick = {
                    navController.navigate(Routes.MATCH_JOINED_SUCCESS) {
                        popUpTo(Routes.FIND_MATCH) { inclusive = false }
                    }
                }
            )
        }

        composable(
            route = Routes.MATCH_JOINED_SUCCESS,
            enterTransition = { fadeIn() + slideInVertically { it / 2 } },
            popExitTransition = { fadeOut() }
        ) {
            MatchJoinedScreen(
                onViewMatchesClick = {
                    navController.navigate(Routes.FIND_MATCH) {
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                },
                onHomeClick = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.PROFILE,
            enterTransition = { fadeIn(animationSpec = tween(durationMillis = 200)) },
            exitTransition = { fadeOut(animationSpec = tween(durationMillis = 200)) },
            popEnterTransition = { fadeIn(animationSpec = tween(durationMillis = 200)) },
            popExitTransition = { fadeOut(animationSpec = tween(durationMillis = 200)) }
        ) {
            ProfileScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.LEADERBOARD,
            enterTransition = { slideInHorizontally { it } + fadeIn() },
            popExitTransition = { slideOutHorizontally { it } + fadeOut() }
        ) {
            LeaderboardScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.WALLET,
            enterTransition = { slideInHorizontally { it } + fadeIn() },
            popExitTransition = { slideOutHorizontally { it } + fadeOut() }
        ) {
            MyWalletScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.ADD_FUNDS,
            enterTransition = { slideInVertically { it } + fadeIn() },
            popExitTransition = { slideOutVertically { it } + fadeOut() }
        ) {
            AddFundsScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.WITHDRAW_FUNDS,
            enterTransition = { slideInVertically { it } + fadeIn() },
            popExitTransition = { slideOutVertically { it } + fadeOut() }
        ) {
            WithdrawFundsScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.CONFIRM_WITHDRAWAL,
            enterTransition = { slideInHorizontally { it } + fadeIn() },
            popExitTransition = { slideOutHorizontally { it } + fadeOut() }
        ) {
            ConfirmWithdrawalScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() },
                onConfirmClick = {
                    navController.navigate(Routes.WITHDRAWAL_SUCCESS)
                }
            )
        }

        composable(
            route = Routes.WITHDRAWAL_SUCCESS,
            enterTransition = { slideInHorizontally { it } + fadeIn() },
            popExitTransition = { slideOutHorizontally { it } + fadeOut() }
        ) {
            WithdrawalSuccessScreen(
                navController = navController
            )
        }

        composable(
            route = Routes.MATCH_HISTORY,
            enterTransition = { slideInHorizontally { it } + fadeIn() },
            popExitTransition = { slideOutHorizontally { it } + fadeOut() }
        ) {
            MatchHistoryScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.MATCH_ANALYSIS,
            enterTransition = { slideInHorizontally { it } + fadeIn() },
            popExitTransition = { slideOutHorizontally { it } + fadeOut() }
        ) {
            MatchAnalysisScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.PRE_MATCH_LOBBY,
            enterTransition = { slideInVertically { it } + fadeIn() },
            popExitTransition = { slideOutVertically { it } + fadeOut() }
        ) {
            PreMatchLobbyScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.INVITE_DUO) {
            InviteDuoScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.SEARCHING_MATCH) {
            SearchingMatchScreen(
                navController = navController,
                onCancel = { navController.popBackStack() }
            )
        }

        composable (Routes.MATCH_FOUND){
            MatchFoundScreen(navController = navController)
        }

        composable(Routes.MATCH_STARTING) {
            MatchStartingScreen(navController = navController)
        }

        composable(Routes.CREATE_MATCH) {
            CreateMatchScreen(
                courtId = null,
                slotId = null,
                date = null,
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.CREATE_MATCH_WITH_SLOT,
            arguments = listOf(
                navArgument("courtId") { type = NavType.StringType },
                navArgument("slotId") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType }
            ),
            enterTransition = { slideInHorizontally { it } + fadeIn() },
            popExitTransition = { slideOutHorizontally { it } + fadeOut() }
        ) { backStackEntry ->
            val courtId = backStackEntry.arguments?.getString("courtId")
            val slotId = backStackEntry.arguments?.getString("slotId")
            val date = backStackEntry.arguments?.getString("date")

            CreateMatchScreen(
                courtId = courtId,
                slotId = slotId,
                date = date,
                navController = navController,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.ESTIMATED_COST) {
            EstimatedCostScreen(
                navController = navController,
                onBackClick = { navController.popBackStack() },
                onConfirmClick = {
                    navController.popBackStack(Routes.MATCH_CREATED, inclusive = false)
                }
            )
        }

        composable(Routes.MATCH_CREATED) {
            MatchCreatedScreen(
                navController = navController,
                onViewMyMatches = {

                },
                onShare = { /* Mở share sheet */ },
                onBackToHome = {
                    navController.popBackStack(Routes.HOME, inclusive = false)
                }
            )
        }

    }
}