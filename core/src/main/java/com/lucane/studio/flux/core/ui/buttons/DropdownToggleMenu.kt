package com.lucane.studio.flux.core.ui.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.lucane.studio.flux.core.R
import com.lucane.studio.flux.core.theme.AsAColors
import com.lucane.studio.flux.core.theme.AsAFont
import com.lucane.studio.flux.core.theme.AsAHazeStyles
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect

/**
 * A single toggle action in the dropdown.
 *
 * @param icon      Icon displayed on the left.
 * @param label     Action label.
 * @param isActive  Whether the toggle is currently enabled (shows checkmark).
 * @param onToggle  Called when the row is tapped.
 */
data class DropdownToggleAction(
    val icon: Painter,
    val label: String,
    val isActive: Boolean,
    val onToggle: () -> Unit,
)

/**
 * Popup dropdown menu where each item is a toggle (tap to enable/disable).
 * Active items display a filled circle checkmark on the right.
 *
 * Follows AsAFlux design system: AsAColors palette, Inter font, glassmorphism border.
 *
 * @param expanded    Whether the dropdown is visible.
 * @param onDismiss   Called when the user taps outside.
 * @param actions     List of [DropdownToggleAction] to display.
 * @param offset      Pixel offset for positioning the popup.
 */
@Composable
fun DropdownToggleMenu(
    hazeState: HazeState,
    expanded: Boolean,
    onDismiss: () -> Unit,
    actions: List<DropdownToggleAction>,
    offset: IntOffset = IntOffset(x = 0, y = 0),
) {
    if (!expanded) return

    Popup(
        offset = offset,
        alignment = Alignment.TopEnd,
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true),
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .hazeEffect(state = hazeState, style = AsAHazeStyles.dropdownBlur())
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp),
                )
                .width(230.dp)
        ) {
            Column {
                actions.forEachIndexed { index, action ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                action.onToggle()
                                // Do NOT dismiss — toggle stays open for multiple changes
                            }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        // ── Icon ──────────────────────────────────────────
                        Icon(
                            modifier = Modifier.size(18.dp),
                            painter = action.icon,
                            contentDescription = null,
                            tint = if (action.isActive) AsAColors.purpleNeon
                            else AsAColors.purpleLightGray,
                        )

                        // ── Label ─────────────────────────────────────────
                        Text(
                            modifier = Modifier.weight(1f),
                            text = action.label,
                            style = TextStyle(
                                fontFamily = AsAFont.semiBold,
                                fontSize = 13.sp,
                                color = if (action.isActive) AsAColors.black
                                else AsAColors.purpleGray,
                            ),
                        )

                        // ── Toggle indicator ──────────────────────────────
                        if (action.isActive) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(AsAColors.purpleNeon),
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = 1.5.dp,
                                        color = AsAColors.purpleLightGray,
                                        shape = CircleShape,
                                    ),
                            )
                        }
                    }

                    // ── Divider ───────────────────────────────────────────
                    if (index < actions.size - 1) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(1.dp)
                                .background(AsAColors.purpleWhite.copy(alpha = 0.5f)),
                        )
                    }
                }
            }
        }
    }
}