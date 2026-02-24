package com.example.pickleball.ui.screens.home.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

val NeonLime = Color(0xFFA3FF00)
val CardBlack = Color(0xFF101010)
val BarPast = Color(0xFFE0E0E0)
val BarFuture = Color(0xFF2C2C2E)
val TextGray = Color(0xFF888888)
val LossColor = Color(0xFF666666)

@Composable
fun RatingCard(
    rating: String,
    weeklyChange: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFF5F8F7)
) {
    var isWeekView by remember { mutableStateOf(true) }
    val density = LocalDensity.current
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(150)
        isVisible = true
    }

    val rotationX by animateFloatAsState(if (isVisible) 0f else 90f, spring(0.6f, Spring.StiffnessLow), label = "RotX")
    val alpha by animateFloatAsState(if (isVisible) 1f else 0f, tween(600), label = "Alpha")

    Box(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .fillMaxWidth()
            .height(320.dp)
            .graphicsLayer {
                this.rotationX = rotationX
                this.alpha = alpha
                this.cameraDistance = 8f * density.density
                this.transformOrigin = androidx.compose.ui.graphics.TransformOrigin.Center
            }
    ) {
        // --- 1. CARD NỀN ---
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(48.dp),
            color = CardBlack,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 36.dp, bottom = 24.dp)
            ) {
                AnimatedContent(
                    targetState = isWeekView,
                    transitionSpec = {
                        if (targetState) (slideInVertically { -it } + fadeIn()) togetherWith (slideOutVertically { it } + fadeOut())
                        else (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
                    }, label = "Header"
                ) { isWeek ->
                    if (isWeek) {
                        MetricDisplay("Weekly Matches", rating, weeklyChange)
                    } else {
                        MetricDisplay("Total Rank Matches", "142", "+8%")
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Chart Content
                AnimatedContent(
                    targetState = isWeekView,
                    transitionSpec = { fadeIn(tween(400)) togetherWith fadeOut(tween(400)) }, label = "Chart"
                ) { isWeek ->
                    if (isWeek) {
                        WeekBarChartAnimated()
                    } else {
                        // BIỂU ĐỒ WIN RATE (CÓ ANIMATION SỐ %)
                        WinRateDashboardAnimated()
                    }
                }
            }
        }

        // --- 2. NÚT TOGGLE ---
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 8.dp, y = (-6).dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.size(96.dp).clip(CircleShape).background(backgroundColor)
            )
            Box(
                modifier = Modifier
                    .size(82.dp)
                    .shadow(10.dp, CircleShape, spotColor = NeonLime)
                    .clip(CircleShape)
                    .background(NeonLime)
                    .clickable { isWeekView = !isWeekView },
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = isWeekView,
                    transitionSpec = {
                        if (targetState) (slideInHorizontally { -it } + fadeIn()) togetherWith (slideOutHorizontally { it } + fadeOut())
                        else (slideInHorizontally { it } + fadeIn()) togetherWith (slideOutHorizontally { -it } + fadeOut())
                    }, label = "BtnTxt"
                ) { isWeek ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(if (isWeek) "7" else "30", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, fontSize = 28.sp, color = Color.Black)
                        Text(if (isWeek) "Week" else "Rank", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, fontSize = 9.sp, color = Color.Black.copy(0.7f))
                    }
                }
            }
        }
    }
}

@Composable
fun MetricDisplay(title: String, value: String, percent: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = TextGray,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            RollingNumberText(
                targetValueString = value,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    fontSize = 44.sp
                ),
                color = NeonLime,
                letterSpacing = (-2).sp
            )

            Spacer(Modifier.width(12.dp))

            Text(buildAnnotatedString {
                withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)) { append(percent) }
                withStyle(SpanStyle(color = TextGray, fontSize = 13.sp)) { append(" / vs last") }
            }, Modifier.padding(bottom = 12.dp))
        }
    }
}

@Composable
fun RollingNumberText(
    targetValueString: String,
    style: androidx.compose.ui.text.TextStyle,
    color: Color,
    letterSpacing: androidx.compose.ui.unit.TextUnit = androidx.compose.ui.unit.TextUnit.Unspecified
) {
    val targetInt = targetValueString.replace(",", "").toIntOrNull() ?: 0

    val animatedValue = remember { Animatable(0f) }

    LaunchedEffect(targetInt) {
        animatedValue.animateTo(
            targetValue = targetInt.toFloat(),
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }

    val displayValue = remember(animatedValue.value) {
        NumberFormat.getNumberInstance(Locale.US).format(animatedValue.value.toInt())
    }

    Text(
        text = displayValue,
        style = style,
        color = color,
        letterSpacing = letterSpacing
    )
}

@Composable
fun WinRateDashboardAnimated() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(180.dp),
            contentAlignment = Alignment.Center
        ) {
            WinRateCanvasHTMLAnimated()
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-2).dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            LegendItem(NeonLime, "Wins")
            Spacer(modifier = Modifier.width(20.dp))
            LegendItem(LossColor, "Losses")
        }
    }
}


