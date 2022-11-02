package github.swissonid.productdetail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import github.swissonid.productdetail.ui.theme.MOrange
import github.swissonid.productdetail.ui.theme.ProductDetailTheme
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductDetailPage()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailPage() {
    val timeToElevate = 300
    ProductDetailTheme {
        Scaffold ( containerColor = Color.LightGray ) {
            val padding = it
            var startAnimation by remember { mutableStateOf(false) }

            val shrinkTransition = updateTransition(startAnimation, label = "Add item to basket")
            val imageSize by shrinkTransition.animateIntSize(
                transitionSpec = {
                    tween(
                        durationMillis = 500,
                        easing = EaseOutBounce,
                        delayMillis = timeToElevate + 100,
                    )
                }, label = "imageSize"
            ) { state ->
                if (state) IntSize(40, 40) else IntSize(360, 360)
            }
            val cornerPercent by shrinkTransition.animateInt(
                transitionSpec = {
                    tween(durationMillis = 500, easing = EaseInOutQuad , delayMillis = timeToElevate / 2)
                }, label = "rounding"
            ) { state ->
                if (state) 50 else 5
            }

            val shadowElevation by shrinkTransition.animateDp(
                transitionSpec = {
                    tween(durationMillis = timeToElevate, easing = EaseOutBounce)
                }, label = "elevation"
            ) { state ->
                if (state) 32.dp else 0.dp
            }
            val moveTransition = updateTransition(startAnimation, label = "Move to bottom")
            val shrinkTime = 1000
            val offset by moveTransition.animateIntOffset(
                transitionSpec = {
                    tween(
                        durationMillis = 500,
                        easing = EaseInOutQuad,
                        delayMillis = shrinkTime + 150,
                    )
                }, label = "offset"
            ) { state ->
                if (state) IntOffset(0, 240) else IntOffset(0, 0)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(56.dp))
                    Box {
                        val shape = RoundedCornerShape(percent = 5)
                        Surface(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(360.dp, 360.dp)
                                .alpha(0.5f)
                                .clip(shape)
                        ) {
                            val grayColorMatrix = ColorMatrix()
                            grayColorMatrix.setToSaturation(0F)
                            Image(
                                painter = painterResource(id = R.drawable.banna_image),
                                contentDescription = "Product",
                                contentScale = ContentScale.Crop,
                                colorFilter = ColorFilter.colorMatrix(grayColorMatrix),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .blur(5.dp)
                                    .alpha(0.6f)
                            )
                        }


                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .offset(offset.x.dp, offset.y.dp),
                            contentAlignment = Alignment.Center) {
                            val shape = RoundedCornerShape(percent = cornerPercent)
                            Surface(
                                modifier = Modifier
                                    .size(imageSize.width.dp, imageSize.height.dp)
                                    .shadow(shadowElevation, shape)
                                    .clip(shape)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.banna_image),
                                    contentDescription = "Product",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        AddButton(onClick = {
                            startAnimation = !startAnimation
                        })
                    }
                }
            }
        }
}

@Composable
fun AddButton(amountOfItems: Int = 0,
              onClick: () ->Unit = {},) {
    val showBigButton = amountOfItems == 0

    ElevatedButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MOrange,
            contentColor = Color.White,
            disabledContentColor = Color.DarkGray,
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        AnimatedVisibility(visible = showBigButton) {

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
            Text(text = "Add to cart")
        }

    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    ProductDetailPage()
}