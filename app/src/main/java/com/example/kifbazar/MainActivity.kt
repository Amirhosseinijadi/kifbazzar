package com.example.kifbazar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.category.CategoryScreen
import com.example.di.mymodules
import com.example.main.MainScreen
import com.example.model.repository.TokenInMemory
import com.example.model.repository.UserRepository
import com.example.product.ProductScreen
import com.example.signin.SigninScreen
import com.example.utill.MyScreens
import com.ui.IntroScreen
import com.example.signup.MainCardview
import com.example.signup.SignupScreen
import com.example.utill.KEY_CATEGORY_ARG
import com.example.utill.KEY_PRODUCT_ARG
import com.ui.ui.theme.BackgroundMain
import com.ui.ui.theme.MainAppTheme
import dev.burnoo.cokoin.Koin
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.KoinNavHost
import org.koin.android.ext.koin.androidContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



         setContent {
         Koin(appDeclaration = {
             androidContext(this@MainActivity)
             modules(mymodules)
         }) {
             MainAppTheme {
                 Surface(color = BackgroundMain, modifier = Modifier.fillMaxSize()) {

                     val userrepository:UserRepository = get()
                     userrepository.loadToken()

                     Dunibazarui()
                 }


             }

         }
         }

     }
    }




@Composable
fun Dunibazarui(){

    val navcontroller = rememberNavController()
    val navhost = KoinNavHost(navController = navcontroller, startDestination = MyScreens.MainScreen.route ){

        composable(MyScreens.MainScreen.route){

           if(TokenInMemory.token == null){
               IntroScreen()
           }else{
               MainScreen()
           }

        }
        composable(route = MyScreens.ProductScreen.route + "/" + "{$KEY_PRODUCT_ARG}"
            , arguments = listOf(navArgument(KEY_PRODUCT_ARG){
            type = NavType.StringType
        })){
            ProductScreen(it.arguments!!.getString(KEY_PRODUCT_ARG,"null"))
        }
        composable(route = MyScreens.CategoryScreen.route + "/" + "{$KEY_CATEGORY_ARG}"
            , listOf(navArgument(KEY_CATEGORY_ARG){
            type = NavType.StringType
        })){
            CategoryScreen(it.arguments!!.getString(KEY_CATEGORY_ARG, "null"))
        }



        composable(MyScreens.ProfileScreen.route){
            ProfileScreen()
        }

        composable(MyScreens.CartScreen.route){
            CartScreen()
        }
        composable(MyScreens.SignUpScreen.route){
            SignupScreen()
        }
        composable(MyScreens.SignInScreen.route){
            SigninScreen()
        }
        composable(MyScreens.IntroScreen.route){
            IntroScreen()
        }

    }
}


@Composable
fun ProfileScreen() {

}

@Composable
fun CartScreen() {

}










@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainAppTheme {
        Dunibazarui()
    }
}