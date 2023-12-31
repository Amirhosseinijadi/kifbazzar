package com.example.main


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.kifbazar.R
import com.example.model.data.Ads
import com.example.model.data.Product
import com.example.utill.CATEGORY
import com.example.utill.MyScreens
import com.example.utill.NetworkChecker
import com.example.utill.TAGS
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ui.ui.theme.BackgroundMain
import com.ui.ui.theme.CardViewBackground
import com.ui.ui.theme.MainAppTheme
import com.ui.ui.theme.Shapes
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel
import org.koin.core.parameter.parametersOf

@Preview(showBackground = true)
@Composable
fun MainScreenPreview(){
    
    
    MainAppTheme {
        
        Surface(modifier = Modifier.fillMaxSize(), color = BackgroundMain) {

            MainScreen()
            
        }
        
    }
    
}

@Composable
fun MainScreen(){

    val context = LocalContext.current
    
    val uicontroller = rememberSystemUiController()
    SideEffect {
        uicontroller.setStatusBarColor(Color.White)
    }

    val viewmodel = getNavViewModel<MainViewModel>(
        parameters ={parametersOf(NetworkChecker(context).isInternetConnected)})
    val navigation = getNavController()
    
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(bottom = 16.dp)) {

        if(viewmodel.showprogressbar.value){
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Blue
            )

        }

        Toptoolbar(onCartclicked = {navigation.navigate(MyScreens.CartScreen.route)}, onProfileclicked = {navigation.navigate(MyScreens.ProfileScreen.route)})

        Categorybar(CATEGORY){
           navigation.navigate(MyScreens.CategoryScreen.route + "/" + it)
        }

        val productDataState = viewmodel.dataproducts
        val adsDataState = viewmodel.dataads
        Productsubjectlist(TAGS,productDataState.value,adsDataState.value){
                navigation.navigate(MyScreens.ProductScreen.route + "/" + it)
        }

    }

}

@Composable
fun Productsubjectlist(tags : List<String>,products: List<Product>,ads:List<Ads>,onProductclicked :(String) -> Unit) {
    if(products.isNotEmpty()){
    Column {
        tags.forEachIndexed { it, _ ->
            val withtagdata = products.filter { product -> product.tags == tags[it] }
            ProductSubject(tags[it],withtagdata.shuffled(),onProductclicked)

            if(ads.size>=2)
                if(it == 1 || it == 2)
                    BigpictureTablighat(ads[it - 1],onProductclicked)



    }

}
}
}

@Composable
fun Toptoolbar(onCartclicked :() -> Unit,onProfileclicked :() -> Unit) {
    
    TopAppBar(elevation = 0.dp,backgroundColor = Color.White,
        title = { Text(text = "Kif bazaar") }, actions = {
          IconButton(onClick = {onCartclicked.invoke()}) {
              Icon(Icons.Default.ShoppingCart, contentDescription = null)

          }
            IconButton(onClick = {onProfileclicked.invoke()}) {
                
                Icon(Icons.Default.Person,null)
                
            }
        }
    )

}

@Composable
fun Categorybar(categorylist:List<Pair<String,Int>>,onCategoryclicked :(String) -> Unit) {

LazyRow(modifier = Modifier.padding(16.dp), contentPadding = PaddingValues(end = 16.dp) ){

    items(categorylist.size){
        CategoryItem(categorylist[it],onCategoryclicked)
    }

}


}

@Composable
fun CategoryItem(subject:Pair<String,Int>,onCategoryclicked :(String) -> Unit) {
    Column(modifier = Modifier
        .padding(start = 16.dp)
        .clickable {
                   onCategoryclicked.invoke(subject.first)
        }, horizontalAlignment = Alignment.CenterHorizontally ) {
        Surface (shape = Shapes.medium, color = CardViewBackground){
            Image(painter = painterResource(id = subject.second)
                , contentDescription = null
                , modifier = Modifier.padding(16.dp))
        }
        
        Text(text =subject.first, modifier = Modifier.padding(top = 4.dp), style = TextStyle(color = Color.Gray)  )
    }
}

@Composable
fun ProductSubject(subject:String,data:List<Product>,onProductclicked :(String) -> Unit) {

    Column(modifier = Modifier.padding(top = 32.dp)){
        Text(text = subject, modifier = Modifier.padding(start = 16.dp), style = MaterialTheme.typography.h6)

        Productbar(data,onProductclicked)
    }

}

@Composable
fun Productbar(data:List<Product>,onProductclicked :(String) -> Unit) {
    LazyRow(modifier = Modifier.padding(top = 16.dp), contentPadding = PaddingValues(end = 16.dp)){
        items(data.size){
            Productitem(data[it],onProductclicked)
        }
    }
}

@Composable
fun Productitem(product:Product,onProductclicked :(String) -> Unit) {
    Card(
        modifier = Modifier
            .clickable { onProductclicked.invoke(product.productId) }
            .padding(start = 16.dp)
            , elevation = 4.dp, shape = Shapes.large) {

        Column {

            AsyncImage(model = product.imgUrl,
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop,
                contentDescription = null)

            Column(
                modifier = Modifier.padding(10.dp)
            ) {

                Text(
                    text = product.name,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                )

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = product.price + " Tomans",
                    style = TextStyle(fontSize = 14.sp)
                )

                Text(
                    text = product.soldItem + " SOLD",
                    style = TextStyle(color = Color.Gray, fontSize = 13.sp)
                )

            }
        }
    }
}


@Composable
fun BigpictureTablighat(ads: Ads,onProductclicked :(String) -> Unit) {

    AsyncImage(modifier = Modifier
        .clickable { onProductclicked.invoke(ads.productId) }
        .padding(16.dp)
        .fillMaxWidth()
        .height(260.dp), model = ads.imageURL, contentDescription = null, contentScale = ContentScale.Crop )

}






