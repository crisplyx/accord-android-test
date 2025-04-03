package id.rasyiid.accord_android_test.views.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import id.rasyiid.accord_android_test.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import id.rasyiid.accord_android_test.domain.home.dto.ProductDto
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.sp
import id.rasyiid.accord_android_test.views.UIState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.grid.items

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), onProductClicked: (ProductDto) -> Unit) {
    val productsState by viewModel.productsState.collectAsState()

    // Trigger product fetch on composition
    LaunchedEffect(Unit) {
        viewModel.getProducts(null)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 24.dp, 0.dp, 0.dp),
        contentAlignment = Alignment.Center
    ) {
        when (productsState) {
            is UIState.Idle -> {}
            is UIState.Loading -> {
                CircularProgressIndicator()
            }
            is UIState.Success -> {
                val products = (productsState as UIState.Success<List<ProductDto>>).data

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    contentPadding = PaddingValues(8.dp),
                    userScrollEnabled = true
                ) {
                    items(products) { product ->
                        ProductItem(
                            product,
                            onClick = { onProductClicked(product) }
                        )
                    }
                }
            }

            is UIState.Error -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = (productsState as UIState.Error).message,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.getProducts(null) }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: ProductDto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clickable{ onClick() },
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Product Image
            AsyncImage(
                model = product.image,
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.error_image)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Product Title
            Text(
                text = product.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Price and Rating
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${product.price}",
                    color = MaterialTheme.colors.primary
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "%.1f".format(product.rating.rate),
                    )
                }
            }
        }
    }
}