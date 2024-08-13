package com.example.quantyc.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.quantyc.data.local.entities.UserEntity

@Composable
fun UpdateUserDialog(
    user: UserEntity?,
    onDismiss: () -> Unit,
    onUpdateConfirm: (UserEntity) -> Unit
) {
    if (user == null) return

    var updatedUsername by remember { mutableStateOf(user.username) }
    var updatedEmail by remember { mutableStateOf(user.email) }


    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update User") },
        text = {
            Column {
                TextField(
                    value = updatedUsername,
                    onValueChange = { updatedUsername = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = updatedEmail,
                    onValueChange = { updatedEmail = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onUpdateConfirm(
                    user.copy(
                        username = updatedUsername,
                        email = updatedEmail,
                    )
                )
            }) {
                Text("Update")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}