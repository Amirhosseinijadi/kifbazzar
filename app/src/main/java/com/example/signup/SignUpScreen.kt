package com.example.signup


import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kifbazar.R
import com.example.utill.MyScreens
import com.example.utill.NetworkChecker
import com.example.utill.VALUE_SUCCESS
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ui.ui.theme.Blue
import com.ui.ui.theme.MainAppTheme
import com.ui.ui.theme.Shapes
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainAppTheme {
        SignupScreen()
    }
}

@Composable
fun SignupScreen() {
    val uicontroller = rememberSystemUiController()
    SideEffect {
        uicontroller.setStatusBarColor(Blue)
    }
    val context = LocalContext.current
    val navigation = getNavController()
    val viewmodel = getNavViewModel<SignUpviewmodel>()

    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .background(Blue)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            IconApp()

            MainCardview(navigation,viewmodel) {

                viewmodel.signUpuser {
                    if (it == VALUE_SUCCESS) {

                        navigation.navigate(MyScreens.MainScreen.route){
                            popUpTo(MyScreens.IntroScreen.route){
                                inclusive = true
                            }
                        }

                    } else{
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                }
                



            }

        }


    }


}

@Composable
fun IconApp() {

    Surface(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_icon_app),
            contentDescription = null,
            modifier = Modifier.padding(12.dp)
        )
    }

}


@Composable
fun MainCardview(navigation:NavController, viewmodel: SignUpviewmodel, Signupevent: () -> Unit) {
    val name = viewmodel.name.observeAsState("")
    val email = viewmodel.email.observeAsState("")
    val password = viewmodel.password.observeAsState("")
    val confirmpassword = viewmodel.confirmpassword.observeAsState("")
    val context = LocalContext.current

    Card(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
        elevation = 10.dp,
        shape = Shapes.medium
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Sign Up",
                modifier = Modifier.padding(top = 18.dp, bottom = 18.dp),
                color = Color.Blue
            )

            Maintextfield(
                edtvalue = name.value,
                icon = R.drawable.ic_person,
                hint = "Your Full Name"
            ) { viewmodel.name.value = it }
            Maintextfield(
                edtvalue = email.value,
                icon = R.drawable.ic_email,
                hint = "Enter Your Email"
            ) { viewmodel.email.value = it }
            Passwordtextfield(
                edtvalue = password.value,
                icon = R.drawable.ic_password,
                hint = "Enter Your Password"
            ) { viewmodel.password.value = it }
            Passwordtextfield(
                edtvalue = confirmpassword.value,
                icon = R.drawable.ic_password,
                hint = "Confirm Your Password"
            ) { viewmodel.confirmpassword.value = it }

            Button(
                onClick = {
                       if(name.value.isNotEmpty() && email.value.isNotEmpty() && password.value.isNotEmpty() && confirmpassword.value.isNotEmpty()) {

                           if(password.value == confirmpassword.value){

                               if(password.value.length >= 8){

                                   if(Patterns.EMAIL_ADDRESS.matcher(email.value).matches()){
                                       if(NetworkChecker(context).isInternetConnected){
                                           Signupevent.invoke()
                                       }else{
                                           Toast.makeText(context, "please connect your internet!", Toast.LENGTH_SHORT).show()
                                       }



                                   }else{
                                       Toast.makeText(context, "your email adress is not correct!", Toast.LENGTH_SHORT).show()
                                   }



                               }else{
                                   Toast.makeText(context, "your password is less than 8 char", Toast.LENGTH_SHORT).show()
                               }

                           }else{

                               Toast.makeText(context, "Passwords are not the same!", Toast.LENGTH_SHORT).show()

                           }

                       }else{
                           Toast.makeText(context, "Please write your data first...", Toast.LENGTH_SHORT).show()
                       }

                },
                modifier = Modifier.padding(top = 28.dp, bottom = 8.dp)
            ) {

                Text(text = "Register Account", modifier = Modifier.padding(8.dp))

            }
            Row(
                modifier = Modifier.padding(bottom = 18.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Already have an Account?")
                TextButton(onClick = {navigation.navigate(MyScreens.SignInScreen.route){
                    popUpTo(MyScreens.SignUpScreen.route){
                        inclusive = true
                    }
                } }) {

                    Text(text = "Log in", color = Color.Blue)

                }

            }
        }

    }

}


@Composable
fun Maintextfield(edtvalue: String, icon: Int, hint: String, Onvaluechange: (String) -> Unit) {

    OutlinedTextField(value = edtvalue,
        onValueChange = Onvaluechange,
        singleLine = true,
        placeholder = { Text(text = hint) },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(12.dp),
        shape = Shapes.medium,
        leadingIcon = { Icon(painter = painterResource(icon), contentDescription = null) }


    )


}

@Composable
fun Passwordtextfield(edtvalue: String, icon: Int, hint: String, Onvaluechange: (String) -> Unit) {

    val passwordisvisible = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = edtvalue,
        onValueChange = Onvaluechange,
        singleLine = true,
        placeholder = { Text(text = hint) },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(12.dp),
        shape = Shapes.medium,
        leadingIcon = {
            Icon(painter = painterResource(icon), contentDescription = null)
        }
        , keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation()

        )


}




