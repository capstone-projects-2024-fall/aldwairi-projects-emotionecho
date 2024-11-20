package com.temple.aldwairi_projects_emotionecho.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.temple.aldwairi_projects_emotionecho.ui.navigation.EmotionEchoAppRouter
import com.temple.aldwairi_projects_emotionecho.ui.navigation.Screen

/**
 * This method uses buildAnnotatedString to create a Text Composable with hyperlinked text
 * and a custom modifier which listens for tap input
 *
 * Designed specifically to show a line of text with a hyperlink at the very end
 *
 * @param screen The screen you want this custom Composable to navigate to
 * @param startString the regular string that comes before the hyperlink
 * @param aString the string that will contain the hyperlink
 * @param aStringTag the tag
 * @param aStringAnnotation the description for where this annotation leads to
 */
@Composable
fun CustomClickableText(screen: Screen, startString:String, aString:String, aStringTag: String, aStringAnnotation: String){
    val annotatedText = getAnnotatedString(startString, aString, aStringTag, aStringAnnotation)
    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    Text(
        text = annotatedText,
        style = MaterialTheme.typography.bodyMedium,
        onTextLayout = { result ->
            layoutResult = result
        },
        modifier = Modifier
            .pointerInput(Unit){
                detectTapGestures { offset ->
                    layoutResult?.let { layoutResult ->
                        val position = layoutResult.getOffsetForPosition(offset)
                        annotatedText.getStringAnnotations(
                            start = position,
                            end = position
                        ).firstOrNull{annotation ->
                            annotation.tag.startsWith(aStringTag)
                        }?.let { EmotionEchoAppRouter.navigateTo(screen) }
                    }
                }
            }
    )
}

fun getAnnotatedString(startString:String, aString:String, aStringTag: String, aStringAnnotation: String): AnnotatedString {
    return buildAnnotatedString {
        append(startString)
        pushStringAnnotation(
            tag = aStringTag,
            annotation = aStringAnnotation
        )
        withStyle(
            style = SpanStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            )
        ){
            append(aString)
        }
        pop()
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewCustomClickableText(){
    CustomClickableText(
        screen = Screen.SingupScreen,
        startString = "Text with no hyperlink",
        aString = " Hyperlink",
        aStringTag = "tag",
        aStringAnnotation = "does nothing"
    )
}