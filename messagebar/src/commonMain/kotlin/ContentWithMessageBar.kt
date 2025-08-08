import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.AlertTriangle
import compose.icons.feathericons.Check

@Composable
fun rememberMessageBarState(): MessageBarState {
    return remember { MessageBarState() }
}

enum class MessageBarPosition {
    TOP,
    BOTTOM
}

/**
 * Composable function used to wrap the content around which you want to display a message bar.
 * @param messageBarState - Used to control the message bar itself.
 * @param position - Configure where you want to position the Message Bar.
 * [MessageBarPosition.TOP] and [MessageBarPosition.BOTTOM] as available.
 * @param visibilityDuration - How long the Message Bar should stay visible. A default value is 3 seconds.
 * @param showCopyButton - Whether to show a copy button, on a message bar of an Error type.
 * @param showCopyButtonOnSuccess - Whether to show a copy button, on a message bar of a Success type.
 * @param copyButtonFontSize - The font size of the of the copy button text.
 * @param copyButtonFontStyle - [FontStyle] of the copy button text.
 * @param copyButtonFontWeight - [FontWeight] of the copy button text.
 * @param copyButtonFontFamily - [FontFamily] of the copy button text.
 * @param onMessageCopied - This lambda is used to display a custom info message, when a user clicks
 * the copy button and copy an error message to the clipboard. Usually a Toast message like 'Copied' is fine.
 * @param successIcon - Customize the [ImageVector] icon of a success message.
 * @param errorIcon - Customize the [ImageVector] icon of an error message.
 * @param iconSize - Customize the size of the icon.
 * @param errorMaxLines - In how many lines of text you want to display an error message.
 * @param successMaxLines - In how many lines of text you want to display a success message.
 * @param fontSize - The font size of the of the Success/Error text.
 * @param fontStyle - [FontStyle] of the Success/Error text.
 * @param fontWeight - [FontWeight] of the Success/Error text.
 * @param fontFamily - [FontFamily] of the Success/Error text.
 * @param contentBackgroundColor - The background color on top of which the [content] lambda will be placed.
 * @param successContainerColor - Container color of a success message bar.
 * @param successContentColor - Text color of the success message bar.
 * @param errorContainerColor - Container color of an error message bar.
 * @param errorContentColor - Text color of an error message bar.
 * @param enterAnimation - Enter animation of the message bar.
 * @param exitAnimation - Exit animation of the message bar.
 * @param verticalPadding - Vertical padding of the message bar.
 * @param horizontalPadding - Horizontal padding of the message bar.
 * @param spacing - Spacing between an icon and the text.
 * @param content - Content composable around which you are displaying the message bar.
 * */
@Composable
fun ContentWithMessageBar(
    modifier: Modifier = Modifier,
    messageBarState: MessageBarState,
    position: MessageBarPosition = MessageBarPosition.TOP,
    visibilityDuration: Long = 3000L,
    showCopyButton: Boolean = true,
    showCopyButtonOnSuccess: Boolean = false,
    copyButtonFontSize: TextUnit = MaterialTheme.typography.labelMedium.fontSize,
    copyButtonFontStyle: FontStyle = FontStyle.Normal,
    copyButtonFontWeight: FontWeight = FontWeight.Normal,
    copyButtonFontFamily: FontFamily? = null,
    onMessageCopied: (() -> Unit)? = null,
    successIcon: ImageVector = FeatherIcons.Check,
    errorIcon: ImageVector = FeatherIcons.AlertTriangle,
    iconSize: Dp = 20.dp,
    errorMaxLines: Int = 1,
    successMaxLines: Int = 1,
    fontSize: TextUnit = MaterialTheme.typography.labelLarge.fontSize,
    fontStyle: FontStyle = FontStyle.Normal,
    fontWeight: FontWeight = FontWeight.Normal,
    fontFamily: FontFamily? = null,
    contentBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    successContainerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    successContentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    errorContainerColor: Color = MaterialTheme.colorScheme.errorContainer,
    errorContentColor: Color = MaterialTheme.colorScheme.onErrorContainer,
    enterAnimation: EnterTransition = expandVertically(
        animationSpec = tween(durationMillis = 300),
        expandFrom = if (position == MessageBarPosition.TOP)
            Alignment.Top else Alignment.Bottom
    ),
    exitAnimation: ExitTransition = shrinkVertically(
        animationSpec = tween(durationMillis = 300),
        shrinkTowards = if (position == MessageBarPosition.TOP)
            Alignment.Top else Alignment.Bottom
    ),
    verticalPadding: Dp = 12.dp,
    horizontalPadding: Dp = 12.dp,
    spacing: Dp = 12.dp,
    content: @Composable () -> Unit,
) {
    LaunchedEffect(key1 = messageBarState) {
        messageBarState.reset()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = contentBackgroundColor)
    ) {
        content()
        MessageBarComponent(
            messageBarState = messageBarState,
            position = position,
            visibilityDuration = visibilityDuration,
            successIcon = successIcon,
            errorIcon = errorIcon,
            iconSize = iconSize,
            showCopyButton = showCopyButton,
            showCopyButtonOnSuccess = showCopyButtonOnSuccess,
            copyButtonFontSize = copyButtonFontSize,
            copyButtonFontStyle = copyButtonFontStyle,
            copyButtonFontWeight = copyButtonFontWeight,
            copyButtonFontFamily = copyButtonFontFamily,
            errorMaxLines = errorMaxLines,
            successMaxLines = successMaxLines,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            successContainerColor = successContainerColor,
            successContentColor = successContentColor,
            errorContainerColor = errorContainerColor,
            errorContentColor = errorContentColor,
            enterAnimation = enterAnimation,
            exitAnimation = exitAnimation,
            verticalPadding = verticalPadding,
            horizontalPadding = horizontalPadding,
            spacing = spacing,
            onMessageCopied = onMessageCopied
        )
    }
}

