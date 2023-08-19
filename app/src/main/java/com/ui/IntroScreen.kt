package com.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.utill.MyScreens
import com.example.signup.SignupScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ui.ui.theme.Blue
import com.ui.ui.theme.MainAppTheme
import dev.burnoo.cokoin.navigation.getNavController

@Preview(showBackground = true)
@Composable
fun IntroScreenPreview() {
    MainAppTheme {
        IntroScreen()
    }
}

@Composable
fun IntroScreen() {
    val uicontroller = rememberSystemUiController()
    SideEffect {
        uicontroller.setStatusBarColor(Blue)
    }
    val navigation = getNavController()

    Image(modifier = Modifier.fillMaxSize() , painter = painterResource(com.example.kifbazar.R.drawable.img_intro ), contentDescription = null, contentScale = ContentScale.Crop )
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.78f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
        Button(onClick = {
                         navigation.navigate(MyScreens.SignUpScreen.route)
        }
            , modifier = Modifier.fillMaxWidth(0.7f)) {

            Text(text = "Sign Up"
                , color = Color.White)

        }

        Button(onClick = {
                         navigation.navigate(MyScreens.SignInScreen.route)
        }
            , modifier = Modifier.fillMaxWidth(0.7f),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)

        ) {

            Text(text = "Sign in"
                , color = Color.Blue)

        }



    }

}