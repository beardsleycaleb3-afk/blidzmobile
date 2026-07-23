package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsFootball
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppTab
import com.example.ui.GameViewModel
import com.example.ui.components.GameWebView
import com.example.ui.screens.LockerRoomScreen
import com.example.ui.screens.TrophiesScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MascotRunnerApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MascotRunnerApp(viewModel: GameViewModel) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val equipment by viewModel.userEquipment.collectAsStateWithLifecycle()
    val allRuns by viewModel.allRuns.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF0F172A),
                contentColor = Color.White
            ) {
                NavigationBarItem(
                    selected = currentTab == AppTab.RUNNER,
                    onClick = { viewModel.selectTab(AppTab.RUNNER) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.SportsFootball,
                            contentDescription = "Runner"
                        )
                    },
                    label = {
                        Text(
                            text = "3D RUNNER",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF0F172A),
                        selectedTextColor = Color(0xFFFACC15),
                        indicatorColor = Color(0xFFFACC15),
                        unselectedIconColor = Color(0xFF64748B),
                        unselectedTextColor = Color(0xFF64748B)
                    ),
                    modifier = Modifier.testTag("nav_runner")
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.LOCKER_ROOM,
                    onClick = { viewModel.selectTab(AppTab.LOCKER_ROOM) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Locker Room"
                        )
                    },
                    label = {
                        Text(
                            text = "LOCKER ROOM",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF0F172A),
                        selectedTextColor = Color(0xFFFACC15),
                        indicatorColor = Color(0xFFFACC15),
                        unselectedIconColor = Color(0xFF64748B),
                        unselectedTextColor = Color(0xFF64748B)
                    ),
                    modifier = Modifier.testTag("nav_locker")
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.CAREER_TROPHIES,
                    onClick = { viewModel.selectTab(AppTab.CAREER_TROPHIES) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Trophies"
                        )
                    },
                    label = {
                        Text(
                            text = "TROPHIES",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF0F172A),
                        selectedTextColor = Color(0xFFFACC15),
                        indicatorColor = Color(0xFFFACC15),
                        unselectedIconColor = Color(0xFF64748B),
                        unselectedTextColor = Color(0xFF64748B)
                    ),
                    modifier = Modifier.testTag("nav_trophies")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF0F172A))
        ) {
            when (currentTab) {
                AppTab.RUNNER -> {
                    GameWebView(
                        equipment = equipment,
                        onRunCompleted = { yards, xp, coins, multiplier, isFlawless ->
                            viewModel.onGameRunFinished(yards, xp, coins, multiplier, isFlawless)
                        }
                    )
                }
                AppTab.LOCKER_ROOM -> {
                    LockerRoomScreen(
                        equipment = equipment,
                        onUpgradeGear = { gearId ->
                            viewModel.upgradeGear(gearId)
                        }
                    )
                }
                AppTab.CAREER_TROPHIES -> {
                    TrophiesScreen(
                        equipment = equipment,
                        allRuns = allRuns
                    )
                }
            }
        }
    }
}