@Composable
internal fun MessageBarComponent(
    messageBarState: MessageBarState,
    position: MessageBarPosition,
    visibilityDuration: Long,
    successIcon: ImageVector,
    errorIcon: ImageVector,
    iconSize: Dp,
    errorMaxLines: Int,
    successMaxLines: Int,
    fontSize: TextUnit,
    fontStyle: FontStyle,
    fontWeight: FontWeight,
    fontFamily: FontFamily?,
    successContainerColor: Color,
    successContentColor: Color,
    errorContainerColor: Color,
    errorContentColor: Color,
    showCopyButton: Boolean,
    showCopyButtonOnSuccess: Boolean,
    copyButtonFontSize: TextUnit,
    copyButtonFontStyle: FontStyle,
    copyButtonFontWeight: FontWeight,
    copyButtonFontFamily: FontFamily?,
    enterAnimation: EnterTransition,
    exitAnimation: ExitTransition,
    verticalPadding: Dp,
    horizontalPadding: Dp,
    spacing: Dp,
    onMessageCopied: (() -> Unit)? = null,
) {
    val scope = rememberCoroutineScope()
    var showMessageBar by remember { mutableStateOf(false) }

    // Retrieve both error message and exception message
    val errorMessage = messageBarState.errorMessage ?: messageBarState.errorException?.message
    val successMessage = messageBarState.success

    val timerManager = remember { TimerManager() }

    DisposableEffect(key1 = messageBarState.updated) {
        showMessageBar = true
        timerManager.scheduleTimer(
            scope = scope,
            visibilityDuration = visibilityDuration,
            onTimerTriggered = {
                showMessageBar = false
            }
        )
        onDispose { timerManager.cancelTimer() }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = if (position == MessageBarPosition.TOP)
            Arrangement.Top else Arrangement.Bottom
    ) {
        AnimatedVisibility(
            visible = (errorMessage != null || successMessage != null) && showMessageBar,
            enter = enterAnimation,
            exit = exitAnimation
        ) {
            MessageBar(
                successMessage = successMessage,
                errorMessage = errorMessage,
                successIcon = successIcon,
                errorIcon = errorIcon,
                iconSize = iconSize,
                errorMaxLines = errorMaxLines,
                successMaxLines = successMaxLines,
                fontSize = fontSize,
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                successContainerColor = successContainerColor,
                successContentColor = successContentColor,
                errorContainerColor = errorContainerColor,
                errorContentColor = errorContentColor,
                showCopyButton = showCopyButton,
                showCopyButtonOnSuccess = showCopyButtonOnSuccess,
                copyButtonFontSize = copyButtonFontSize,
                copyButtonFontStyle = copyButtonFontStyle,
                copyButtonFontWeight = copyButtonFontWeight,
                copyButtonFontFamily = copyButtonFontFamily,
                verticalPadding = verticalPadding,
                horizontalPadding = horizontalPadding,
                spacing = spacing,
                onMessageCopied = onMessageCopied
            )
        }
    }
}

@Composable
internal fun MessageBar(
    successMessage: String?,
    errorMessage: String?,
    successIcon: ImageVector,
    errorIcon: ImageVector,
    iconSize: Dp,
    errorMaxLines: Int,
    successMaxLines: Int,
    fontSize: TextUnit,
    fontStyle: FontStyle,
    fontWeight: FontWeight,
    fontFamily: FontFamily?,
    successContainerColor: Color,
    successContentColor: Color,
    errorContainerColor: Color,
    errorContentColor: Color,
    showCopyButton: Boolean,
    showCopyButtonOnSuccess: Boolean,
    copyButtonFontSize: TextUnit,
    copyButtonFontStyle: FontStyle,
    copyButtonFontWeight: FontWeight,
    copyButtonFontFamily: FontFamily?,
    verticalPadding: Dp,
    horizontalPadding: Dp,
    spacing: Dp,
    onMessageCopied: (() -> Unit)? = null,
) {
    val clipboardManager = LocalClipboardManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (errorMessage != null) errorContainerColor
                else successContainerColor
            )
            .padding(vertical = verticalPadding)
            .padding(horizontal = horizontalPadding)
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(4f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(iconSize),
                imageVector =
                    if (errorMessage != null) errorIcon
                    else successIcon,
                contentDescription = "Message Bar Icon",
                tint = if (errorMessage != null) errorContentColor
                else successContentColor
            )
            Spacer(modifier = Modifier.width(spacing))
            Text(
                text = successMessage ?: (errorMessage ?: "Unknown"),
                color = if (errorMessage != null) errorContentColor
                else successContentColor,
                fontSize = fontSize,
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                overflow = TextOverflow.Ellipsis,
                maxLines = if (errorMessage != null) errorMaxLines else successMaxLines
            )
        }
        if ((errorMessage != null && showCopyButton) || (successMessage != null && showCopyButtonOnSuccess)) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {
                        clipboardManager.setText(
                            AnnotatedString(text = errorMessage ?: successMessage ?: "Unknown")
                        )
                        onMessageCopied?.invoke()
                    },
                    contentPadding = PaddingValues(vertical = 0.dp)
                ) {
                    Text(
                        text = "Copy",
                        color = if (errorMessage != null && showCopyButton) errorContentColor
                        else successContentColor,
                        fontSize = copyButtonFontSize,
                        fontStyle = copyButtonFontStyle,
                        fontWeight = copyButtonFontWeight,
                        fontFamily = copyButtonFontFamily,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}