package com.example.quantyc.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quantyc.data.local.entities.UserEntity
import com.example.quantyc.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(onNavigateToSignUp: () -> Unit, onLoginSuccess: (UserEntity) -> Unit) {
    val viewModel: AuthViewModel = hiltViewModel()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val loginState by viewModel.loginState.collectAsState()
    val loginErrorState by viewModel.loginErrorState.collectAsState()

    LaunchedEffect(loginState) {
        if (loginState) {
            val user = viewModel.fetchUser(viewModel.email.value)
            onLoginSuccess(user)
        }
    }

    if (loginErrorState) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissLoginErrorDialog() },
            title = { Text("Login Failed") },
            text = { Text("The email or password you entered is incorrect.") },
            confirmButton = {
                Button(onClick = { viewModel.dismissLoginErrorDialog() }) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = { viewModel.email.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = password,
            onValueChange = { viewModel.password.value = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.login()
                      },
            modifier = Modifier.fillMaxWidth()) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onNavigateToSignUp,
            modifier = Modifier.fillMaxWidth()) {
            Text("Sign Up")
        }
    }
}
