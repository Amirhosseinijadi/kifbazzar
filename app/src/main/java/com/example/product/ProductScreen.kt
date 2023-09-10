package com.example.product


import android.content.res.Configuration
import android.graphics.Paint.Style
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.kifbazar.R
import com.example.model.data.Ads
import com.example.model.data.Comment
import com.example.model.data.Product
import com.example.utill.CATEGORY
import com.example.utill.MyScreens
import com.example.utill.NetworkChecker
import com.example.utill.TAGS
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ui.ui.theme.*
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel
import org.koin.core.parameter.parametersOf

@Preview(showBackground = true)
@Composable
fun ProductScreenPreview() {


    MainAppTheme {

        Surface(modifier = Modifier.fillMaxSize(), color = BackgroundMain) {

            ProductScreen("")

        }

    }

}

@Composable
fun ProductScreen(productid: String) {

    val context = LocalContext.current

    val viewModel = getNavViewModel<ProductViewmodel>()
    viewModel.loaddata(productid,NetworkChecker(context).isInternetConnected)

    val navigation = getNavController()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 58.dp)
                .verticalScroll(rememberScrollState())
        ) {
            ProductToolbar(
                productName = "Details",
                badgeNumber = 0,
                OnBackClicked = {
                    navigation.popBackStack()
                },
                OnCartClicked = {

                    if (NetworkChecker(context).isInternetConnected) {
                        navigation.navigate(MyScreens.CartScreen.route)
                    } else {
                        Toast.makeText(
                            context,
                            "please connect to internet first...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
            
          Productitem(data = viewModel.thisProduct.value,comments = viewModel.comments.value, onCategoryClicked = {
              navigation.navigate(MyScreens.CategoryScreen.route + "/" + it)
          }, onAddnewcomment = {})

            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier.padding(top = 14.dp, bottom = 14.dp,start = 8.dp, end = 8.dp)
            )

            ProductDetail(viewModel.thisProduct.value,viewModel.comments.value.size.toString())

            Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(top = 14.dp, bottom = 4.dp,start = 8.dp, end = 8.dp))


            ProductComment(viewModel.comments.value){
               viewModel.addNewComment(productid,it,{})
            }





        }
    }
}
@Composable
fun CommentBody(comment:Comment){
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 8.dp)
        , elevation = 0.dp
    ,border = BorderStroke(1.dp, Color.LightGray)
    , shape = Shapes.large
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
          Text(text = comment.userEmail,style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold))
          Text(modifier = Modifier.padding(top = 10.dp), text = comment.text,style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal))
      }
    }
}

@Composable
fun ProductComment(comments:List<Comment>,AddnewComment : (String) -> Unit){
     val showcommentDialog = remember{ mutableStateOf(false) }
    val context = LocalContext.current

    if(comments.isNotEmpty()){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(modifier = Modifier.padding(start = 8.dp),text = "Comments", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium))
            TextButton(onClick = {
                if(NetworkChecker(context).isInternetConnected){
                    showcommentDialog.value = true
                }else{
                    Toast.makeText(context, "connect your internet!", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text(text = "Add New Comment", style = TextStyle(fontSize = 14.sp))
            }
        }
        comments.forEach {
            CommentBody(comment = it)
        }
    }else{
        TextButton(onClick = {
            if (NetworkChecker(context).isInternetConnected) {
                showcommentDialog.value = true
            } else {
                Toast.makeText(context, "connect your internet!", Toast.LENGTH_SHORT).show()
            }
        }){
            Text(text = "Add New Comment", style = TextStyle(fontSize = 14.sp))
        }
    }

    if(showcommentDialog.value){
        AddCommentDialog(
            OnDismiss = { showcommentDialog.value = false }
            , OnPositiveClick =  { AddnewComment.invoke(it) }
        )
    }
}
@Composable
fun AddCommentDialog(OnDismiss: () -> Unit,OnPositiveClick: (String) -> Unit)
{

    val context = LocalContext.current
    val userComment = remember { mutableStateOf("") }

    Dialog(onDismissRequest = OnDismiss) {

        Card(
            modifier = Modifier.fillMaxHeight(0.53f),
            elevation = 8.dp,
            shape = Shapes.medium
        ) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Write Your Comment",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // enter data =>
                MainTextField(
                    edtValue = userComment.value,
                    hint = "write something..."
                ) {
                    userComment.value = it
                }

                // Buttons =>
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = { OnDismiss.invoke() }) {
                        Text(text = "Cancel")
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    TextButton(onClick = {

                        if (userComment.value.isNotEmpty() && userComment.value.isNotBlank()) {
                            if (NetworkChecker(context).isInternetConnected) {
                                OnPositiveClick(userComment.value)
                                OnDismiss.invoke()
                            } else {
                                Toast.makeText(
                                    context,
                                    "connect to internet first...",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(context, "please write  first...", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }) {
                        Text(text = "Ok")
                    }
                }
            }
        }
    }
}
@Composable
fun MainTextField(edtValue: String, hint: String, OnValueChanges: (String) -> Unit) {

    OutlinedTextField(
        label = { Text(text = hint) },
        value = edtValue,
        singleLine = false,
        maxLines = 2,
        onValueChange = OnValueChanges,
        placeholder = { Text(text = "Write Something...") },
        modifier = Modifier.fillMaxWidth(0.9f),
        shape = Shapes.medium
    )

}






@Composable
fun ProductDetail(data:Product,commentnumber:String){
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column()
        {
            Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier.padding(8.dp)) {
                Image(painterResource(id =R.drawable.ic_details_comment )
                    , contentDescription = null
                    , modifier = Modifier.size(26.dp)
                )
                Text(text = commentnumber + "Comments", modifier = Modifier.padding(start = 6.dp), fontSize = 13.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier.padding(8.dp)) {
                Image(painterResource(id =R.drawable.ic_details_material )
                    , contentDescription = null
                    , modifier = Modifier.size(26.dp)
                )
                Text(text = data.material, modifier = Modifier.padding(start = 6.dp), fontSize = 13.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                Image(painterResource(id =R.drawable.ic_details_sold )
                    , contentDescription = null
                    , modifier = Modifier.size(26.dp)
                )
                Text(text = data.soldItem + " Sold", modifier = Modifier.padding(start = 6.dp), fontSize = 13.sp)
            }
        }

        Surface(modifier = Modifier
            .clip(Shapes.medium)
            .align(Alignment.Bottom)
            .padding(end = 8.dp)
            ,color = Blue
        ) {
            Text(text = data.tags, color = Color.White,modifier = Modifier.padding(6.dp), style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium))
        }

    }
}

@Composable
fun Productitem(comments:List<Comment>,data:Product,onCategoryClicked: (String) -> Unit,onAddnewcomment : (String) -> Unit){
    Column(modifier = Modifier.padding(16.dp)) {
        ProductDesign(data,onCategoryClicked)
            

    }
}

@Composable
fun ProductToolbar(
    productName: String,
    badgeNumber: Int,
    OnBackClicked: () -> Unit,
    OnCartClicked: () -> Unit,
) {

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { OnBackClicked.invoke() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        },
        elevation = 2.dp,
        backgroundColor = Color.White,
        modifier = Modifier.fillMaxWidth(),
        title = {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp),
                text = productName,
                textAlign = TextAlign.Center
            )
        },
        actions = {

            IconButton(
                modifier = Modifier.padding(end = 6.dp),
                onClick = { OnCartClicked.invoke() }
            ) {

                if (badgeNumber == 0) {
                    Icon(Icons.Default.ShoppingCart, null)
                } else {

                    BadgedBox(badge = { Badge { Text(badgeNumber.toString()) } }) {
                        Icon(Icons.Default.ShoppingCart, null)
                    }
                }
            }
        }
    )
}

