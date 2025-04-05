package id.rasyiid.accord_android_test.views.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import id.rasyiid.accord_android_test.domain.product.entity.ProductEntity
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.primarySurface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import java.text.NumberFormat

@Composable
fun CartScreen(viewModel: CartViewModel = hiltViewModel()) {

    val cartItemsState by viewModel.cartItemsState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCarts()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().padding(0.dp, 24.dp, 0.dp, 0.dp),
        topBar = {
            TopAppBar(
                title = { Text("Your Cart") },
                backgroundColor = MaterialTheme.colors.primarySurface
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (cartItemsState.isEmpty()) {
                EmptyCartView()
            } else {
                // List of cart items
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cartItemsState, key = { it.id }) { product ->
                        CartItem(
                            product = product,
                            onQuantityChange = { newQuantity ->
                                viewModel.updateQuantity(product.id, newQuantity)
                            },
                            onRemove = {
                                viewModel.removeProduct(product.id)
                            }
                        )
                    }
                }

                // Checkout section
                CheckoutSection(
                    totalItems = cartItemsState.sumOf { it.quantity },
                    totalPrice = cartItemsState.sumOf { it.price * it.quantity },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

}

@Composable
private fun EmptyCartView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Your cart is empty",
                style = MaterialTheme.typography.h6
            )
            Text(
                text = "Browse products to add items to your cart",
                style = MaterialTheme.typography.body2,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun CartItem(
    product: ProductEntity,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product image
            AsyncImage(
                model = product.image,
                contentDescription = null,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))

            // Product details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.h6,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = product.category,
                    style = MaterialTheme.typography.caption,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = formatCurrency(product.price),
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold
                )
            }

            // Quantity controls
            QuantitySelector(
                currentQuantity = product.quantity,
                onQuantityChange = onQuantityChange,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // Remove button
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove",
                    tint = MaterialTheme.colors.error
                )
            }
        }
    }
}


@Composable
private fun QuantitySelector(
    currentQuantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onQuantityChange(currentQuantity - 1) },
            enabled = currentQuantity > 1
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Decrease quantity"
            )
        }

        Text(
            text = "$currentQuantity",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        IconButton(
            onClick = { onQuantityChange(currentQuantity + 1) }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Increase quantity"
            )
        }
    }
}


@Composable
private fun CheckoutSection(
    totalItems: Int,
    totalPrice: Double,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Divider()

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total ($totalItems items)",
                style = MaterialTheme.typography.body1
            )
            Text(
                text = formatCurrency(totalPrice),
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Handle checkout */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = totalItems > 0,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White
            )
        ) {
            Text(text = "Proceed to Checkout")
        }
    }
}

private fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 2
    return format.format(amount)
}