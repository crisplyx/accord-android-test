package id.rasyiid.accord_android_test.views.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import id.rasyiid.accord_android_test.domain.auth.dto.AddressDto
import id.rasyiid.accord_android_test.domain.auth.dto.SignInResponseDto
import id.rasyiid.accord_android_test.domain.auth.dto.UserDto
import id.rasyiid.accord_android_test.views.SecurityManager
import id.rasyiid.accord_android_test.views.UIState

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel(), securityManager: SecurityManager? = null, onSignInSuccess: (SignInResponseDto) -> Unit, onSignOutSuccess: () -> Unit) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isLoading = false
    val signInState by viewModel.signInState.collectAsState()
    val userDetailState by viewModel.userDetailState.collectAsState()

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Expanded,
        skipHalfExpanded = true
    )

    LaunchedEffect(Unit) {
        securityManager?.getToken()?.let { token ->
            viewModel.getUserDetail(token, securityManager.getSub())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 24.dp, 0.dp, 0.dp),
        contentAlignment = Alignment.Center,
    ) {

        if(securityManager?.getToken() == null) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.signIn(username, password) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Text("Sign In")
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (signInState) {
                    is UIState.Idle -> {
                        isLoading = false
                    }
                    is UIState.Loading -> {
                        isLoading = true
                        CircularProgressIndicator()
                    }
                    is UIState.Success -> {
                        isLoading = false
                        val signInResponseDto = (signInState as UIState.Success<SignInResponseDto>).data
                        securityManager?.storeAuthInfo(signInResponseDto)
                        onSignInSuccess(signInResponseDto)
                    }
                    is UIState.Error -> {
                        isLoading = false
                        Text(
                            text = (signInState as UIState.Error).message,
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        } else {

            when (userDetailState) {
                is UIState.Idle -> {}
                is UIState.Loading -> {
                    CircularProgressIndicator()
                }
                is UIState.Success -> {
                    val userDetail = (userDetailState as UIState.Success<UserDto>).data

                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("Welcome back ${userDetail.name.firstname} ${userDetail.name.lastname}")
                        Button(
                            onClick = {
                                securityManager.clear()
                                onSignOutSuccess()
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Sign Out")
                        }
                    }

                    UserDetailBottomSheet(
                        user = (userDetailState as UIState.Success<UserDto>).data,
                        sheetState = sheetState
                    )

                    LaunchedEffect(Unit) {
                        sheetState.show()
                    }
                }
                is UIState.Error -> {
                    isLoading = false
                    Text(
                        text = (userDetailState as UIState.Error).message,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
            }

        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserDetailBottomSheet(
    user: UserDto,
    sheetState: ModalBottomSheetState,
) {
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetElevation = 8.dp,
        sheetContent = {
            UserDetailContent(user = user)
            Spacer(modifier = Modifier.height(16.dp))
        }
    ) {
        // Empty main content
    }
}

@Composable
fun UserDetailContent(user: UserDto) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header with close button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "User Profile",
                style = MaterialTheme.typography.h6
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Profile Section
        ProfileSection(user)

        Spacer(modifier = Modifier.height(24.dp))

        // Contact Section
        ContactSection(user)

        Spacer(modifier = Modifier.height(24.dp))

        // Address Section
        AddressSection(user.address)
    }
}

@Composable
private fun ProfileSection(user: UserDto) {
    Column {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.name.firstname.take(1) + user.name.lastname.take(1),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "${user.name.firstname} ${user.name.lastname}",
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = "@${user.username}",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun ContactSection(user: UserDto) {
    Column {
        Text(
            text = "Contact",
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        DetailRow(icon = Icons.Default.Email, text = user.email)
        DetailRow(icon = Icons.Default.Phone, text = user.phone)
    }
}

@Composable
private fun AddressSection(address: AddressDto) {
    Column {
        Text(
            text = "Address",
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        DetailRow(icon = Icons.Default.Home, text = "${address.number} ${address.street}")
        DetailRow(icon = Icons.Default.LocationCity, text = address.city)
        DetailRow(icon = Icons.Default.PinDrop, text = address.zipcode)

        // Map Button
        Button(
            onClick = { /* Open maps */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.1f),
                contentColor = MaterialTheme.colors.primary
            ),
            elevation = null,
            border = BorderStroke(1.dp, MaterialTheme.colors.primary.copy(alpha = 0.2f))
        ) {
            Icon(Icons.Default.Map, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("View on Map")
        }
    }
}

@Composable
private fun DetailRow(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.body1)
    }
}