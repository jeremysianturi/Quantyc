package com.example.quantyc.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.quantyc.data.local.entities.UserEntity
import com.example.quantyc.domain.model.Photo
import com.example.quantyc.ui.components.PhotoItem
import com.example.quantyc.ui.viewmodel.AuthViewModel
import com.example.quantyc.ui.viewmodel.PhotoViewModel

@Composable
fun MainScreen(user: UserEntity, onLogout: () -> Unit, context: Context) {
    val viewModel: PhotoViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()

    val photos = viewModel.photosFlow.collectAsLazyPagingItems()
    val users by authViewModel.usersFlow.collectAsState()
    val successUpdatedDataAsAdmin by authViewModel.updateUserState.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    val albumIds = listOf(1, 2, 3, 4, 5)
    var selectedAlbum by remember { mutableStateOf(albumIds[0]) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var userToDelete by remember { mutableStateOf<UserEntity?>(null) }

    var showUpdateDialog by remember { mutableStateOf(false) }
    var userToUpdate by remember { mutableStateOf<UserEntity?>(null) }

    var password by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.query, viewModel.selectedAlbumId) {
        if (user.role == "Admin") {
            authViewModel.getUsers()
        } else {
            viewModel.getPhotos()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Welcome, ${user.username}!", color = Color.White) },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (user.role == "Admin") {
                AdminUserList(
                    users = users,
                    onUpdateUser = { userEntity ->
                        userToUpdate = userEntity
                        showUpdateDialog = true
//                        authViewModel.updateUser(userEntity)
//                        if(successUpdatedDataAsAdmin){
//                            Toast.makeText(context, "successfully update user data", Toast.LENGTH_SHORT).show()
//                        } else {
//                            Toast.makeText(context, "failed to update user data", Toast.LENGTH_SHORT).show()
//                        }

                                   },
                    onDeleteUser = { userEntity ->
                        userToDelete = userEntity
                        showDeleteDialog = true
                    }
                )
            } else {
                // Normal User View
                TextField(
                    value = viewModel.query,
                    onValueChange = {
                        viewModel.query = it
                        viewModel.getPhotos()
                    },
                    label = { Text("Search") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Album ID Dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Filter by Album ID: $selectedAlbum")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        albumIds.forEach { albumId ->
                            DropdownMenuItem(onClick = {
                                selectedAlbum = albumId
                                viewModel.selectedAlbumId = albumId
                                expanded = false
                                viewModel.getPhotos()
                            }) {
                                Text(text = albumId.toString())
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Photo List
                LazyColumn {
                    items(
                        count = photos.itemCount,
                        key = photos.itemKey { it.id },
                        contentType = photos.itemContentType { it.title }
                    ) { index ->
                        val photo = photos[index]
                        if (photo != null) {
                            PhotoItem(photo = photo)
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        DeleteUserDialog(
            user = userToDelete,
            password = password,
            onPasswordChange = { password = it },
            onDeleteConfirm = {
                if (password == user.password) {
                    authViewModel.deleteUser(userToDelete!!.email)
                    showDeleteDialog = false
                    password = ""
                    Toast.makeText(context, "Succesfully delete the user!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Wrong Password!", Toast.LENGTH_SHORT).show()
                }

            },
            onDismiss = {
                showDeleteDialog = false
                password = ""
            }
        )
    }

    if (showUpdateDialog) {

        LaunchedEffect(successUpdatedDataAsAdmin) {
            if (!successUpdatedDataAsAdmin) {
                Toast.makeText(context, "Successfully updated the user!", Toast.LENGTH_SHORT).show()
                // Reset the state to avoid multiple toasts
                authViewModel.resetUpdateUserState()
            } else {
                Toast.makeText(context, "Failed to update the user!", Toast.LENGTH_SHORT).show()
            }
        }

        UpdateUserDialog(
            user = userToUpdate,
            onDismiss = { showUpdateDialog = false },
            onUpdateConfirm = { updatedUser ->
                authViewModel.updateUser(updatedUser)
                showUpdateDialog = false
                if (successUpdatedDataAsAdmin) {
                    Toast.makeText(context, "Successfully updated the user!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to update the user!", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}

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



@Composable
fun LoadingView(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(modifier = Modifier.padding(8.dp), text = message)
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun ErrorView(errorMessage: String?, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(64.dp),
            imageVector = Icons.Rounded.Warning,
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = errorMessage ?: "Unknown error",
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onRetry,
            content = { Text(text = "Retry") },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
            )
        )
    }
}