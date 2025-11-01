package com.pthw.food.ui.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.pthw.food.utils.ext.alertDialog
import com.pthw.food.utils.ext.textButton
import fooddimultiplatform.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

/**
 * Created by P.T.H.W on 27/07/2024.
 */

@Composable
fun PermissionDialog(onRequestPermission: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(true) }

    if (showWarningDialog) {
        AlertDialog(
            modifier = Modifier.alertDialog(),
            title = { Text(stringResource(Res.string.notification_permission_title)) },
            text = { Text(stringResource(Res.string.notification_permission_description)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onRequestPermission()
                        showWarningDialog = false
                    },
                    modifier = Modifier.textButton(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) { Text(text = stringResource(Res.string.request_notification_permission)) }
            },
            onDismissRequest = { }
        )
    }
}