@Composable
fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = text, style = MaterialTheme.typography.labelMedium, color = TextGray, fontSize = 11.sp)
    }
}

@Composable
fun WinRateCanvasHTMLAnimated() {
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animationProgress.animateTo(1f, tween(1200, easing = LinearOutSlowInEasing))
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val r = size.minDimension * 0.32f
        val strokeWidth = 14.dp.toPx()
        val currentProgress = animationProgress.value

        drawCircle(
            color = LossColor.copy(alpha = 0.3f),
            radius = r,
            center = center,
            style = Stroke(width = strokeWidth)
        )

        drawArc(
            color = LossColor,
            startAngle = -90f + (360 * 0.72f),
            sweepAngle = 360 * 0.28f * currentProgress,
            useCenter = false,
            topLeft = Offset(center.x - r, center.y - r),
            size = androidx.compose.ui.geometry.Size(r * 2, r * 2),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        drawArc(
            color = NeonLime,
            startAngle = -90f,
            sweepAngle = 360 * 0.72f * currentProgress,
            useCenter = false,
            topLeft = Offset(center.x - r, center.y - r),
            size = androidx.compose.ui.geometry.Size(r * 2, r * 2),
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        drawContext.canvas.nativeCanvas.apply {
            val paint = Paint().apply {
                color = TextGray.toArgb()
                textSize = 10.sp.toPx()
                textAlign = Paint.Align.CENTER
                typeface = Typeface.DEFAULT_BOLD
                letterSpacing = 0.1f
            }
            drawText("WIN RATE", center.x, center.y + 4.dp.toPx(), paint)
        }

        val scale = size.minDimension / 100f

        val p1 = center + Offset(28f * scale, -28f * scale)
        val p2 = center + Offset(45f * scale, -45f * scale)
        val p3 = center + Offset(65f * scale, -45f * scale)

        if (currentProgress > 0.2f) {
            val lineAlpha = ((currentProgress - 0.2f) / 0.8f).coerceIn(0f, 1f)
            val lineColor = NeonLime.copy(alpha = lineAlpha)

            drawLine(lineColor, p1, p2, strokeWidth = 2.dp.toPx())
            drawLine(lineColor, p2, p3, strokeWidth = 2.dp.toPx())
            drawCircle(lineColor, radius = 3.dp.toPx(), center = p1)

            val winPercent = (72 * currentProgress).toInt()

            drawContext.canvas.nativeCanvas.apply {
                val paint = Paint().apply {
                    color = NeonLime.toArgb()
                    textSize = 14.sp.toPx()
                    textAlign = Paint.Align.LEFT
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    alpha = (lineAlpha * 255).toInt()
                }
                drawText("$winPercent%", p3.x + 4.dp.toPx(), p3.y + 4.dp.toPx(), paint)
            }
        }


        val l1 = center + Offset(-28f * scale, -28f * scale)
        val l2 = center + Offset(-45f * scale, -45f * scale)
        val l3 = center + Offset(-60f * scale, -45f * scale)

        if (currentProgress > 0.2f) {
            val lineAlpha = ((currentProgress - 0.2f) / 0.8f).coerceIn(0f, 1f)
            val lineColor = LossColor.copy(alpha = lineAlpha)

            drawLine(lineColor, l1, l2, strokeWidth = 2.dp.toPx())
            drawLine(lineColor, l2, l3, strokeWidth = 2.dp.toPx())
            drawCircle(lineColor, radius = 3.dp.toPx(), center = l1)

            val lossPercent = (28 * currentProgress).toInt()

            drawContext.canvas.nativeCanvas.apply {
                val paint = Paint().apply {
                    color = LossColor.toArgb()
                    textSize = 14.sp.toPx()
                    textAlign = Paint.Align.RIGHT
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    alpha = (lineAlpha * 255).toInt()
                }
                drawText("$lossPercent%", l3.x - 4.dp.toPx(), l3.y + 4.dp.toPx(), paint)
            }
        }
    }
}

@Composable
fun WeekBarChartAnimated() {
    val bars = listOf(0.35f, 0.6f, 0.2f, 1.0f, 0.45f, 0.45f, 0.45f)
    val days = listOf("S", "M", "T", "Today", "T", "F", "S")
    val activeIndex = 3
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessVeryLow))
    }

    Row(
        modifier = Modifier.fillMaxWidth().height(160.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        bars.forEachIndexed { i, h ->
            val isActive = i == activeIndex
            val color = if (isActive) NeonLime else if (i < activeIndex) BarPast else BarFuture

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.BottomCenter) {
                    Box(Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(h * progress.value)
                        .clip(RoundedCornerShape(6.dp))
                        .background(color)
                        .then(if (isActive) Modifier.shadow(20.dp, spotColor = NeonLime, ambientColor = NeonLime) else Modifier))
                }
                Spacer(Modifier.height(16.dp))
                Box(modifier = Modifier.height(18.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = days[i],
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isActive) Color.White else TextGray.copy(0.6f),
                        maxLines = 1
                    )
                }
            }
        }
    }
}