package com.cnc.tictac.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cnc.tictac.R
import com.cnc.tictac.R.string as content

@Composable
fun SingleLineTextField(
    modifier: Modifier = Modifier,
    label: String = "text field label",
    value: String = "text field value",
    placeholder: String = stringResource(id = R.string.user_name_placeholder),
    onValueChange: (String) -> Unit,
) {
    BasicTextField (
        value = value,
        onValueChange = onValueChange,
        textStyle =  TextStyle(
            fontFamily = MaterialTheme.typography.labelSmall.fontFamily,
            fontSize = MaterialTheme.typography.labelSmall.fontSize,
            fontWeight = MaterialTheme.typography.labelSmall.fontWeight,
            color = MaterialTheme.colorScheme.onPrimary
        ),
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.outlineVariant),
        decorationBox = {innerTextField ->
            
            // CONTAINER: Contains both text field and label
            Column (
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // ELEMENT: Text field label
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )

                // CONTAINER: Text field
                Row(
                    Modifier
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(percent = 100))
                        .border(BorderStroke(1.dp, SolidColor(MaterialTheme.colorScheme.onPrimary)), RoundedCornerShape(percent = 100))
                        .fillMaxWidth().heightIn(44.dp).widthIn(44.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outlineVariant,
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Composable
fun TitleMedium(
    content: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = content,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BodyMedium(
    content: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
fun BodyLarge(
    content: String = "bodyLarge",
    isCenter: Boolean = false,
    rowModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier
) {
    val arrangement = if (isCenter) Arrangement.Center else Arrangement.Start

    Row(
        modifier = rowModifier,
        horizontalArrangement = arrangement
    ) {
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = textModifier
        )
    }
}

@Composable
fun LogoText(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = content.app_display_name),
        style = MaterialTheme.typography.displaySmall,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier
    )
}