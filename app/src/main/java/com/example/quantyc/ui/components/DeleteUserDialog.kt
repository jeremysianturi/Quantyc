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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.quantyc.data.local.entities.UserEntity

@Composable
fun DeleteUserDialog(
    user: UserEntity?,
    password: String,
    onPasswordChange: (String) -> Unit,
    onDeleteConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (user == null) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete User") },
        text = {
            Column {
                Text("Are you sure you want to delete ${user.username}?")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text("Enter your password to confirm") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = onDeleteConfirm) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}