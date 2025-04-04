package id.rasyiid.accord_android_test.views.product

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import id.rasyiid.accord_android_test.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Chip
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import id.rasyiid.accord_android_test.domain.product.dto.ProductDto
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import id.rasyiid.accord_android_test.domain.product.entity.ProductEntity
import id.rasyiid.accord_android_test.views.UIState
import kotlin.Double
import kotlin.String

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    productId: Int,
    onAddToCart: (ProductDto, Int) -> Unit,
    onBackClick: () -> Unit
) {
    var quantity by remember { mutableIntStateOf(1) }
    val scrollState = rememberScrollState()
    val productDetailState by viewModel.productDetailState.collectAsState()

    // Trigger product fetch on composition
    LaunchedEffect(Unit) {
        viewModel.getProductDetail(null, productId)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 24.dp, 0.dp, 0.dp),
        topBar = {
            TopAppBar(
                title = { Text("Product Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                elevation = 4.dp
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.addCartItem(
                    ProductEntity(
                        id = (productDetailState as UIState.Success<ProductDto>).data.id,
                        title = (productDetailState as UIState.Success<ProductDto>).data.title,
                        price = (productDetailState as UIState.Success<ProductDto>).data.price,
                        description = (productDetailState as UIState.Success<ProductDto>).data.description,
                        category =  (productDetailState as UIState.Success<ProductDto>).data.category,
                        image = (productDetailState as UIState.Success<ProductDto>).data.image,
                        quantity = quantity
                    )
                ) },
                icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Add to cart") },
                text = { Text("Add to Cart") },
                backgroundColor = MaterialTheme.colors.primary
            )
        }
    ) { padding ->

        when(productDetailState) {
            is UIState.Idle -> {}
            is UIState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 24.dp, 0.dp, 0.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is UIState.Success -> {
                val product = (productDetailState as UIState.Success<ProductDto>).data

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(scrollState),
                ) {
                    // Product Image
                    AsyncImage(
                        model = product.image,
                        contentDescription = product.title,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = painterResource(R.drawable.placeholder),
                        error = painterResource(R.drawable.error_image)
                    )

                    // Product Details
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = product.title,
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Price and Rating
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Text(
                                text = "$${"%.2f".format(product.price)}",
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.primary
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            RatingBar(rating = product.rating.rate)
                        }

                        // Quantity Selector
                        QuantitySelector(
                            quantity = quantity,
                            onQuantityChange = { quantity = it },
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Description
                        Text(
                            text = product.description,
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Category Chip
                        Chip(
                            onClick = {},
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Text(
                                text = product.category.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.caption
                            )
                        }
                    }
                }

            }
            is UIState.Error -> {
                Toast.makeText(LocalContext.current, (productDetailState as UIState.Error).message, Toast.LENGTH_SHORT).show()
            }
        }

    }
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = 2.dp,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(4.dp)
        ) {
            IconButton(
                onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Decrease")
            }

            Text(
                text = quantity.toString(),
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            IconButton(
                onClick = { onQuantityChange(quantity + 1) },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Increase")
            }
        }
    }
}

@Composable
fun RatingBar(rating: Double) {
    Row {
        repeat(5) { index ->
            Icon(
                imageVector = if (index < rating) Icons.Filled.Star else Icons.Default.Star,
                contentDescription = null,
                tint = if (index < rating) Color(0xFFFFC107) else Color.LightGray,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = "%.1f".format(rating),
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}