package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsFootball
import androidx.compose.material.icons.filled.SportsMotorsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserEquipment

data class GearInfo(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val level: Int,
    val description: String,
    val effectTag: String
)

@Composable
fun LockerRoomScreen(
    equipment: UserEquipment,
    onUpgradeGear: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val gearList = listOf(
        GearInfo(
            id = "HELMET",
            title = "Helmet & Visor",
            icon = Icons.Default.SportsMotorsports,
            level = equipment.helmetLevel,
            description = "Controls target acquisition. Grants Bullet Time (0.5x speed) during shootout targets.",
            effectTag = "Bullet Time Slowdown"
        ),
        GearInfo(
            id = "PADS",
            title = "Shoulder Pads",
            icon = Icons.Default.Shield,
            level = equipment.padsLevel,
            description = "Grants Trucking State aura (plows straight through obstacles on double-tap).",
            effectTag = "Obstacle Trucking Aura"
        ),
        GearInfo(
            id = "CLEATS",
            title = "Pro Turf Cleats",
            icon = Icons.Default.FitnessCenter,
            level = equipment.cleatsLevel,
            description = "Increases lane-switch reaction speed and grants immunity to mud pit hazards.",
            effectTag = "+30% Reaction & Mud Immunity"
        ),
        GearInfo(
            id = "GLOVES",
            title = "Grip Gloves & Wristbands",
            icon = Icons.Default.ElectricBolt,
            level = equipment.glovesLevel,
            description = "Expands magnetic pickup radius for Gold Footballs across adjacent lanes.",
            effectTag = "Magnetic Pickup Field"
        ),
        GearInfo(
            id = "BALL",
            title = "Equipped Match Football",
            icon = Icons.Default.SportsFootball,
            level = equipment.ballLevel,
            description = "Boosts overall score multiplier during Touchdown Sprints (+2x to +5x).",
            effectTag = "Score Multiplier Boost"
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
                )
            )
            .padding(16.dp)
    ) {
        // Top Header Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "LOCKER ROOM",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "EQUIPMENT & STAT BOOSTS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color(0xFF334155), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsFootball,
                        contentDescription = "Gold Footballs",
                        tint = Color(0xFFFACC15),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${equipment.totalGoldFootballs}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFACC15)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // List of Equipment Items
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(gearList.size) { index ->
                val gear = gearList[index]
                GearCard(
                    gear = gear,
                    canAfford = equipment.totalGoldFootballs >= 25,
                    onUpgrade = { onUpgradeGear(gear.id) }
                )
            }
        }
    }
}

@Composable
private fun GearCard(
    gear: GearInfo,
    canAfford: Boolean,
    onUpgrade: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E293B).copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0284C7).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = gear.icon,
                            contentDescription = gear.title,
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = gear.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(3) { idx ->
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (idx < gear.level) Color(0xFFFACC15) else Color(0xFF475569),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "LEVEL ${gear.level}/3",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFFACC15)
                            )
                        }
                    }
                }

                if (gear.level < 3) {
                    Button(
                        onClick = onUpgrade,
                        enabled = canAfford,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF59E0B),
                            contentColor = Color(0xFF0F172A),
                            disabledContainerColor = Color(0xFF334155),
                            disabledContentColor = Color(0xFF64748B)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "UPGRADE\n25 🏈",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            lineHeight = 12.sp
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF10B981).copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "MAXED OUT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF10B981)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = gear.description,
                fontSize = 12.sp,
                color = Color(0xFFCBD5E1),
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .background(Color(0xFF0284C7).copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "EFFECT: ${gear.effectTag}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF38BDF8)
                )
            }
        }
    }
}
