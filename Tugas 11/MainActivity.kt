package com.example.authentication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.authentication.ui.LamanAplikasi
import com.example.authentication.ui.auth.LamanMasuk
import com.example.authentication.ui.auth.LamanOTP
import com.example.authentication.ui.auth.LamanDaftar
import com.example.authentication.ui.LayarMuat
import com.example.authentication.ui.LamanDepan
import com.example.authentication.ui.theme.authenticationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            authenticationTheme {
                Navigasi()
            }
        }
    }
}

sealed class Screen(val route: String) {
    data object LayarMuat : Screen("layarmuat")
    data object LamanDepan : Screen("lamandepan")
    data object Masuk : Screen("masuk")
    data object Daftar : Screen("daftar")
    data object OTP : Screen("otp")
    data object Aplikasi : Screen("aplikasi")
}

@Composable
fun Navigasi() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.LayarMuat.route) {

        composable(Screen.LayarMuat.route) {
            LayarMuat(onLayarMuatFinished = {
                navController.navigate(Screen.LamanDepan.route) {
                    popUpTo(Screen.LayarMuat.route) { inclusive = true }
                }
            })
        }

        composable(Screen.LamanDepan.route) {
            LamanDepan(
                onMasukClick = { navController.navigate(Screen.Masuk.route) },
                onDaftarClick = { navController.navigate(Screen.Daftar.route) }
            )
        }

        composable(Screen.Masuk.route) {
            LamanMasuk(
                onMasukClick = {
                    navController.navigate(Screen.Aplikasi.route) {
                        popUpTo(Screen.LamanDepan.route) { inclusive = true }
                    }
                },
                onDaftarClick = { navController.navigate(Screen.Daftar.route) }
            )
        }

        composable(Screen.Daftar.route) {
            LamanDaftar(
                onDaftarClick = { navController.navigate(Screen.OTP.route) },
                onMasukClick = { navController.navigate(Screen.Masuk.route) }
            )
        }

        composable(Screen.OTP.route) {
            LamanOTP(
                onVerifyClick = {
                    navController.navigate(Screen.Masuk.route) {
                        popUpTo(Screen.LamanDepan.route)
                    }
                }
            )
        }

        composable(Screen.Aplikasi.route) {
            LamanAplikasi(
                onKeluarClick = {
                    navController.navigate(Screen.LamanDepan.route) {
                        popUpTo(Screen.LamanDepan.route)
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun NavigasiPreview() {
    authenticationTheme {
        Navigasi()
    }
}
