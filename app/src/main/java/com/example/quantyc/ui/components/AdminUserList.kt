package com.example.quantyc.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quantyc.data.local.entities.UserEntity

@Composable
fun AdminUserList(
    users: List<UserEntity>,
    onUpdateUser: (UserEntity) -> Unit,
    onDeleteUser: (UserEntity) -> Unit
) {
    LazyColumn {
        items(
            count = users.size,
        ) { index ->
            val user = users[index]

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = user.id.toString(), modifier = Modifier.weight(1f))
                Text(text = user.username, modifier = Modifier.weight(2f))
                Text(text = user.email, modifier = Modifier.weight(3f))
                Text(text = user.role, modifier = Modifier.weight(2f))

                IconButton(onClick = { onUpdateUser(user) }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Update User")
                }
                IconButton(onClick = { onDeleteUser(user) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete User")
                }
            }
        }
    }
}