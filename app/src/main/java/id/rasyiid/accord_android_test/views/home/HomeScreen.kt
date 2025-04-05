package id.rasyiid.accord_android_test.views.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import id.rasyiid.accord_android_test.domain.product.dto.ProductDto
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.sp
import id.rasyiid.accord_android_test.views.UIState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), onProductClicked: (ProductDto) -> Unit) {
    val productsState by viewModel.productsState.collectAsState()
    val productsFilteredState by viewModel.productsFilteredState.collectAsState()

    var categories = remember(productsState) {
        when (productsState) {
            is UIState.Success -> {
                viewModel.setUnfilteredProducts((productsState as UIState.Success<List<ProductDto>>).data)
                listOf("All") + (productsState as UIState.Success<List<ProductDto>>).data
                    .map { it.category }
                    .distinct()
                    .sorted()
            }
            else -> emptyList()
        }
    }
    var selectedCategory by remember { mutableStateOf("All") }

    // Trigger product fetch on composition
    LaunchedEffect(Unit) {
        viewModel.getProducts(null)
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(0.dp, 24.dp, 0.dp, 0.dp)
    ) {
        // Category Filter - Using Buttons styled as chips
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            categories.forEach { category ->
                Button(
                    onClick = {
                        selectedCategory = category
                        viewModel.selectCategory(selectedCategory)
                    },
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .height(32.dp), // Chip-like height
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (selectedCategory == category) {
                            MaterialTheme.colors.primary
                        } else {
                            MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                        },
                        contentColor = if (selectedCategory == category) {
                            Color.White
                        } else {
                            MaterialTheme.colors.onSurface
                        }
                    ),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    ),
                    shape = RoundedCornerShape(16.dp) // Chip-like shape
                ) {
                    Text(
                        text = category.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (productsFilteredState) {
                is UIState.Idle -> {}
                is UIState.Loading -> {
                    CircularProgressIndicator()
                }
                is UIState.Success -> {
                    val products = (productsFilteredState as UIState.Success<List<ProductDto>>).data

                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 150.dp),
                        contentPadding = PaddingValues(8.dp),
                        userScrollEnabled = true
                    ) {
                        items(
                            items = products,
                            key = { product -> product.id }
                        ) { product ->
                            ProductItem(
                                product,
                                onClick = {
                                    onProductClicked(product)
                                }
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
                            text = (productsFilteredState as UIState.Error).message,
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