@Composable
fun ProductDesign(data: Product, onCategoryClicked: (String) -> Unit) {

    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(Shapes.medium),
        model = data.imgUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )

    Text(
        modifier = Modifier.padding(top = 14.dp, start = 10.dp),
        text = data.name,
        style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 20.sp)
    )
    Text(
        modifier = Modifier
            .padding(top = 4.dp, start = 12.dp, end = 10.dp),
        text = data.detailText,
        style = TextStyle(fontSize = 15.sp, textAlign = TextAlign.Justify)
    )
    TextButton(onClick = {onCategoryClicked.invoke(data.category)}) {
        Text(
            text = "#" + data.category,
            style = TextStyle(fontSize = 13.sp)
        )
        
    }




}


@Composable
fun AddToCart(
    price: String,
    isAddingProduct: Boolean,
    OnCartClicked: () -> Unit,
) {

    val configuration = LocalConfiguration.current
    val fraction =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 0.15f else 0.08f

    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(182.dp, 40.dp),
                onClick = { OnCartClicked.invoke() }
            ) {

                if (isAddingProduct) {

                } else {

                    Text(
                        text = "Add Product To Cart",
                        modifier = Modifier.padding(2.dp),
                        color = Color.White,
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    )

                }

            }


            Surface(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clip(Shapes.medium),
                color = PriceBackground
            ) {

                Text(
                    modifier = Modifier.padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 6.dp,
                        bottom = 6.dp
                    ),
                    text = " Tomans",
                    style = TextStyle(fontSize = 14.sp),
                    fontWeight = FontWeight.Medium
                )

            }


        }

    }


}

@Composable
fun DotsTyping() {

    val dotSize = 10.dp
    val delayUnit = 350
    val maxOffset = 10f

    @Composable
    fun Dot(
        offset: Float,
    ) = Spacer(
        Modifier
            .size(dotSize)
            .offset(y = -offset.dp)
            .background(
                color = Color.White,
                shape = CircleShape
            )
            .padding(start = 8.dp, end = 8.dp)
    )

    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun animateOffsetWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = delayUnit * 4
                0f at delay with LinearEasing
                maxOffset at delay + delayUnit with LinearEasing
                0f at delay + delayUnit * 2
            }
        )
    )

    val offset1 by animateOffsetWithDelay(0)
    val offset2 by animateOffsetWithDelay(delayUnit)
    val offset3 by animateOffsetWithDelay(delayUnit * 2)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = maxOffset.dp)
    ) {
        val spaceSize = 2.dp

        Dot(offset1)
        Spacer(Modifier.width(spaceSize))
        Dot(offset2)
        Spacer(Modifier.width(spaceSize))
        Dot(offset3)
    }
}









