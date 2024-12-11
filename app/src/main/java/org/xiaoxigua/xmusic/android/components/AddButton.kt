package org.xiaoxigua.xmusic.android.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import org.xiaoxigua.xmusic.android.ui.theme.ContainerColor
import org.xiaoxigua.xmusic.android.ui.theme.DisabledLightGray
import org.xiaoxigua.xmusic.android.ui.theme.Purple

@Composable
fun AddButton(
    content: @Composable ((MutableState<Boolean>) -> Unit)? = null,
    onClickable: (() -> Unit)? = null
) {
    val openDialog = remember { mutableStateOf(false) }

    TextButton(
        {
            openDialog.value = true
            onClickable?.invoke()
        }, colors = ButtonColors(
            containerColor = Color.Transparent,
            contentColor = Purple,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = DisabledLightGray
        )
    ) {
        Text("Add", color = Purple)
    }

    if (openDialog.value)
        content?.invoke(openDialog)

}

@Composable
fun AddButtonAlertDialog(
    alertDialogTitle: String,
    confirmButton: String,
    onConfirm: (String, String) -> Unit,
    onDismiss: () -> Unit,
    titleInit: String? = null,
    descriptionInit: String? = null
) {
    var title by remember { mutableStateOf(titleInit ?: "") }
    var description by remember { mutableStateOf(descriptionInit ?: "") }

    AlertDialog(
        title = {
            Text(alertDialogTitle)
        },
        onDismissRequest = onDismiss,
        text = {
            Column {
                OutlinedTextField(
                    title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                OutlinedTextField(
                    description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(title, description)
                }
            ) {
                Text(confirmButton)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel", color = DisabledLightGray)
            }
        },
        containerColor = ContainerColor
    )
}
