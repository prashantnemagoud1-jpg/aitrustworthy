package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.Screen
import com.example.viewmodel.TrustViewModel
import com.example.ui.theme.*

@Composable
fun AuthScreen(
    viewModel: TrustViewModel,
    mode: String
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBackground)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { viewModel.navigateTo(Screen.Landing) }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back back",
                        tint = CyberPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = CyberPrimary,
                glowColor = CyberPrimary
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = CyberPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "TRUSTSHIELD PORTAL",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                when (mode) {
                    "login" -> {
                        Text("USER LOGIN", color = CyberPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it; errorMsg = "" },
                            label = { Text("CONSOLE EMAIL", color = TextSecondary) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberPrimary,
                                unfocusedBorderColor = CyberBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it; errorMsg = "" },
                            label = { Text("PASSWORD KEY", color = TextSecondary) },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberPrimary,
                                unfocusedBorderColor = CyberBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        if (errorMsg.isNotBlank()) {
                            Text(errorMsg, color = TrustHigh, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        CyberButton(
                            onClick = {
                                if (email.isBlank() || password.isBlank()) {
                                    errorMsg = "ALL IDENTIFICATION CREDENTIALS REQUIRED."
                                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                    errorMsg = "INVALID CONSOLE EMAIL SYNTAX."
                                } else {
                                    viewModel.login(email)
                                }
                            },
                            text = "AUTHENTICATE PROFILE",
                            testTag = "login_submit_button"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        TextButton(
                            onClick = { viewModel.navigateTo(Screen.ForgotPassword) },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("FORGOT ENCRYPTED PASSCODE?", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        TextButton(
                            onClick = { viewModel.navigateTo(Screen.SignUp) },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("CREATE ZERO-TRUST ACCOUNT", color = CyberTertiary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    "signup" -> {
                        Text("SIGN IN ENROLLMENT", color = CyberTertiary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it; errorMsg = "" },
                            label = { Text("OPERATOR FULL NAME", color = TextSecondary) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberPrimary,
                                unfocusedBorderColor = CyberBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it; errorMsg = "" },
                            label = { Text("DEFENSE CONSOLE EMAIL", color = TextSecondary) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberPrimary,
                                unfocusedBorderColor = CyberBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it; errorMsg = "" },
                            label = { Text("CIPHER PASSCODE (6+ CHAR)", color = TextSecondary) },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberPrimary,
                                unfocusedBorderColor = CyberBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        if (errorMsg.isNotBlank()) {
                            Text(errorMsg, color = TrustHigh, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        CyberButton(
                            onClick = {
                                if (name.isBlank() || email.isBlank() || password.isBlank()) {
                                    errorMsg = "ALL SECURITY PARAMETERS MUST BE SATISFIED."
                                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                    errorMsg = "INVALID EMAIL FORMAT."
                                } else if (password.length < 6) {
                                    errorMsg = "PASSCODE REQUIREMENT METRIC UNSATISFIED (MIN 6 CHAR)."
                                } else {
                                    viewModel.signUp(name, email)
                                }
                            },
                            text = "ESTABLISH CONTINUOUS SHIELD",
                            containerColor = CyberTertiary,
                            testTag = "signup_submit_button"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        TextButton(
                            onClick = { viewModel.navigateTo(Screen.Login) },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("EXISTING USER CORE? AUTHENTICATE", color = CyberPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    "forgot" -> {
                        Text("CIPHER ACCESS REINTEGRATION", color = CyberAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Specify your registered console email below. We will push authorization reset codes to recover your key profile.",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 15.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it; errorMsg = "" },
                            label = { Text("REGISTERED EMAIL", color = TextSecondary) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberPrimary,
                                unfocusedBorderColor = CyberBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        if (errorMsg.isNotBlank()) {
                            Text(errorMsg, color = if (errorMsg.contains("SENT")) TrustGreen else TrustHigh, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        CyberButton(
                            onClick = {
                                if (email.isBlank()) {
                                    errorMsg = "EMAIL SPECIFICATION REQUIRED."
                                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                    errorMsg = "INVALID EMAIL STRUCTURE."
                                } else {
                                    errorMsg = "AUTHORIZATION DECODER KEY SENT. BACKUP RECOVERY DISPATCHED!"
                                }
                            },
                            text = "DISPATCH AUTHORIZATION CODE",
                            containerColor = CyberAccent,
                            testTag = "forgot_submit_button"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        TextButton(
                            onClick = { viewModel.navigateTo(Screen.Login) },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("RETURN TO AUTHENTICATION CONSOLE", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
